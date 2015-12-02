package hu.rxd.filebot.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTagType;

public class BasicVisitorRunner {

	private ISectionVisitor classifier;
	private Collection<MediaTagType<?>> neededTags2= new ArrayList<>();
	private Collection<MediaTagType<?>> excludedTags2= new ArrayList<>();

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
		for (MediaTagType<?> mediaTag : excludedTags2) {
			if (node.hasTag1(mediaTag)) {
				return false;
			}
		}
		for (MediaTagType<?> mediaTag : neededTags2) {
			if (!node.hasTag1(mediaTag)) {
				return false;
			}
		}
		return true;
	}

	public BasicVisitorRunner having(MediaTagType<?> tag) {
		neededTags2.add(tag);
		return this;
	}

	public BasicVisitorRunner exclude(MediaTagType<?> tag) {
		excludedTags2.add(tag);
		return this;
	}

}
