package hu.rxd.filebot.classifiers;

import java.util.Collection;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class SeriesDirByVote implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		Collection<ISection> ch = node.getChildren();
		int vCnt=0;
		for (ISection s : ch) {
			if(s.hasTag1(MediaTag.isVideo)){
				vCnt++;
			}
		}
		if(vCnt>3 || ch.size()>10){
			node.addTag(MediaTag.isSeries,true);
		}

	}

}
