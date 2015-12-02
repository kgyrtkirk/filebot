package hu.rxd.filebot.tree;

import net.filebot.web.Movie;

public enum MediaTagKey {

	dir,
	entry,
	isVideo,
	isNfo,
	isJunk,
	isSubtitle,
	isArchive,
	misc,
	
//	releasePrefix,
	
	series,
//	season,
//	episode,
	
	canBeSeries,
	
	seriesOutput, 
	extension,
	canBeMovie,
	movie, 
	
	removed, isRoot, 
	movieOutput,
	part,
	isSeries
	;
	public	static final MediaTagKey2<Movie>	movieObj =new MediaTagKey2<>();
	public	static final MediaTagKey2<String>	releasePrefix =new MediaTagKey2<>();
	public	static final MediaTagKey2<Integer>	episode =new MediaTagKey2<>();
	public	static final MediaTagKey2<Integer>	season =new MediaTagKey2<>();
	public	static final MediaTagKey2<Integer>	year =new MediaTagKey2<>();
}
