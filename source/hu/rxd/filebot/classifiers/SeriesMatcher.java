package hu.rxd.filebot.classifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagType;
import hu.rxd.filebot.tree.SearchKey;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.similarity.SeriesNameMatcher;

public class SeriesMatcher implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		
		Queue<SearchKey>	searchKeys=new PriorityQueue<>();
		SeriesNameMatcher seriesNameMatcher = new SeriesNameMatcher(Locale.getDefault(), false);

		String sn = seriesNameMatcher.matchByEpisodeIdentifier(node.getName());
		if(sn!=null)
			searchKeys.add(new SearchKey(0.0f, sn));
		ISection parent = node.getParent();
		if(parent.hasTag(MediaTag.canBeSeries)){
			sn = seriesNameMatcher.matchByEpisodeIdentifier(parent.getName());
			if(sn!=null)
				searchKeys.add(new SearchKey(0.1f, sn));
			searchKeys.add(new SearchKey(1.1f, parent.getName()));
		}
		searchKeys.addAll(node.getSearchKeys(MediaTag.series));
		if(parent.hasTag(MediaTag.canBeSeries)){
			searchKeys.add(new SearchKey(2.1f, parent.getName()));
		}

		searchKeys.add(new SearchKey(2.2f, node.getName()));
		
		PriorityQueue<SeriesMatch.MatchResult>		results=new PriorityQueue<>();
		for (SearchKey sk : searchKeys) {
			results.add(	SeriesMatch.lookup(sk.getQueryStr().replaceAll("[-. ]+", " ").toLowerCase(),0.1));
		}
		SeriesMatch.MatchResult best = results.peek();
		if(best.distance < 0.1){
			node.addTag(MediaTag.canBeSeries,true);
			node.addSearchKey(MediaTag.series, -1.0f,best.result.getLenientName());
//			node.addTag(MediaTag.series,best.result.getLenientName());
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
