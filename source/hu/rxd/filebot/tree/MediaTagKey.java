package hu.rxd.filebot.tree;

import net.filebot.web.Movie;

public enum MediaTagKey {

//	dir,
//	entry,
//	isVideo,
//	isNfo,
//	isJunk,
//	isSubtitle,
//	isArchive,
//	misc,
	
//	releasePrefix,
	
//	series,
//	season,
//	episode,
	
	
	seriesOutput, 
//	extension,
	canBeMovie,
	movie, 
	
	removed, isRoot, 
	movieOutput,
	part,
	isSeries
	;
	
	// FIXME: looks like its mere existence is to prioritize searchKeys...f
	@Deprecated
	public	static final MediaTagKey2<Boolean>		canBeSeries =new MediaTagKey2<>();
	
	public	static final MediaTagKey2<Boolean>	isVideo =new MediaTagKey2<>();
	public	static final MediaTagKey2<Boolean>	isNfo =new MediaTagKey2<>();
	public	static final MediaTagKey2<Boolean>	isJunk =new MediaTagKey2<>();
	public	static final MediaTagKey2<Boolean>	isSubtitle =new MediaTagKey2<>();
	public	static final MediaTagKey2<Boolean>	isArchive =new MediaTagKey2<>();
	public	static final MediaTagKey2<Boolean>	misc =new MediaTagKey2<>();

	public	static final MediaTagKey2<Boolean>	entry =new MediaTagKey2<>();
	public	static final MediaTagKey2<Boolean>	dir =new MediaTagKey2<>();

	public	static final MediaTagKey2<String>	extension =new MediaTagKey2<>();
	public	static final MediaTagKey2<String>	releasePrefix =new MediaTagKey2<>();
	
	public	static final MediaTagKey2<Integer>	year =new MediaTagKey2<>();
	
	public	static final MediaTagKey2<Movie>	movieObj =new MediaTagKey2<>();
	
	public	static final MediaTagKey2<String>	series =new MediaTagKey2<>();
	public	static final MediaTagKey2<Integer>	episode =new MediaTagKey2<>();
	public	static final MediaTagKey2<Integer>	season =new MediaTagKey2<>();
}
