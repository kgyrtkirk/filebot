package hu.rxd.filebot.classifiers;

import java.util.Collection;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class SeriesDirParentPopulator implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		if(node.getParent().hasTag(MediaTag.season)){
			node.addTag(MediaTag.season,node.getParent().getTag(MediaTag.season));
		}
		if(node.getParent().hasTag(MediaTag.series)){
			node.addTag(MediaTag.series,node.getParent().getTag(MediaTag.series));
		}
		if(node.getParent().hasTag(MediaTag.isSeries)){
			node.addTag(MediaTag.isSeries,node.getParent().getTag(MediaTag.isSeries));
		}
		
		Collection<String> parentSearchKeys = node.getParent().getSearchKeys(MediaTag.series);
		for (String string : parentSearchKeys) {
			node.addSearchKey(MediaTag.series, 1.1f,string);
		}

	}

}
