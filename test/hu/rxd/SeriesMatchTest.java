package hu.rxd;

import java.util.List;

import org.junit.Test;

import hu.rxd.filebot.SeriesMatch;
import net.filebot.media.SmartSeasonEpisodeMatcher;
import net.filebot.similarity.SeasonEpisodeMatcher;
import net.filebot.similarity.SeasonEpisodeMatcher.SxE;

public class SeriesMatchTest {

	@Test
	public void asd() throws Exception{
		System.out.println("asd");
//		SeriesMatch sm = new SeriesMatch("Law.and.Order.C.I.S10E01");
//		System.out.println(sm.getR().getLenientName());
//		System.out.println(sm.getDistance());
		
		SmartSeasonEpisodeMatcher sem = new SmartSeasonEpisodeMatcher(SeasonEpisodeMatcher.DEFAULT_SANITY, false);
		List<SxE> a = sem.match("Law.and.Order.C.I.S10E01");
//		 String name = "blindspot.901";
		 String name = "5-15";
		a = sem.match(name);
			System.out.println(sem.head(name));
			System.out.println(a);
			System.out.println(a.get(0).season);
			System.out.println(a.get(0).episode);
//		SeriesMatchTest
	}
	
}
