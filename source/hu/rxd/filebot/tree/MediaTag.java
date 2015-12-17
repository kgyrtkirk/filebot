package hu.rxd.filebot.tree;

import java.util.Set;

import net.filebot.web.Episode;
import net.filebot.web.Movie;
import net.filebot.web.SearchResult;

/* interface to prevent instantiation */
public interface MediaTag {

	// FIXME: looks like its mere existence is to prioritize searchKeys...f
	@Deprecated
	public static final MediaTagType<Boolean> canBeSeries = new MediaTagType<>("canBeSeries");
	public static final MediaTagType<Boolean> canBeMovie = new MediaTagType<>("canBeMovie");

	public static final MediaTagType<Boolean> isVideo = new MediaTagType<>("isVideo");
	public static final MediaTagType<Boolean> isNfo = new MediaTagType<>("isNfo");
	public static final MediaTagType<Boolean> isJunk = new MediaTagType<>("isJunk");
	public static final MediaTagType<Boolean> isSubtitle = new MediaTagType<>("isSubtitle");
	public static final MediaTagType<Boolean> isArchive = new MediaTagType<>("isArchive");
	public static final MediaTagType<Boolean> misc = new MediaTagType<>("misc");

	public static final MediaTagType<Boolean> entry = new MediaTagType<>("entry");
	public static final MediaTagType<Boolean> dir = new MediaTagType<>("dir");

	public static final MediaTagType<String> extension = new MediaTagType<>("extension");
	public static final MediaTagType<String> releasePrefix = new MediaTagType<>("releasePrefix");

	public static final MediaTagType<Integer> year = new MediaTagType<>("year");

	public static final MediaTagType<Movie> movieObj = new MediaTagType<>("movieObj");

	public static final MediaTagType<String> series = new MediaTagType<>("series");
	public static final MediaTagType<Integer> episode = new MediaTagType<>("episode");
	public static final MediaTagType<Integer> season = new MediaTagType<>("season");

	public static final MediaTagType<String> seriesOutput = new MediaTagType<>("seriesOutput");
	public static final MediaTagType<String> movie = new MediaTagType<>("movie");
	public static final MediaTagType<String> movieOutput = new MediaTagType<>("movieOutput");

	public static final MediaTagType<String> part = new MediaTagType<>("part");

	public static final MediaTagType<Boolean> removed = new MediaTagType<>("removed");
	public static final MediaTagType<Boolean> isRoot = new MediaTagType<>("isRoot");
	public static final MediaTagType<Boolean> isSeries = new MediaTagType<>("isSeries");

	public static final MediaTagType<Set<Integer>> imdbId = new MediaTagType<>("imdbId");
	public static final MediaTagType<Set<Integer>> tvdbId = new MediaTagType<>("tvdbId");
	public static final MediaTagType<Episode> episodeObj = new MediaTagType<>("episodeObj");

}
