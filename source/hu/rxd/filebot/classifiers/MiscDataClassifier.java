package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class MiscDataClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		
		String startPatterns[]={"DVDRip","BDRIP","HDTV","hdtv","720p","720P","x264","Hdtv"};
		String n = node.getName();
		int	start=-1;
		for (String pat : startPatterns) {
			int i = (n.indexOf(pat));
			if(i>=0){
				if(start == -1 || start > i){
					start=i;
				}
			}
		}
		if(start>=0){
			String suffix=n.substring(start);
			node.addNormalization(new SuffixRemoval(TypeTags.MISC, suffix));
		}
		System.out.println(node);

	}

}
