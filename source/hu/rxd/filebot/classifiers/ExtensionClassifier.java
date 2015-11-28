package hu.rxd.filebot.classifiers;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.MediaTypes;
import net.filebot.util.FileUtilities.ExtensionFileFilter;

public class ExtensionClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) {
		if(MediaTypes.VIDEO_FILES.accept(node.getName())){
			removeExt(node, TypeTags.VIDEO);
			return;
		}
		if(MediaTypes.SUBTITLE_FILES.accept(node.getName())){
			removeExt(node, TypeTags.SUBTITLE);
			return;
		}
		if(MediaTypes.ARCHIVE_FILES.accept(node.getName())){
			removeExt(node, TypeTags.ARCHIVE);
			return;
		}
		if(MediaTypes.NFO_FILES.accept(node.getName())){
			removeExt(node, TypeTags.NFO);
			return;
		}
	}

	private void removeExt(ISection node, MediaTag tag) {
		node.addTag(tag);
		String ext = node.getName().substring(node.getName().lastIndexOf(".")+1);
		node.addTag(new MediaTag(MediaTagKey.extension,ext));
		node.addNormalization(new SuffixRemoval(tag,ext));
	}

}
