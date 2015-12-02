package hu.rxd.filebot.visitor;

import hu.rxd.filebot.tree.MediaSection.ISection;

public interface ISectionVisitor {

	void visit(ISection node) throws Exception;

}
