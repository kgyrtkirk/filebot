package hu.rxd.filebot.tree;

public class TypeTags {

	public static final MediaTag VIDEO = new MediaTag(MediaTagKey.isVideo);
//	public static final MediaTag DIRECTORY = new MediaTag(MediaTagKey.dir);
//	public static final MediaTag ENTRY = new MediaTag(MediaTagKey.entry);
	public static final MediaTag NFO = new MediaTag(MediaTagKey.isNfo);
	public static final MediaTag JUNK = new MediaTag(MediaTagKey.isJunk);
	public static final MediaTag SUBTITLE = new MediaTag(MediaTagKey.isSubtitle);
	public static final MediaTag ARCHIVE = new MediaTag(MediaTagKey.isArchive);
	public static final MediaTag MISC = new MediaTag(MediaTagKey.misc);
	public static final MediaTag PART = new MediaTag(MediaTagKey.part);

}
