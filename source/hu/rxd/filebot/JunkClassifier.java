package hu.rxd.filebot;

import hu.rxd.filebot.MediaSection.ISection;
import hu.rxd.filebot.MediaSection.Root;

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
		if (node.hasTag(TypeTags.DIRECTORY) && node.getName().equalsIgnoreCase("sample") ){
			return true;
		}
		if(node.getParent().hasTag(TypeTags.JUNK)){
			return true;
		}
		return false;
	}

}
