package hu.rxd.filebot.classifiers;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.normalization.SuffixRemoval;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class SeriesDirClassifier implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		Pattern pats[]={
		// season folder pattern for complementing partial sxe info from filename
				compile("S[-._ ]?(\\d{1,2})$", /*CASE_INSENSITIVE | */UNICODE_CHARACTER_CLASS),
		compile("Season[-._ ]?(\\d{1,2})$", CASE_INSENSITIVE | UNICODE_CHARACTER_CLASS),
		};

		String name = node.getName();
		for (Pattern pattern : pats) {
			Matcher m = pattern.matcher(name);
			if(m.find()){
				String matchedSeasonPart = m.group(0);
				String seasonStr = m.group(1).replaceFirst("0+", "");
				node.addTag1(MediaTagKey.season,Integer.valueOf(seasonStr));
				node.addTag1(MediaTagKey.canBeSeries,true);

				node.addNormalization(new SuffixRemoval(MediaTagKey.season, matchedSeasonPart));
//				System.out.println(node);
				return;
			}
		}
	}

}
