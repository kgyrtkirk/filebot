package hu.rxd.filebot;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import hu.rxd.filebot.MediaSection.ISection;
import hu.rxd.filebot.MediaSection.Root;
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
		node.tag(tag);
		String ext = node.getName().substring(node.getName().lastIndexOf("."));
		node.addNormalization(new SuffixRemoval(tag,ext));
	}

}
