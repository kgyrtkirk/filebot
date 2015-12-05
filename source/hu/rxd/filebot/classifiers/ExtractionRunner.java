package hu.rxd.filebot.classifiers;

import java.io.File;
import java.util.List;

import com.google.common.collect.Sets;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.cli.CmdlineOperations;
import net.filebot.cli.ConflictAction;

public class ExtractionRunner implements ISectionVisitor {
	
	private File extractionRoot;
	public ExtractionRunner(File extractionRoot) {
		this.extractionRoot = extractionRoot;
	}
	
	@Override
	public void visit(ISection node) throws Exception {
		
		CmdlineOperations cmdline = new CmdlineOperations();
		List<File> ef = cmdline.extract(Sets.newHashSet(node.getAbsoluteFile()),
				new File(extractionRoot,node.getName()).getAbsolutePath(),
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
