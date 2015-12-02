package hu.rxd.filebot.normalization;

import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey2;

public class SuffixRemoval  implements INormalization{

	private MediaTag tag;
	private String suffix;
	private MediaTagKey2<Integer> tag2;

	public SuffixRemoval(MediaTag tag, String suffix) {
		this.tag = tag;
		this.suffix = suffix;
		
	}

	public SuffixRemoval(MediaTagKey2<Integer> tag, String suffix) {
		tag2 = tag;
		this.suffix = suffix;
	}

	@Override
	public String apply(String c) {
		assert(c.endsWith(suffix));
		return c.substring(0, c.length()-suffix.length());
	}

}
