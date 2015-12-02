package hu.rxd.filebot.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTagKey2;

public class BasicVisitorRunner {

	private ISectionVisitor classifier;
	private Collection<MediaTagKey2<?>> neededTags2= new ArrayList<>();
	private Collection<MediaTagKey2<?>> excludedTags2= new ArrayList<>();

	public BasicVisitorRunner(ISectionVisitor junkClassifier) {
		classifier = junkClassifier;
	}

	public void run(Root root) throws Exception {
		Queue<ISection> queue = new LinkedList<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			ISection node = queue.poll();
			if (accepted(node)) {
				classifier.visit(node);
			}
			queue.addAll(node.getChildren());
		}

	}

	private boolean accepted(ISection node) {
		for (MediaTagKey2<?> mediaTag : excludedTags2) {
			if (node.hasTag1(mediaTag)) {
				return false;
			}
		}
		for (MediaTagKey2<?> mediaTag : neededTags2) {
			if (!node.hasTag1(mediaTag)) {
				return false;
			}
		}
		return true;
	}

	public BasicVisitorRunner having(MediaTagKey2<?> tag) {
		neededTags2.add(tag);
		return this;
	}

	public BasicVisitorRunner exclude(MediaTagKey2<?> tag) {
		excludedTags2.add(tag);
		return this;
	}

}
