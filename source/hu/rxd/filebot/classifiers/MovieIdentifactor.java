package hu.rxd.filebot.classifiers;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import hu.rxd.filebot.SeriesMatch.KeyDistance;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.WebServices;
import net.filebot.format.ExpressionFormat;
import net.filebot.format.MediaBindingBean;
import net.filebot.web.Movie;
import net.filebot.web.TMDbClient;

public class MovieIdentifactor implements ISectionVisitor {

	
	private boolean polite;
	static class ScoredResult {
		public static final Comparator<ScoredResult> SCORE_COMPARATOR = new Comparator<ScoredResult>() {
			@Override
			public int compare(ScoredResult o1, ScoredResult o2) {
				return Double.compare(o1.distance, o2.distance);
			}
		};
		
		private Movie result;
		private double distance;

		ScoredResult(KeyDistance kd, Movie result) {
			this.result = result;
			
			
			distance = kd.distance(result.getName());
			for (String alias : result.getAliasNames()) {
				distance=Math.min(distance, kd.distance(alias));
			}
		}
		Movie getPayload(){
			return result;
		}
	}
	private Map<String,String> m;
	public MovieIdentifactor(boolean polite) {
		this.polite = polite;
//		m=JsonReader.jsonToMaps(MovieIdentifactor.class.getResourceAsStream("norm.json"), new HashMap<String, Object>());

		
	}
	@Override
	public void visit(ISection node) throws Exception {
		TMDbClient db = WebServices.TheMovieDB;

		Locale language = Locale.getDefault();
		
		if(polite) {
			String movieName = node.getTag(MediaTag.movie);
			if(node.hasTag1(MediaTag.imdbId)){
				Set<Integer> imdbids = node.getTag(MediaTag.imdbId);
				doImdbLookup(node,db,imdbids);
			}
			doSearch(node, db, language, movieName);
		}else{
			if(!node.getParent().hasTag1(MediaTag.isRoot))
				if(doSearch(node, db, language, node.getParent().getName())){
					return;
				}
			doSearch(node, db, language, node.getName());
		}
	}
	private void doImdbLookup(ISection node, TMDbClient db, Set<Integer> imdbids) {
//		List<Movie> results = db.getMovieInfo();
		System.out.println("skipped");
		
	}
	private boolean doSearch(ISection node, TMDbClient db, Locale language, String movieName)
			throws IOException, ScriptException {
		movieName=movieName.replaceAll("[-. ]+", " ");
		final int	movieYear=getYear(node);
		List<Movie> results = db.searchMovie(movieName, movieYear, language,true);
		
		if(results.size()==0){
			System.out.println("no match for:"+node);
			return false;
		}
		
		System.out.println(results);
		System.out.println(results.size());
		
		KeyDistance distanceFn = new KeyDistance(movieName);
		PriorityQueue<ScoredResult> pq = results.stream().map(a -> new ScoredResult(distanceFn, a))
				.collect(Collectors.toCollection(() -> new PriorityQueue<ScoredResult>(ScoredResult.SCORE_COMPARATOR)));

		if(node.hasTag1(MediaTag.year)){
			pq.removeIf(res -> {
				if(movieYear!=res.getPayload().getYear()){
//					throw 
					System.out.println("purging res because: year: "+movieYear+"!="+res.getPayload().getYear());
//					return true;
					throw new RuntimeException("purging res because: year: "+movieYear+"!="+res.getPayload().getYear());
				}
				return false;
				
			});
		}

//		StringDistanceIndex<Movie, Function<Movie, String>> sdi = new StringDistanceIndex<>(results, new MovieMapper(), new MetricLCS());
//		
//		Result<Movie> best = sdi.queryBest(movieName.toLowerCase());
		for (ScoredResult scoredResult : pq) {
			if(scoredResult.distance>0.1){
				break;
			}
			ScoredResult best = scoredResult;
		if(best.distance<0.01 || pq.size()==1){
			node.addTag1(MediaTag.movieObj, best.getPayload());
			identified(node, best);
			return true;
		}else{
			System.out.println("not assigning:" +best);
			System.out.println("to:" +node);
		}
		}
		return false;
	}
	private int getYear(ISection node) {
		if(node.hasTag1(MediaTag.year)){
			return node.getTag(MediaTag.year);
		}
		return -1;
	}
	private void identified(ISection node, ScoredResult best) throws ScriptException {
		MediaBindingBean mbb = new MediaBindingBean(best.getPayload(),null,null);
		ExpressionFormat	ef=new ExpressionFormat("{n} ({y})/{n} ({y})");
		String a = ef.format(mbb);
		if(node.hasTag1(MediaTag.part)){
			a+="."+node.getTag(MediaTag.part);
		}
		a+="."+node.getTag(MediaTag.extension);
		Pattern ILLEGAL_CHARACTERS = Pattern.compile("[\\\\:*?\"<>|\\r\\n]|[ ]+$|(?<=[^.])[.]+$|(?<=.{250})(.+)(?=[.]\\p{Alnum}{3}$)");
		a=ILLEGAL_CHARACTERS.matcher(a).replaceAll("").replaceAll("\\s+", " ").trim();
		node.addTag1(MediaTag.movieOutput,a);
	}

}
