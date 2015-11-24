package hu.rxd.filebot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryScanner {

	private File rootDir;

	public DirectoryScanner(File rootDir, File dir) throws Exception {
		this.rootDir = rootDir;
		Files.walk(dir.toPath()).filter(Files::isRegularFile).forEach(f -> checkFile(f));
	}

	private Object checkFile(Path p) {
		File f = p.toFile();
		String key = getKeyFor(f);
		System.out.println(key);
		return key;

	}

	private String getKeyFor(File f) {
		return rootDir.toURI().relativize(f.toURI()).getPath();
	}

}
