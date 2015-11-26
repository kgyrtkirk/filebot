package hu.rxd;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.rxd.filebot.DirectoryScanner;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTagKey;

//@RunWith(Parameterized.class)
public class ClassificationIntegrationTests {

	// @Parameters(name="{0}")
	// public static Iterable<Object[]> getParameters(){
	//
	// List<Object[]> list=new ArrayList<>();
	// list.add(new Object[]{basicSections("The Good Wife S05","20 - The Deep
	// Web.mkv")});
	// return list;
	//
	// }
	
	public static MediaSection.Root basicSections(String... strings) {
		Root r = new MediaSection.Root("/");
		ISection s = r;
		for (int i = 0; i < strings.length - 1; i++) {
			s = s.getSubsection(strings[i]);
		}
		s.getEntry(strings[strings.length - 1]);
		return r;
	}

	// public ClassificationIntegrationTests(String a,String b){
	// System.out.println(a);
	// }

	@Test
	public void goodWife() throws Exception {
		Root root = basicSections("The Good Wife S05", "20 - The Deep Web.mkv");
		DirectoryScanner.tagDecorator1(root);
		ISection leaf = getLeaf(root);
		assertEquals("20",leaf.getTag(MediaTagKey.episode).getValue());
		assertEquals("5",leaf.getTag(MediaTagKey.season).getValue());
		assertEquals("The Good Wife",leaf.getTag(MediaTagKey.series).getValue());
//		assertEquals("20",leaf.getTagByName("series").getValue());

	}

	private ISection getLeaf(Root root) {
		ISection curr = root;
		while (curr.getChildren().size() > 0) {
			curr = curr.getChildren().iterator().next();
		}
		return curr;
	}
}
