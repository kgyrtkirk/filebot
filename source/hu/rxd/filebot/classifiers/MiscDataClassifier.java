package hu.rxd.filebot.classifiers;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class MiscDataClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		
		
		Pattern pat = compile("(REPACK|DVDRip|BDRIP|HDTV|hdtv|720p|1080p|x264|Hdtv).*", CASE_INSENSITIVE | UNICODE_CHARACTER_CLASS);

//		String startPatterns[]={"DVDRip","BDRIP","HDTV","hdtv","720p","720p","x264","Hdtv"};
		String n = node.getName();
		int	start=-1;
		Matcher matcher = pat.matcher(n);
		if(matcher.find()){
//			String suffix=n.substring(start);
			node.addNormalization(new SuffixRemoval(MediaTag.misc, matcher.group(0)));
		}
//		for (String pat : startPatterns) {
//			int i = (n.indexOf(pat));
//			if(i>=0){
//				if(start == -1 || start > i){
//					start=i;
//				}
//			}
//		}
//		if(start>=0){
//		}
//		System.out.println(node);

	}

}
