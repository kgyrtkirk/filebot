package hu.rxd.filebot.normalization;

import hu.rxd.filebot.tree.MediaTagType;

public class SuffixRemoval  implements INormalization{

	private String suffix;
	private MediaTagType<?> tag2;

	public SuffixRemoval(MediaTagType<?> tag, String suffix) {
		tag2 = tag;
		this.suffix = suffix;
	}

	@Override
	public String apply(String c) {
		assert(c.endsWith(suffix));
		return c.substring(0, c.length()-suffix.length());
	}

}
