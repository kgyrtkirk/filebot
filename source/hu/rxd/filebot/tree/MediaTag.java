package hu.rxd.filebot.tree;

import java.util.Set;

import net.filebot.web.Movie;

/* interface to prevent instantiation */
public interface MediaTag {

	// FIXME: looks like its mere existence is to prioritize searchKeys...f
	@Deprecated
	public static final MediaTagKey2<Boolean> canBeSeries = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> canBeMovie = new MediaTagKey2<>();

	public static final MediaTagKey2<Boolean> isVideo = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> isNfo = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> isJunk = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> isSubtitle = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> isArchive = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> misc = new MediaTagKey2<>();

	public static final MediaTagKey2<Boolean> entry = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> dir = new MediaTagKey2<>();

	public static final MediaTagKey2<String> extension = new MediaTagKey2<>();
	public static final MediaTagKey2<String> releasePrefix = new MediaTagKey2<>();

	public static final MediaTagKey2<Integer> year = new MediaTagKey2<>();

	public static final MediaTagKey2<Movie> movieObj = new MediaTagKey2<>();

	public static final MediaTagKey2<String> series = new MediaTagKey2<>();
	public static final MediaTagKey2<Integer> episode = new MediaTagKey2<>();
	public static final MediaTagKey2<Integer> season = new MediaTagKey2<>();

	public static final MediaTagKey2<String> seriesOutput = new MediaTagKey2<>();
	public static final MediaTagKey2<String> movie = new MediaTagKey2<>();
	public static final MediaTagKey2<String> movieOutput = new MediaTagKey2<>();

	public static final MediaTagKey2<String> part = new MediaTagKey2<>();

	public static final MediaTagKey2<Boolean> removed = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> isRoot = new MediaTagKey2<>();
	public static final MediaTagKey2<Boolean> isSeries = new MediaTagKey2<>();

	public static final MediaTagKey2<Set<Integer>> imdbId = new MediaTagKey2<>();
	public static final MediaTagKey2<Set<Integer>> tvdbId = new MediaTagKey2<>();

}
