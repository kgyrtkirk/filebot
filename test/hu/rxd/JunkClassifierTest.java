package hu.rxd;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.rxd.filebot.classifiers.JunkClassifier;
import hu.rxd.filebot.normalization.INormalization;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagType;
import hu.rxd.filebot.tree.SearchKey;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;

@RunWith(Parameterized.class)
public class JunkClassifierTest {
	
    private boolean expectation;
	private String str;

	@Parameters
    public static Collection<Object[]> data() {
    	List<Object[]> ret=new ArrayList<>();
    	ret.add(new Object[] { "sample", true} );
    	ret.add(new Object[] { "minta", true} );
    	ret.add(new Object[] { "!sample", true} );
    	ret.add(new Object[] { "asdsajddfjk.sample", true} );
    	ret.add(new Object[] { "jklds fdf jkhle", false} );
        return ret;
    }
	
    public JunkClassifierTest(String str,boolean expectation) {
		this.str = str;
		this.expectation = expectation;
	}
	
	@Test
	public void testJunkness() throws Exception {
		JunkClassifier jc = new JunkClassifier();
		ISection s = ClassificationIntegrationTests.basicSections(str).getChildren().iterator().next();
		jc.visit(s);
		assertEquals(expectation, s.hasTag(MediaTag.isJunk));
	}

}
