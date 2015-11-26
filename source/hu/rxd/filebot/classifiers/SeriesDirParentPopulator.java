package hu.rxd.filebot.classifiers;

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

	}

}
