package hu.rxd.filebot;

public class MediaTag implements Comparable<MediaTag>{

	private String tagName;
	private String value;

	public MediaTag(String string) {
		this.tagName = string;
	}

	public MediaTag(String string, String value) {
		this.value = value;
		
	}

	@Override
	public int compareTo(MediaTag o) {
		return tagName.compareTo(o.tagName);
	}
	@Override
	public String toString() {
		return String.format("%s[%s]", tagName,value);
	}

}
