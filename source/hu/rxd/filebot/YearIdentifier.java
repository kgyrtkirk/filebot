package hu.rxd.filebot;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class YearIdentifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		String n = node.getName();
		// FIXME: be more conservative!!!!
		Pattern pat = compile("((19|20)[0-9]{2}).*", CASE_INSENSITIVE | UNICODE_CHARACTER_CLASS);
		
		Matcher m = pat.matcher(n);
		if(m.find()){
			node.addTag(new MediaTag(MediaTagKey.year,m.group(1)));
			node.addNormalization(new SuffixRemoval(TypeTags.MISC, m.group(0)));
		}
		if(node.getParent().hasTag(MediaTagKey.year)){
			MediaTag pt = node.getParent().getTag(MediaTagKey.year);
			if(node.hasTag(MediaTagKey.year)){
				boolean nt = node.hasTag(MediaTagKey.year);
				if(!pt.equals(nt)){
					throw new RuntimeException("invalid: parent year/entry year mismatch");
				}
			}else{
				node.addTag(pt);
			}
		}

	}

}
