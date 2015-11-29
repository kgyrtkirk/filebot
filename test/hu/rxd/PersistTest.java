package hu.rxd;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import hu.rxd.filebot.tree.MediaSection.Root;

public class PersistTest {

	@Test
	public void t1() {

		Root root = ClassificationIntegrationTests.basicSections("d1", "movies",
				"Daddy.or.Mommy.2015.PROPER.RETAiL.DVDRip.x264.HuN-No1", "dom.dvdrip.x264-no1.mkv");
		Map<String, Object> writerOpts = new HashMap<>();
		writerOpts.put("PRETTY_PRINT", "true");
		String w = JsonWriter.objectToJson(root, writerOpts);
//		System.out.println(w);
//		System.out.println(w.length());
		Root reRead = (Root) JsonReader.jsonToJava(w);

//		assertEquals(root, reRead);
	}

}
