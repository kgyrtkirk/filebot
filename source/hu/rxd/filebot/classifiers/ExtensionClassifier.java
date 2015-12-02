package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey2;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.MediaTypes;

public class ExtensionClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) {
		if(MediaTypes.VIDEO_FILES.accept(node.getName())){
			removeExt(node, MediaTag.isVideo);
			return;
		}
		if(MediaTypes.SUBTITLE_FILES.accept(node.getName())){
			removeExt(node, MediaTag.isSubtitle);
			return;
		}
		if(MediaTypes.ARCHIVE_FILES.accept(node.getName())){
			removeExt(node, MediaTag.isArchive);
			return;
		}
		if(MediaTypes.NFO_FILES.accept(node.getName())){
			removeExt(node, MediaTag.isNfo);
			return;
		}
	}

	private void removeExt(ISection node, MediaTagKey2<Boolean> isvideo) {
		node.addTag1(isvideo,true);
		String ext = node.getName().substring(node.getName().lastIndexOf(".")+1);
		node.addTag1(MediaTag.extension,ext);
		node.addNormalization(new SuffixRemoval(isvideo,ext));
	}

}
