package hu.rxd.filebot;

import java.lang.annotation.Annotation;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTagType;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class VisitOncePolicy implements ISectionVisitor {

	private ISectionVisitor classifier;
	private MediaTagType<String> visitTag;

	public VisitOncePolicy(ISectionVisitor classifier) {
		this.classifier = classifier;
		String label = getClassifierLabel(classifier.getClass());
		visitTag=new MediaTagType<String>(label);
	}

	@Override
	public void visit(ISection node) throws Exception {
		if(node.hasTag(visitTag))
			return;
		classifier.visit(node);
		node.addTag(visitTag, null);
	}

	private String getClassifierLabel(Class<? extends ISectionVisitor> cl) {
		Annotation[] a = cl.getAnnotations();
		for (Annotation annotation : a) {
			if(annotation instanceof VisitName){
				VisitName visitName = (VisitName) annotation;
				String l = visitName.label();
				if(l==null || l.length()<1 ){
					throw new RuntimeException("invalid annotation label on " + cl);
				}
				return l;
			}
		}
		throw new RuntimeException("no annotation label on " + cl);
	}

}
