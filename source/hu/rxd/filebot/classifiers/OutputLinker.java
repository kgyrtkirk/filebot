package hu.rxd.filebot.classifiers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTagType;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class OutputLinker implements ISectionVisitor {

	private String outputDir;
	private MediaTagType<String> tag;

	public OutputLinker(String outputDir, MediaTagType<String> seriesoutput) {
		this.outputDir = outputDir;
		this.tag = seriesoutput;
	}

	@Override
	public void visit(ISection node) throws Exception {
		String targetRelativeName = node.getTag(tag);
		
		Path sourceFile = node.getPath();
		Path targetFile = new File(outputDir+"/"+targetRelativeName).toPath();
		if(Files.exists(targetFile) || Files.isSymbolicLink(targetFile)){
			if(!Files.isSymbolicLink(targetFile)){
				throw new RuntimeException("target exists and not a symlink");
			}
			Path target = Files.readSymbolicLink(targetFile);
			if(target.equals(sourceFile)){
				// already points to the right target
				return;
			}
			System.err.println("WWW link exists..but points to elsewhere..exception for now: \nlink: "
			+ targetFile +"\nexpected:"+sourceFile+"\nactual:"+target);
//			throw new RuntimeException("link exists..but points to elsewhere..exception for now: \nlink:" + sourceFile +"\nexpected:"+targetFile+"\nactual:"+target);
		}
		// create parent directories
		Files.createDirectories(targetFile.getParent());
		Files.createSymbolicLink(targetFile, sourceFile);
		System.out.println(" * linked: " + targetFile +" => "+sourceFile);
	}

}
