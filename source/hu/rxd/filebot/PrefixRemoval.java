package hu.rxd.filebot;

public class PrefixRemoval implements INormalization {

	private MediaTag tag;
	private String suffix;



	public PrefixRemoval(MediaTag tag, String suffix) {
		this.tag = tag;
		this.suffix = suffix;
	}

	@Override
	public String apply(String c) {
		assert(c.startsWith(suffix));
		return c.substring(suffix.length());
	}

}
