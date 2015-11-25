package hu.rxd.filebot;

import hu.rxd.filebot.MediaSection.ISection;

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
