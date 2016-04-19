package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.VisitName;
import hu.rxd.filebot.normalization.PrefixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

@VisitName(label = "cl_releaseprefix")
public class ReleasePrefixClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) {
		ISection parent = node.getParent();
		String[] pp = parent.getName().split("-");
		String[] np = node.getName().split("-");
		if(pp.length>1 && np.length>1){
			String pCand = pp[pp.length-1];
			String nCand = np[0];
//			MediaTag tag = new MediaTag(MediaTagKey.releasePrefix,nCand);
			if(pCand.equalsIgnoreCase(nCand)){
				node.addTag(MediaTag.releasePrefix,nCand);
				node.addNormalization(new PrefixRemoval(MediaTag.releasePrefix, nCand));
				return;
			}
			// support: qwe-TheAsd/ta-qwe.avi
			pCand=pCand.replaceAll("[a-z]", "");
			if(pCand.equalsIgnoreCase(nCand)) {
				node.addTag(MediaTag.releasePrefix,nCand);
				node.addNormalization(new PrefixRemoval(MediaTag.releasePrefix, nCand));
				return;
			}
		}
	}


}
