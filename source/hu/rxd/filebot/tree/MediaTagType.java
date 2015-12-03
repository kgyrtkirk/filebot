package hu.rxd.filebot.tree;

import com.google.common.base.Objects;

public class MediaTagType<T> {

	private final String label;

	public MediaTagType(String label) {
		this.label = label;
	}
	
	@Override
	public int hashCode() {
		return label.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj instanceof MediaTagType<?>){
			return Objects.equal(((MediaTagType) obj).label, label);
		}
		return super.equals(obj);
	}
}
