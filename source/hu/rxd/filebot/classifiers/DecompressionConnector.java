package hu.rxd.filebot.classifiers;

import java.io.File;
import java.util.List;

import com.google.common.collect.Sets;

import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.MediaCollection;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.cli.CmdlineOperations;
import net.filebot.cli.ConflictAction;

public class DecompressionConnector implements ISectionVisitor {

	
	@Override
	public void visit(ISection node) throws Exception {
		
		CmdlineOperations cmdline = new CmdlineOperations();
		List<File> ef = cmdline.extract(Sets.newHashSet(node.getAbsoluteFile()),
				"/tmp/e1/"+node.getName(),
				ConflictAction.AUTO.toString(),
				null,
				false);
		
		for (File file : ef) {
			node.getParent().getShadowEntry(file);
		}
		System.out.println(ef);
//		MediaCollection c = (MediaCollection)node.getParent();
		
//		c.getEntry(name);
		
	}

}
