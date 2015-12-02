package hu.rxd.filebot.classifiers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.select.Collector;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.SeriesMatch.KeyDistance;
import hu.rxd.filebot.classifiers.SeriesIdentifactor.ScoredResult;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.WebServices;
import net.filebot.WebServices.TheTVDBClientWithLocalSearch;
import net.filebot.format.ExpressionFormat;
import net.filebot.format.MediaBindingBean;
import net.filebot.util.FileUtilities;
import net.filebot.web.Episode;
import net.filebot.web.SearchResult;
import net.filebot.web.SortOrder;

public class SeriesIdentifactor implements ISectionVisitor {

	static class ScoredResult {
		public static final Comparator<ScoredResult> SCORE_COMPARATOR = new Comparator<SeriesIdentifactor.ScoredResult>() {
			@Override
			public int compare(ScoredResult o1, ScoredResult o2) {
				return Double.compare(o1.distance, o2.distance);
			}
		};
		private SearchResult result;
		private double distance;

		ScoredResult(KeyDistance kd, SearchResult result) {
			this.result = result;
			
			
			distance = kd.distance(result.getName());
			for (String alias : result.getAliasNames()) {
				distance=Math.min(distance, kd.distance(alias));
			}
		}
	}

	@Override
	public void visit(ISection node) throws Exception {
		TheTVDBClientWithLocalSearch db = WebServices.TheTVDB;

		SortOrder sortOrder = SortOrder.DVD;
		Locale language = Locale.getDefault();
		String seriesName = node.getTag(MediaTagKey.series);
		List<SearchResult> results = db.search(seriesName, language);
		
		KeyDistance distanceFn = new KeyDistance(seriesName);
		PriorityQueue<ScoredResult> pq = results.stream().map(a -> new ScoredResult(distanceFn, a))
				.collect(Collectors.toCollection(() -> new PriorityQueue<ScoredResult>(ScoredResult.SCORE_COMPARATOR)));
		Integer	episode=node.getTag(MediaTagKey.episode);
		Integer	season=node.getTag(MediaTagKey.season);
		for (ScoredResult res : pq) {
			if(res.distance>0.1){
				continue;
			}
			List<Episode> episodeList = db.getEpisodeList(res.result, sortOrder, language);
			List<Episode> el=new ArrayList<>();
			for (Episode e : episodeList) {
				if(Objects.equals(e.getEpisode() , episode) && Objects.equals(e.getSeason() , season)){
					el.add(e);
				}
			}
			if(el.size()==1){
				Episode s = el.get(0);
				MediaBindingBean mbb = new MediaBindingBean(s,null,null);
				ExpressionFormat	ef=new ExpressionFormat("{n}/{s00e00}.{t}");
				String a = ef.format(mbb);
				a+="."+node.getTag(MediaTagKey.extension);
				Pattern ILLEGAL_CHARACTERS = Pattern.compile("[\\\\:*?\"<>|\\r\\n]|[ ]+$|(?<=[^.])[.]+$|(?<=.{250})(.+)(?=[.]\\p{Alnum}{3}$)");
				a=ILLEGAL_CHARACTERS.matcher(a).replaceAll("").replaceAll("\\s+", " ").trim();
				node.addTag1(MediaTagKey.seriesOutput,a);
//				node.tag(new MediaTag(MediaTagKey.));
//				System.out.println(a);
				break;
//				node.addTag(new MediaTag(key));
				
			}
			
//			System.out.println(el);
		}

	}

}
