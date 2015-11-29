package hu.rxd.filebot.classifiers;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.google.common.base.Function;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;
import hu.rxd.sdi.StringDistanceIndex;
import hu.rxd.sdi.StringDistanceIndex.Result;
import info.debatty.java.stringsimilarity.MetricLCS;
import net.filebot.WebServices;
import net.filebot.format.ExpressionFormat;
import net.filebot.format.MediaBindingBean;
import net.filebot.web.Movie;
import net.filebot.web.TMDbClient;

public class MovieIdentifactor implements ISectionVisitor {

	public static class MovieMapper implements Function<Movie, String>{

		@Override
		public String apply(Movie input) {
			return input.getName().toLowerCase();
		}
	}
	@Override
	public void visit(ISection node) throws Exception {
		TMDbClient db = WebServices.TheMovieDB;

		Locale language = Locale.getDefault();
		String movieName = node.getTag(MediaTagKey.movie).getValue();
		List<Movie> results = db.searchMovie(movieName, language);
		
		if(results.size()==0){
			System.out.println("no match for:"+node);
			return;
		}
		
		System.out.println(results);
		System.out.println(results.size());
		StringDistanceIndex<Movie, Function<Movie, String>> sdi = new StringDistanceIndex<>(results, new MovieMapper(), new MetricLCS());
		
		Result<Movie> best = sdi.queryBest(movieName.toLowerCase());
		
		if(best.getDistance()<0.01){
			
			MediaBindingBean mbb = new MediaBindingBean(best.getPayload(),null,null);
			ExpressionFormat	ef=new ExpressionFormat("{n} ({y})/{n} ({y})");
			String a = ef.format(mbb);
			a+="."+node.getTag(MediaTagKey.extension).getValue();
			Pattern ILLEGAL_CHARACTERS = Pattern.compile("[\\\\:*?\"<>|\\r\\n]|[ ]+$|(?<=[^.])[.]+$|(?<=.{250})(.+)(?=[.]\\p{Alnum}{3}$)");
			a=ILLEGAL_CHARACTERS.matcher(a).replaceAll("").replaceAll("\\s+", " ").trim();
			node.addTag(new MediaTag(MediaTagKey.movieOutput,a));
		}else{
			System.out.println("not assigning:" +best);
			System.out.println("to:" +node);
		}
//		node.addTag(new MediaTag(key));
		
//		KeyDistance distanceFn = new KeyDistance(seriesTag.getValue());
//		PriorityQueue<ScoredResult> pq = results.stream().map(a -> new ScoredResult(distanceFn, a))
//				.collect(Collectors.toCollection(() -> new PriorityQueue<ScoredResult>(ScoredResult.SCORE_COMPARATOR)));
//		
//		for (ScoredResult res : pq) {
//			if(res.distance>0.1){
//				break;
//			}
//			List<Episode> episodeList = db.getEpisodeList(res.result, sortOrder, language);
//			List<Episode> el=new ArrayList<>();
//			for (Episode e : episodeList) {
//				if(Objects.equals(e.getEpisode() , episode) && Objects.equals(e.getSeason() , season)){
//					el.add(e);
//				}
//			}
//			if(el.size()==1){
//				Episode s = el.get(0);
//				MediaBindingBean mbb = new MediaBindingBean(s,null,null);
//				ExpressionFormat	ef=new ExpressionFormat("{n}/{s00e00}.{t}");
//				String a = ef.format(mbb);
//				a+="."+node.getTag(MediaTagKey.extension).getValue();
//				Pattern ILLEGAL_CHARACTERS = Pattern.compile("[\\\\:*?\"<>|\\r\\n]|[ ]+$|(?<=[^.])[.]+$|(?<=.{250})(.+)(?=[.]\\p{Alnum}{3}$)");
//				a=ILLEGAL_CHARACTERS.matcher(a).replaceAll("").replaceAll("\\s+", " ").trim();
//				node.addTag(new MediaTag(MediaTagKey.seriesOutput,a));
////				node.tag(new MediaTag(MediaTagKey.));
////				System.out.println(a);
//				break;
////				node.addTag(new MediaTag(key));
//				
//			}
////			System.out.println(el);
//		}
	}

}
