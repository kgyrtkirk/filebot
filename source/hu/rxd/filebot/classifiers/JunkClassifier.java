package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.normalization.PrefixRemoval;
import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class JunkClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) {
		if(isJunk(node)){
			node.tag(TypeTags.JUNK);
		}
//			|| child.getParentFile().getName().equalsIgnoreCase("Sample")) {
//		System.out.println("skip:" + child);
//		
		
	}

	private boolean isJunk(ISection node) {
		String n = node.getName();
		if (n.startsWith("sample-") ){
			node.addNormalization(new PrefixRemoval(TypeTags.JUNK, "sample-"));
			return true;
		}
		if (n.endsWith("-sample") ){
			node.addNormalization(new SuffixRemoval(TypeTags.JUNK, "-sample"));
			return true;
		}
		if (node.getName().equalsIgnoreCase("sample") ){
			return true;
		}
		if(node.getParent().hasTag(TypeTags.JUNK)){
			return true;
		}
		return false;
	}

}
