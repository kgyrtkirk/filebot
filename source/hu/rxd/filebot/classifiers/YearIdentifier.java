package hu.rxd.filebot.classifiers;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.rxd.filebot.VisitName;
import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

@VisitName(label = "cl_yearident")
public class YearIdentifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		String n = node.getName();
		// FIXME: be more conservative!!!!
		Pattern pat = compile("((19|20)[0-9]{2}).*", CASE_INSENSITIVE | UNICODE_CHARACTER_CLASS);
		
		Matcher m = pat.matcher(n);
		if(m.find()){
			node.addTag(MediaTag.year,Integer.parseInt(m.group(1)));
			node.addNormalization(new SuffixRemoval(MediaTag.year, m.group(0)));
		}
		if(node.getParent().hasTag(MediaTag.year)){
			int pt = node.getParent().getTag(MediaTag.year);
			if(node.hasTag(MediaTag.year)){
				int nt = node.getTag(MediaTag.year);
				if(pt!=nt){
					System.out.println(node);
					System.out.println(node.getParent());
					System.err.println("invalid: parent year/entry year mismatch parent:"+pt+" node:"+nt);
					
//					throw new RuntimeException("invalid: parent year/entry year mismatch parent:"+pt+" node:"+nt);
				}
			}else{
				node.addTag(MediaTag.year,pt);
			}
		}

	}

}
