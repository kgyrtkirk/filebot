
package net.sourceforge.filebot;


import static net.sourceforge.filebot.Settings.*;

import net.sourceforge.filebot.web.AnidbClient;
import net.sourceforge.filebot.web.EpisodeListProvider;
import net.sourceforge.filebot.web.FanartTV;
import net.sourceforge.filebot.web.IMDbClient;
import net.sourceforge.filebot.web.MovieIdentificationService;
import net.sourceforge.filebot.web.OpenSubtitlesClient;
import net.sourceforge.filebot.web.SerienjunkiesClient;
import net.sourceforge.filebot.web.SubsceneSubtitleClient;
import net.sourceforge.filebot.web.SubtitleProvider;
import net.sourceforge.filebot.web.TMDbClient;
import net.sourceforge.filebot.web.TVRageClient;
import net.sourceforge.filebot.web.TheTVDBClient;
import net.sourceforge.filebot.web.VideoHashSubtitleService;


/**
 * Reuse the same web service client so login, cache, etc. can be shared.
 */
public final class WebServices {
	
	// episode dbs
	public static final TVRageClient TVRage = new TVRageClient();
	public static final AnidbClient AniDB = new AnidbClient(getApplicationName().toLowerCase(), 2);
	public static final TheTVDBClient TheTVDB = new TheTVDBClient(getApplicationProperty("thetvdb.apikey"));
	public static final SerienjunkiesClient Serienjunkies = new SerienjunkiesClient(getApplicationProperty("serienjunkies.apikey"));
	
	// movie dbs
	public static final IMDbClient IMDb = new IMDbClient();
	public static final TMDbClient TMDb = new TMDbClient(getApplicationProperty("themoviedb.apikey"));
	
	// subtitle dbs
	public static final OpenSubtitlesClient OpenSubtitles = new OpenSubtitlesClient(String.format("%s %s", getApplicationName(), getApplicationVersion()));
	public static final SubsceneSubtitleClient Subscene = new SubsceneSubtitleClient();
	
	// fanart.tv
	public static final FanartTV FanartTV = new FanartTV(Settings.getApplicationProperty("fanart.tv.apikey"));
	
	
	public static EpisodeListProvider[] getEpisodeListProviders() {
		return new EpisodeListProvider[] { TVRage, AniDB, TheTVDB, Serienjunkies };
	}
	
	
	public static MovieIdentificationService[] getMovieIdentificationServices() {
		return new MovieIdentificationService[] { OpenSubtitles, IMDb, TMDb };
	}
	
	
	public static SubtitleProvider[] getSubtitleProviders() {
		return new SubtitleProvider[] { OpenSubtitles, Subscene };
	}
	
	
	public static VideoHashSubtitleService[] getVideoHashSubtitleServices() {
		return new VideoHashSubtitleService[] { OpenSubtitles };
	}
	
	
	public static EpisodeListProvider getEpisodeListProvider(String name) {
		for (EpisodeListProvider it : WebServices.getEpisodeListProviders()) {
			if (it.getName().equalsIgnoreCase(name))
				return it;
		}
		
		return null; // default
	}
	
	
	public static MovieIdentificationService getMovieIdentificationService(String name) {
		for (MovieIdentificationService it : getMovieIdentificationServices()) {
			if (it.getName().equalsIgnoreCase(name))
				return it;
		}
		
		return null; // default
	}
	
	
	/**
	 * Dummy constructor to prevent instantiation.
	 */
	private WebServices() {
		throw new UnsupportedOperationException();
	}
	
}
