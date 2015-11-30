package hu.rxd.filebot.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;

public class BasicVisitorRunner {

	private ISectionVisitor classifier;
	private Collection<MediaTagKey> neededTags = new ArrayList<>();
	private Collection<MediaTagKey> excludedTags = new ArrayList<>();

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
		for (MediaTagKey mediaTag : excludedTags) {
			if (node.hasTag(mediaTag)) {
				return false;
			}
		}
		for (MediaTagKey mediaTag : neededTags) {
			if (!node.hasTag(mediaTag)) {
				return false;
			}
		}
		return true;
	}

	public BasicVisitorRunner having(MediaTagKey tag) {
		neededTags.add(tag);
		return this;
	}

	public BasicVisitorRunner exclude(MediaTagKey tag) {
		excludedTags.add(tag);
		return this;
	}
	
	@Deprecated
	public BasicVisitorRunner having(MediaTag tag) {
		neededTags.add(tag.getKey());
		return this;
	}

	@Deprecated
	public BasicVisitorRunner exclude(MediaTag tag) {
		excludedTags.add(tag.getKey());
		return this;
	}

}
