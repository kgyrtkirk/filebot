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
		if(node.getParent().hasTag(MediaTag.isJunk)){
			return true;
		}
		String n = node.getName().toLowerCase();
		if (n.matches("^sample[-_ .].*") ){
			node.addNormalization(new PrefixRemoval(MediaTag.isJunk, "sample-"));
			return true;
		}
		if (n.matches(".*[-_ .]sample$") ){
			node.addNormalization(new SuffixRemoval(MediaTag.isJunk, "-sample"));
			return true;
		}
		if (n.equalsIgnoreCase("sample") ){
			return true;
		}
		if (n.equalsIgnoreCase("minta") ){
			return true;
		}
		// sample1 ; !sample and friends.
		if(n.contains("sample") && n.length() <= 7 ){
			return true;
		}
		return false;
	}

}
