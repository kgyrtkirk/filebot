package hu.rxd.filebot;

import java.io.File;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class FileTreeWalker implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		File f = node.getAbsoluteFile();
		if (f.exists()) {
			if (f.isDirectory()) {
				for (File child : f.listFiles()) {
					if (child.isDirectory()) {
						node.getSubsection(child.getName());
					} else {
						node.getEntry(child.getName());
					}
				}
			}
		}else{
			node.addTag1(MediaTagKey.removed,true);
		}
	}
}
