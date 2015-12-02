package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.normalization.PrefixRemoval;
import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class JunkClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) {
		if(isJunk(node)){
			node.addTag1(MediaTagKey.isJunk,true);
		}
	}

	private boolean isJunk(ISection node) {
		String n = node.getName();
		if (n.startsWith("sample-") ){
			node.addNormalization(new PrefixRemoval(MediaTagKey.isJunk, "sample-"));
			return true;
		}
		if (n.endsWith("-sample") ){
			node.addNormalization(new SuffixRemoval(MediaTagKey.isJunk, "-sample"));
			return true;
		}
		if (node.getName().equalsIgnoreCase("sample") ){
			return true;
		}
		if(node.getParent().hasTag1(MediaTagKey.isJunk)){
			return true;
		}
		return false;
	}

}
