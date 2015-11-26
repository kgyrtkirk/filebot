package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.SeriesMatch;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class SeriesMatcher implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		if(node.hasTag(TypeTags.JUNK))
			return;
		if(!node.hasTag(TypeTags.VIDEO))
			return;
		SeriesMatch sm = new SeriesMatch(node.getName());
		if(!sm.isMatch()){
			System.out.println(sm.getDistance());
			System.out.println(node);
		}
	}

}
