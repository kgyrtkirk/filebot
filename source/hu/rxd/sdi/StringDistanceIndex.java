package hu.rxd.sdi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Function;

import info.debatty.java.stringsimilarity.interfaces.NormalizedStringDistance;

public class StringDistanceIndex<S, K extends Function<S, String>> {

	static class Entry<S> {

		private String label;
		private S payload;

		public Entry(String label, S payload) {
			this.label = label;
			this.payload = payload;
		}
	}

	public static class Result<S> {

		double distance;
		private S payload;

		public Result(double distance, S payload) {
			this.distance = distance;
			this.payload = payload;
		}
		public double getDistance() {
			return distance;
		}
		public S getPayload() {
			return payload;
		}
		
		@Override
		public String toString() {
			return "[distance: "+distance+ ", payload: "+payload+"]";
		}
	}

	private List<Entry<S>> entries;
	private NormalizedStringDistance m;

	public StringDistanceIndex(Collection<S> _entries, Function<S, String> extractor, NormalizedStringDistance m) {
		this.m = m;
		entries = new ArrayList<>();
		for (S e : _entries) {
			entries.add(new Entry<S>(extractor.apply(e), e));
		}
	}

	public TreeSet<Result<S>> query(String needle, double maxDistance) {
		TreeSet<Result<S>> ret = new TreeSet<>();
		for (Entry<S> entry : entries) {
			double dist = m.distance(needle, entry.label);
			if (dist > maxDistance) {
				continue;
			}
			ret.add(new Result<S>(dist, entry.payload));
		}
		return ret;
	}
	public Result<S> queryBest(String needle) {
		
		Result<S>	ret=null;
		for (Entry<S> entry : entries) {
			double dist = m.distance(needle, entry.label);
			if (ret != null && ret.distance < dist) {
				continue;
			}
			ret=new Result<S>(dist, entry.payload);
		}
		return ret;
	}

}
