package hu.rxd.filebot.classifiers;

import java.util.Collection;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class SeriesDirParentPopulator implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		if(node.getParent().hasTag(MediaTagKey.season)){
			node.addTag(node.getParent().getTag(MediaTagKey.season));
		}
		if(node.getParent().hasTag(MediaTagKey.series)){
			node.addTag(node.getParent().getTag(MediaTagKey.series));
		}
		
		Collection<String> parentSearchKeys = node.getParent().getSearchKeys(MediaTagKey.series);
		for (String string : parentSearchKeys) {
			node.addSearchKey(MediaTagKey.series, string);
		}

	}

}
