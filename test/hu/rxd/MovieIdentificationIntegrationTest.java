package hu.rxd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.rxd.filebot.ScanBot;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTagKey;

import static hu.rxd.ClassificationIntegrationTests.*;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class MovieIdentificationIntegrationTest {

	static String[] cases = { 
			"d1/movies/Flicka 2..avi",
			"d1/movies/Szabadeses.2014.RETAiL.DVDRip.x264.HuN-No1/szabadeses.dvdrip.x264-no1.mkv",
			"She's.Funny.That.Way.2014.BDRip.x264.HuN-HRT/hrt-shes.funny.that.way.2014.bdrip.x264.mkv", };
//disabled..only imdbid can help	"d1/movies/Flicka.3.DVDRip.x264.HUN-Baggio1/Flicka.3.DVDRip.x264.HUN-Baggio1.mkv",

	@Parameters(name = "{0}")
	public static Iterable<Object[]> getParameters() {

		List<Object[]> list = new ArrayList<>();
		for (String c : cases) {
			File f = new File(c);
			list.add(new Object[] { f.getName(), c.split("/") });
		}

		return list;

	}

	private Root root;
	
	public MovieIdentificationIntegrationTest(String label,String[]parts) {
		root=basicSections(parts);
	}
	
	@Test
	public void testIsMovie() throws Exception{
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertTrue(leaf.hasTag(MediaTagKey.movieOutput));
		System.out.println(leaf.getTag(MediaTagKey.movieOutput));
	}

}
