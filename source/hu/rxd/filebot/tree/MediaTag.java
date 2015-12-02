package hu.rxd.filebot.tree;

import java.util.Set;

import net.filebot.web.Movie;

/* interface to prevent instantiation */
public interface MediaTag {

	// FIXME: looks like its mere existence is to prioritize searchKeys...f
	@Deprecated
	public static final MediaTagType<Boolean> canBeSeries = new MediaTagType<>();
	public static final MediaTagType<Boolean> canBeMovie = new MediaTagType<>();

	public static final MediaTagType<Boolean> isVideo = new MediaTagType<>();
	public static final MediaTagType<Boolean> isNfo = new MediaTagType<>();
	public static final MediaTagType<Boolean> isJunk = new MediaTagType<>();
	public static final MediaTagType<Boolean> isSubtitle = new MediaTagType<>();
	public static final MediaTagType<Boolean> isArchive = new MediaTagType<>();
	public static final MediaTagType<Boolean> misc = new MediaTagType<>();

	public static final MediaTagType<Boolean> entry = new MediaTagType<>();
	public static final MediaTagType<Boolean> dir = new MediaTagType<>();

	public static final MediaTagType<String> extension = new MediaTagType<>();
	public static final MediaTagType<String> releasePrefix = new MediaTagType<>();

	public static final MediaTagType<Integer> year = new MediaTagType<>();

	public static final MediaTagType<Movie> movieObj = new MediaTagType<>();

	public static final MediaTagType<String> series = new MediaTagType<>();
	public static final MediaTagType<Integer> episode = new MediaTagType<>();
	public static final MediaTagType<Integer> season = new MediaTagType<>();

	public static final MediaTagType<String> seriesOutput = new MediaTagType<>();
	public static final MediaTagType<String> movie = new MediaTagType<>();
	public static final MediaTagType<String> movieOutput = new MediaTagType<>();

	public static final MediaTagType<String> part = new MediaTagType<>();

	public static final MediaTagType<Boolean> removed = new MediaTagType<>();
	public static final MediaTagType<Boolean> isRoot = new MediaTagType<>();
	public static final MediaTagType<Boolean> isSeries = new MediaTagType<>();

	public static final MediaTagType<Set<Integer>> imdbId = new MediaTagType<>();
	public static final MediaTagType<Set<Integer>> tvdbId = new MediaTagType<>();

}
