package hu.rxd.filebot.normalization;

import hu.rxd.filebot.tree.MediaTagType;

public class PrefixRemoval implements INormalization {

	private String suffix;
	private MediaTagType<?> tag2;

	public PrefixRemoval(MediaTagType<?> tag	, String prefix) {
		tag2 = tag;
		suffix = prefix;
	}

	@Override
	public String apply(String c) {
		assert(c.startsWith(suffix));
		return c.substring(suffix.length());
	}

}
