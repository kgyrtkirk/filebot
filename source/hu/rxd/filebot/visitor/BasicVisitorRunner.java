package hu.rxd.filebot.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTag;

public class BasicVisitorRunner {

	private ISectionVisitor classifier;
	private Collection<MediaTag> neededTags = new ArrayList<>();
	private Collection<MediaTag> excludedTags = new ArrayList<>();

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
		for (MediaTag mediaTag : excludedTags) {
			if (node.hasTag(mediaTag)) {
				return false;
			}
		}
		for (MediaTag mediaTag : neededTags) {
			if (!node.hasTag(mediaTag)) {
				return false;
			}
		}
		return true;
	}

	public BasicVisitorRunner having(MediaTag tag) {
		neededTags.add(tag);
		return this;
	}

	public BasicVisitorRunner exclude(MediaTag tag) {
		excludedTags.add(tag);
		return this;
	}

}
