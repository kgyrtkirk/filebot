package hu.rxd.filebot.classifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

import hu.rxd.filebot.SeriesMatch;
import hu.rxd.filebot.SeriesMatch.MatchResult;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.similarity.SeriesNameMatcher;
import sun.reflect.annotation.TypeAnnotation.TypeAnnotationTarget;

public class SeriesMatcher implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		
		List<String>	searchKeys=new ArrayList<>();
		SeriesNameMatcher seriesNameMatcher = new SeriesNameMatcher(Locale.getDefault(), false);

		String sn = seriesNameMatcher.matchByEpisodeIdentifier(node.getName());
		if(sn!=null)
			searchKeys.add(sn);
		ISection parent = node.getParent();
		if(parent.hasTag(MediaTagKey.canBeSeries)){
			sn = seriesNameMatcher.matchByEpisodeIdentifier(parent.getName());
			if(sn!=null)
				searchKeys.add(sn);
			searchKeys.add(parent.getName());
		}
		searchKeys.addAll(node.getSearchKeys(MediaTagKey.series));
		if(parent.hasTag(MediaTagKey.canBeSeries)){
			searchKeys.add(parent.getName());
		}

		searchKeys.add(node.getName());
		
		PriorityQueue<SeriesMatch.MatchResult>		results=new PriorityQueue<>();
		for (String key : searchKeys) {
			results.add(	SeriesMatch.lookup(key,0.1));
		}
		MatchResult best = results.peek();
		if(best.distance < 0.01){
			node.addTag(new MediaTag(MediaTagKey.canBeSeries));
			node.addTag(new MediaTag(MediaTagKey.series,best.result.getLenientName()));
		}else{
			System.out.println("---");
			System.out.println(best);
			System.out.println(node);
		}
//		System.out.println("asd");
//		SeriesMatch sm = new SeriesMatch(node.getName());
//		if(!sm.isMatch()){
//			System.out.println(sm.getDistance());
//			System.out.println(node);
//		}
		
	}

}
