package hu.rxd.filebot;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Set;

import com.google.common.io.Files;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.media.MediaDetection;

public class NfoReader implements ISectionVisitor {

	@Override
	public void visit(ISection node) throws Exception {
		File f = node.getAbsoluteFile();
		String content = Files.toString(f, Charset.forName("UTF-8"));
		Set<Integer> imdbIds = MediaDetection.grepImdbId(content);
		Set<Integer> tvdbIds = MediaDetection.grepTheTvdbId(content);
		
		if(imdbIds.size()>1){
			throw new RuntimeException("not expected");
		}
		if(tvdbIds.size()>1){
			throw new RuntimeException("not expected");
		}
		if(imdbIds.size()>0){
			
//			node.addTag(new MediaTag(MediaTagKey.imdbId,imdbIds));
		}
		

	}

}
