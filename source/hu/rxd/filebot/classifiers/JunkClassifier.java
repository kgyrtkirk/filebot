package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.normalization.PrefixRemoval;
import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class JunkClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) {
		if(isJunk(node)){
			node.addTag(MediaTag.isJunk,true);
		}
	}

	private boolean isJunk(ISection node) {
		String n = node.getName();
		if (n.startsWith("sample-") ){
			node.addNormalization(new PrefixRemoval(MediaTag.isJunk, "sample-"));
			return true;
		}
		if (n.endsWith("-sample") ){
			node.addNormalization(new SuffixRemoval(MediaTag.isJunk, "-sample"));
			return true;
		}
		if (node.getName().equalsIgnoreCase("sample") ){
			return true;
		}
		if(node.getParent().hasTag(MediaTag.isJunk)){
			return true;
		}
		return false;
	}

}
