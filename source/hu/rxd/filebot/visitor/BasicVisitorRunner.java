package hu.rxd.filebot.visitor;

import java.util.ArrayList;
import java.util.Collection;

import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import net.filebot.MediaTypes;

public class BasicVisitorRunner {

	private ISectionVisitor classifier;
	private Collection<MediaTag> neededTags = new ArrayList<>();
	private Collection<MediaTag> excludedTags = new ArrayList<>();

	public BasicVisitorRunner(ISectionVisitor junkClassifier) {
		classifier = junkClassifier;
	}

	public void run(Root root) throws Exception {
		visit(root);
	}

	private void visit(ISection node) throws Exception {
		if (accepted(node)) {
			classifier.visit(node);
		}
		Collection<ISection> c = node.getChildren();
		for (ISection ic : c) {
			visit(ic);
		}
	}

	private boolean accepted(ISection node) {
		for (MediaTag mediaTag : excludedTags) {
			if(node.hasTag(mediaTag)){
				return false;
			}
		}
		for (MediaTag mediaTag : neededTags) {
			if(!node.hasTag(mediaTag)){
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
