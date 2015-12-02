package hu.rxd.filebot;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class MultipartClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		String n = node.getName();
		// FIXME: be more conservative!!!!
		Pattern pat = compile("cd([1-9])$", CASE_INSENSITIVE | UNICODE_CHARACTER_CLASS);
		
		Matcher m = pat.matcher(n);
		if(m.find()){
			node.addTag1(MediaTagKey.part,m.group(0));
			node.addNormalization(new SuffixRemoval(MediaTagKey.part, m.group(0)));
		}

	}

}
