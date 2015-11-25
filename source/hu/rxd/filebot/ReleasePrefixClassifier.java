package hu.rxd.filebot;

import hu.rxd.filebot.MediaSection.ISection;

public class ReleasePrefixClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) {
		ISection parent = node.getParent();
		String[] pp = parent.getName().split("-");
		String[] np = node.getName().split("-");
		if(pp.length>1 && np.length>1){
			String pCand = pp[pp.length-1];
			String nCand = np[0];
			MediaTag tag = new MediaTag("releasePrefix",nCand);
			if(pCand.equalsIgnoreCase(nCand)){
				node.tag(tag);
				node.addNormalization(new PrefixRemoval(tag, nCand));
				return;
			}
			pCand=pCand.replaceAll("[a-z]", "");
			if(pCand.equalsIgnoreCase(nCand)) {
				node.tag(tag);
				node.addNormalization(new PrefixRemoval(tag, nCand));
				return;
			}
		}
	}


}
