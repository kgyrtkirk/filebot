package hu.rxd.filebot.classifiers;

import java.util.Collection;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class SeriesDirParentPopulator implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		if(node.getParent().hasTag1(MediaTag.season)){
			node.addTag1(MediaTag.season,node.getParent().getTag(MediaTag.season));
		}
		if(node.getParent().hasTag1(MediaTag.series)){
			node.addTag1(MediaTag.series,node.getParent().getTag(MediaTag.series));
		}
		if(node.getParent().hasTag1(MediaTag.isSeries)){
			node.addTag1(MediaTag.isSeries,node.getParent().getTag(MediaTag.isSeries));
		}
		
		Collection<String> parentSearchKeys = node.getParent().getSearchKeys(MediaTag.series);
		for (String string : parentSearchKeys) {
			node.addSearchKey(MediaTag.series, string);
		}

	}

}
