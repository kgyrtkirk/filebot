package hu.rxd.filebot.normalization;

import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey2;

public class PrefixRemoval implements INormalization {

	private MediaTag tag;
	private String suffix;
	private MediaTagKey2<String> tag2;



	public PrefixRemoval(MediaTag tag, String suffix) {
		this.tag = tag;
		this.suffix = suffix;
	}

	public PrefixRemoval(MediaTagKey2<String> tag	, String prefix) {
		tag2 = tag;
		suffix = prefix;
	}

	@Override
	public String apply(String c) {
		assert(c.startsWith(suffix));
		return c.substring(suffix.length());
	}

}
