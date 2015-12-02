package hu.rxd.filebot;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class YearIdentifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		String n = node.getName();
		// FIXME: be more conservative!!!!
		Pattern pat = compile("((19|20)[0-9]{2}).*", CASE_INSENSITIVE | UNICODE_CHARACTER_CLASS);
		
		Matcher m = pat.matcher(n);
		if(m.find()){
			node.addTag1(MediaTag.year,Integer.parseInt(m.group(1)));
			node.addNormalization(new SuffixRemoval(MediaTag.year, m.group(0)));
		}
		if(node.getParent().hasTag1(MediaTag.year)){
			int pt = node.getParent().getTag(MediaTag.year);
			if(node.hasTag1(MediaTag.year)){
				int nt = node.getTag(MediaTag.year);
				if(pt!=nt){
					throw new RuntimeException("invalid: parent year/entry year mismatch");
				}
			}else{
				node.addTag1(MediaTag.year,pt);
			}
		}

	}

}
