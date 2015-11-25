package hu.rxd.filebot;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

import org.junit.Test;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringDistance;
import net.filebot.cli.CmdlineOperations;
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
	public void asd(String s) throws Exception{
		
	}
	
	public SeriesMatch(String name) throws Exception {
		SeriesNameMatcher strictSeriesNameMatcher = new SeriesNameMatcher(Locale.getDefault(), false);
		String s = strictSeriesNameMatcher.matchByEpisodeIdentifier(name);
		
		List<IndexEntry<SearchResult>> si = MediaDetection.getSeriesIndex();
		Ex1Comparator comparator = new Ex1Comparator(s!=null?s:name);
		PriorityQueue<IndexEntry<SearchResult>> pq = new PriorityQueue<IndexEntry<SearchResult>>(comparator);
		pq.addAll(si);
		IndexEntry<SearchResult> e;
//		for(int i=0;i<10;i++){
		//System.out.println(
				e=pq.poll();
				//);
		System.out.println(comparator.debug(e));
		System.out.println(e.getObject().getEffectiveNames());
//		}
		bestMatch=e;
		distance=comparator.distance(bestMatch);
//		System.out.println(s);
	}

	public boolean isMatch() {
		return distance<0.0001;
	}

	public IndexEntry<SearchResult> getR() {
		return bestMatch;
	}

}
