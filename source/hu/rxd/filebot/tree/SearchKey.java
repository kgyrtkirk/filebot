package hu.rxd.filebot.tree;

public class SearchKey  implements Comparable<SearchKey>{

	private String queryStr;
	private float weight;

	public SearchKey(float weight, String queryStr) {
		this.weight = weight;
		this.queryStr = queryStr;
	}

	@Override
	public int compareTo(SearchKey o) {
		return Float.compare(weight, o.weight);
	}

	public String getQueryStr() {
		return queryStr;
	}

	public float getWeight() {
		return weight;
	}

}