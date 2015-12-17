package hu.rxd.filebot;

import java.util.regex.Pattern;

import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagType;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.format.ExpressionFormat;
import net.filebot.format.MediaBindingBean;
import net.filebot.web.Episode;
import net.filebot.web.SearchResult;

public class NameBindingVisitor<T> implements ISectionVisitor {

	final MediaTagType<String> outputTag;
	private MediaTagType<T> mediaInfoTag;
	private String formatString;
	
	public NameBindingVisitor(MediaTagType<T> mediaInfoTag, MediaTagType<String> outputTag,String formatString) {
		this.mediaInfoTag = mediaInfoTag;
		this.outputTag=outputTag;
		this.formatString = formatString;
	}
	
	@Override
	public void visit(ISection node) throws Exception {
		
		T s = node.getTag(mediaInfoTag);
		MediaBindingBean mbb = new MediaBindingBean(s,null,null);
		ExpressionFormat	ef=new ExpressionFormat(formatString);
		String a = ef.format(mbb);
		a+="."+node.getTag(MediaTag.extension);
		Pattern ILLEGAL_CHARACTERS = Pattern.compile("[\\\\:*?\"<>|\\r\\n]|[ ]+$|(?<=[^.])[.]+$|(?<=.{250})(.+)(?=[.]\\p{Alnum}{3}$)");
		a=ILLEGAL_CHARACTERS.matcher(a).replaceAll("").replaceAll("\\s+", " ").trim();
		node.addTag(outputTag,a);

	}

}
