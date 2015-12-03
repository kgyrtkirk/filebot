package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class PrintThem implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		System.out.println(node);
	}

}
