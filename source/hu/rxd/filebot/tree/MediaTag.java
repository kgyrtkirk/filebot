package hu.rxd.filebot.tree;

import com.google.common.base.Objects;

public class MediaTag {

	private String value;
	private final MediaTagKey key;


	public MediaTag(MediaTagKey key, String value) {
		this.key = key;
		this.value = value;
		
	}

	public MediaTag(MediaTagKey key) {
		this.key=key;
	}

	@Override
	public String toString() {
		if(value==null)
			return key.toString();
		else
		return String.format("%s[%s]", key,value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MediaTag){
			MediaTag o = (MediaTag) obj;
			return Objects.equal(key, o.key) && Objects.equal(value, o.value);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key,value);
	}

	public String getValue() {
		return value;
	}

	public MediaTagKey getKey() {
		return key;
	}

}
