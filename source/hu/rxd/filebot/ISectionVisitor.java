package hu.rxd.filebot;

import hu.rxd.filebot.MediaSection.ISection;

public interface ISectionVisitor {

	void visit(ISection node) throws Exception;

}
