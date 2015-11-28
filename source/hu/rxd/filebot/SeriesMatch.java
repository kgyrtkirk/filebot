package hu.rxd.filebot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import hu.rxd.sdi.StringDistanceIndex;
import hu.rxd.sdi.StringDistanceIndex.Result;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringDistance;
import net.filebot.media.MediaDetection;
import net.filebot.media.MediaDetection.IndexEntry;
import net.filebot.similarity.SeriesNameMatcher;
import net.filebot.web.Movie;
import net.filebot.web.SearchResult;

public class SeriesMatch {

	
	static class Ex2Comparator implements Comparator<IndexEntry<Movie>>{
		private String key;
		private NormalizedStringDistance cs;

		public Ex2Comparator(String key) {
			this.key = key.toLowerCase();
//			cs=new SorensenDice();
//			cs=new Jaccard();
//			cs=new MetricLCS();
			cs=new NormalizedLevenshtein();
		}

		@Override
		public int compare(IndexEntry<Movie> o1, IndexEntry<Movie> o2) {
			double v1 = cs.distance(key,o1.getLenientName().toLowerCase());
			double v2 = cs.distance(key,o2.getLenientName().toLowerCase());
			if(v1<v2)return -1;
			else
				return 1;
		}

		public String debug(IndexEntry<Movie> e) {
			return cs.distance(key, e.getLenientName().toLowerCase()) + " " +e.getLenientName();
		}
		
	}
public	static class KeyDistance {
		private NormalizedStringDistance cs;
		private String key;
		public KeyDistance(String key) {
			this.key = key;
			cs=new NormalizedLevenshtein();
		}

		public double distance(String o) {
			return cs.distance(key, o);
		}
	}

//	static class KeyDistanceCompare implements Comparator<String>{
//
//		private NormalizedStringDistance cs;
//		private String key;
//		public KeyDistanceCompare(KeyDistance c) {
//			this.key = key.toLowerCase();
//			cs=new NormalizedLevenshtein();
//			
//		}
//
//		@Override
//		public int compare(String o1, String o2) {
//			// TODO Auto-generated method stub
//			Double.compare(d1, d2)
//			double v1 = cs.distance(key,o1.getLenientName().toLowerCase());
//			return 0;
//		}
//		
//	}

//	static class StringDistanceMapper {
//		private String key;
//		private double minDistance;
//		private NormalizedStringDistance cs;
//		
//		public StringDistanceMapper(String key,double minDistance) {
//			this.key = key;
//			this.minDistance = minDistance;
//			cs=new NormalizedLevenshtein();
//		}
//		public double distance(String s) {
//			return cs.distance(key,s);
//		}
//		
//	}


	
	static class Ex1Comparator implements Comparator<IndexEntry<SearchResult>>{
		private String key;
		private NormalizedStringDistance cs;

		
		public Ex1Comparator(String key) {
			this.key = key.toLowerCase();
//			cs=new SorensenDice();
//			cs=new Jaccard();
//			cs=new MetricLCS();
			cs=new NormalizedLevenshtein();
		}

		@Override
		public int compare(IndexEntry<SearchResult> o1, IndexEntry<SearchResult> o2) {
			double v1 = cs.distance(key,o1.getLenientName().toLowerCase());
			double v2 = cs.distance(key,o2.getLenientName().toLowerCase());
			if(v1<v2)return -1;
			else
				return 1;
		}

		public String debug(IndexEntry<SearchResult> e) {
			return cs.distance(key, e.getLenientName().toLowerCase()) + " " +e.getLenientName();
		}
		public double distance(IndexEntry<SearchResult> e) {
			return cs.distance(key, e.getLenientName().toLowerCase());
		}
		
	}

	private IndexEntry<SearchResult> bestMatch;
	private double distance;
	
	public static class IndexEntryExtractor implements Function<IndexEntry<SearchResult>, String>{

		@Override
		public String apply(IndexEntry<SearchResult> input) {
			return input.getLenientName().toLowerCase();
		}
	}
	
	static StringDistanceIndex<IndexEntry<SearchResult>, Function<IndexEntry<SearchResult>,String>>	sdiSeries;
	
	static StringDistanceIndex<IndexEntry<SearchResult>, Function<IndexEntry<SearchResult>, String>> getSdiSeries() throws IOException{
		if(sdiSeries==null){
			sdiSeries=			new StringDistanceIndex<>(
					MediaDetection.getSeriesIndex(),
					new IndexEntryExtractor(),
					new NormalizedLevenshtein());

		}
		return sdiSeries;
	}

	static LoadingCache<String, IndexEntry<SearchResult>>	c=CacheBuilder.newBuilder()
			.maximumSize(1000)
			.build(new SeriesLookup());

	static LoadingCache<String, Result<IndexEntry<SearchResult>>>	c2=CacheBuilder.newBuilder()
			.maximumSize(1000)
			.build(new SeriesLookup2());
	
	public static class SeriesLookup extends CacheLoader<String, IndexEntry<SearchResult>>{

		@Override
		public IndexEntry<SearchResult> load(String key) throws Exception {
			List<IndexEntry<SearchResult>> si = MediaDetection.getSeriesIndex();
			Ex1Comparator comparator = new Ex1Comparator(key);
			PriorityQueue<IndexEntry<SearchResult>> pq = new PriorityQueue<IndexEntry<SearchResult>>(comparator);
			pq.addAll(si);
			IndexEntry<SearchResult> e;
			e=pq.poll();
			return e;
		}
		
	}
	
	public static class SeriesLookup2 extends CacheLoader<String, Result<IndexEntry<SearchResult>>>{

		@Override
		public Result<IndexEntry<SearchResult>> load(String key) throws Exception {
			Result<IndexEntry<SearchResult>> res = getSdiSeries().queryBest(key);
			return res;
//			if(res.size()>0)
//				return res.first();
//			return null;
		}
		
	}
	
	public static final class MatchResult implements Comparable<MatchResult>{
		public MatchResult(String key) throws ExecutionException {
//			result=c.get(key.toLowerCase());
			Result<IndexEntry<SearchResult>> result0 = c2.get(key.toLowerCase());
//			if(result0 != null){
				result=result0.getPayload();
				distance=result0.getDistance();
//				distance=new Ex1Comparator(key).distance(result);
//			}
		}
		public double						distance;
		public IndexEntry<SearchResult>	result;
		@Override
		public int compareTo(MatchResult o) {
			return Double.compare(distance, o.distance);
		}
		
		@Override
		public String toString() {
			return "d:"+distance+" ; "+result;
		}
	}
	
	public static MatchResult lookup(String key, double maxDist) throws Exception{
		return new MatchResult(key);
	}
	
	public SeriesMatch(String name) throws Exception {
//		SeriesNameMatcher strictSeriesNameMatcher = new SeriesNameMatcher(Locale.getDefault(), false);
//		String s = strictSeriesNameMatcher.matchByEpisodeIdentifier(name);
//		
//		List<IndexEntry<SearchResult>> si = MediaDetection.getSeriesIndex();
		List<String>	searchKeys=new ArrayList<>();
		
		SeriesNameMatcher strictSeriesNameMatcher = new SeriesNameMatcher(Locale.getDefault(), false);

		String s = strictSeriesNameMatcher.matchByEpisodeIdentifier(name);
		
		if(s!=null){
			searchKeys.add(s);
		}
		String key = s!=null?s:name;
		Ex1Comparator comparator = new Ex1Comparator(key);
		
//		PriorityQueue<IndexEntry<SearchResult>> pq = new PriorityQueue<IndexEntry<SearchResult>>(comparator);
//		pq.addAll(si);
//		IndexEntry<SearchResult> e;
////		for(int i=0;i<10;i++){
//		//System.out.println(
//				e=pq.poll();
//				//);
////		System.out.println(comparator.debug(e));
////		System.out.println(e.getObject().getEffectiveNames());
////		}
		bestMatch=c.get(key);
		distance=comparator.distance(bestMatch);
//		System.out.println(s);
	}

	public boolean isMatch() {
		return distance<0.0001;
	}
	public double getDistance() {
		return distance;
	}

	public IndexEntry<SearchResult> getR() {
		return bestMatch;
	}

}
