package hu.rxd.filebot;

public class SuffixRemoval  implements INormalization{

	private MediaTag tag;
	private String suffix;

	public SuffixRemoval(MediaTag tag, String suffix) {
		this.tag = tag;
		this.suffix = suffix;
		
	}

	@Override
	public String apply(String c) {
		assert(c.endsWith(suffix));
		return c.substring(0, c.length()-suffix.length());
	}

}
