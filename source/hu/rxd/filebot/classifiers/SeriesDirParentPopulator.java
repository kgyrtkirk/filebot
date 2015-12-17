package hu.rxd.filebot.classifiers;

import java.util.Collection;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagType;
import hu.rxd.filebot.tree.SearchKey;
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
		
		for (SearchKey sk : node.getParent().getSearchKeys(MediaTag.series)) {
			node.addSearchKey(MediaTag.series, sk.getWeight()+0.1f,sk.getQueryStr());
		}
		
		if(node.hasTag(MediaTag.season) && node.hasTag(MediaTag.episode) ){
			node.addTag(MediaTag.isSeries, true);
		}

	}

}
