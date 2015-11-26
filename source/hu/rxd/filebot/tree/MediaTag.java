package hu.rxd.filebot.tree;

import com.google.common.base.Objects;

public class MediaTag implements Comparable<MediaTag>{

	@Deprecated
	private String tagName;
	private String value;

	public MediaTag(String string) {
		if(string==null)
			throw new RuntimeException();
		this.tagName = string;
	}

	public MediaTag(String string, String value) {
		if(string==null)
			throw new RuntimeException();
		this.tagName = string;
		this.value = value;
		
	}
	public MediaTag(MediaTagKey key, String value) {
		this.tagName = key.toString();
		this.value = value;
		
	}

	public MediaTag(MediaTagKey key) {
		tagName=key.toString();
	}

	@Override
	public int compareTo(MediaTag o) {
		return tagName.compareTo(o.tagName);
	}
	@Override
	public String toString() {
		if(value==null)
			return tagName;
		else
		return String.format("%s[%s]", tagName,value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MediaTag){
			MediaTag o = (MediaTag) obj;
			return Objects.equal(tagName, o.tagName) && Objects.equal(value, o.value);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(tagName,value);
	}

	public String getTagName() {
		return tagName;
	}
	
	public String getValue() {
		return value;
	}

	public MediaTagKey getKey() {
		return MediaTagKey.valueOf(tagName);
	}

}
