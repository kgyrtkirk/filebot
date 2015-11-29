package hu.rxd.filebot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import com.google.common.base.Function;

import hu.rxd.filebot.SeriesMatch.IndexEntryExtractor;
import hu.rxd.filebot.SeriesMatch.MatchResult;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;
import hu.rxd.sdi.StringDistanceIndex;
import hu.rxd.sdi.StringDistanceIndex.Result;
import info.debatty.java.stringsimilarity.MetricLCS;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import net.filebot.media.MediaDetection;
import net.filebot.media.MediaDetection.IndexEntry;
import net.filebot.web.Movie;

public class MovieMatcher implements ISectionVisitor {

	static StringDistanceIndex<IndexEntry<Movie>, Function<IndexEntry<Movie>, String>> sdiMovies;

	static StringDistanceIndex<IndexEntry<Movie>, Function<IndexEntry<Movie>, String>> getMovieIndex()
			throws IOException {
		if (sdiMovies == null) {
			sdiMovies = new StringDistanceIndex<>(
					MediaDetection.getMovieIndex(),
					new IndexEntryExtractor<>(),
					new NormalizedLevenshtein());
//			);

		}
		return sdiMovies;
	}

	@Override
	public void visit(ISection node) throws Exception {

		List<String> searchKeys = new ArrayList<>();

		
		if(!node.getParent().hasTag(MediaTagKey.isRoot)){
			searchKeys.add(node.getParent().getName());
		}
		if(node.hasTag(MediaTagKey.year)){
			searchKeys.add(node.getName()+ " "+node.getTag(MediaTagKey.year).getValue());
		}
		searchKeys.add(node.getName());
//		TreeSet< Result<IndexEntry<Movie>> > matches =new TreeSet<>();	
		for (String q : searchKeys) {
			Result<IndexEntry<Movie>> best = getMovieIndex().queryBest(q.replaceAll("[._ ]+", " ").toLowerCase());

			if (best.getDistance() < 0.1) {
				node.addTag(new MediaTag(MediaTagKey.canBeMovie));
				node.addTag(new MediaTag(MediaTagKey.movie, best.getPayload().getLenientName()));
				return;
			}else{
				{
					System.out.println("-M-");
					System.out.println(best);
					System.out.println(node);
				}
			}
		}
		// System.out.println("asd");
		// SeriesMatch sm = new SeriesMatch(node.getName());
		// if(!sm.isMatch()){
		// System.out.println(sm.getDistance());
		// System.out.println(node);
		// }

	}
	// static class IndexEntryExtractor {
	// }

}