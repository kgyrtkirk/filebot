package hu.rxd.filebot.normalization;

import hu.rxd.filebot.tree.MediaTagKey2;

public class PrefixRemoval implements INormalization {

	private String suffix;
	private MediaTagKey2<?> tag2;

	public PrefixRemoval(MediaTagKey2<?> tag	, String prefix) {
		tag2 = tag;
		suffix = prefix;
	}

	@Override
	public String apply(String c) {
		assert(c.startsWith(suffix));
		return c.substring(suffix.length());
	}

}
