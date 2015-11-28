package hu.rxd.filebot.classifiers;

import hu.rxd.filebot.tree.MediaTagKey;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.ISectionVisitor;

public class SeriesOutputLinker implements ISectionVisitor {

	private String seriesOutputDir;

	public SeriesOutputLinker(String seriesOutputDir) {
		this.seriesOutputDir = seriesOutputDir;
	}

	@Override
	public void visit(ISection node) throws Exception {
		String targetRelativeName = node.getTag(MediaTagKey.seriesOutput).getValue();
		
		Path sourceFile = node.getPath();
		Path targetFile = new File(seriesOutputDir+"/"+targetRelativeName).toPath();
		if(Files.exists(targetFile)){
			if(!Files.isSymbolicLink(targetFile)){
				throw new RuntimeException("target exists and not a symlink");
			}
			Path target = Files.readSymbolicLink(targetFile);
			if(target == sourceFile){
				// already points to the right target
				return;
			}
			throw new RuntimeException("link exists..but points to elsewhere..exception for now");
		}
		// create parent directories
		Files.createDirectories(targetFile.getParent());
		Files.createSymbolicLink(targetFile, sourceFile);
		System.out.println(" * linked: " + targetFile +" => "+sourceFile);
	}

}
