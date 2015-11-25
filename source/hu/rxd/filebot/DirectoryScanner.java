package hu.rxd.filebot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import net.filebot.MediaTypes;
import net.filebot.cli.CmdlineOperations;
import net.filebot.similarity.SeriesNameMatcher;
import net.filebot.util.FileUtilities.ExtensionFileFilter;

public class DirectoryScanner {

	private File rootDir;

	public DirectoryScanner(File rootDir, File dir) throws Exception {
		this.rootDir = rootDir;
		rwalk(dir);
	    
//		Files.walk(dir.toPath()).filter(Files::isRegularFile).forEach(f -> checkFile(f));
	}

	private void rwalk(File dir) throws Exception {
	    File[] children = dir.listFiles();
	    for (File child : children) {
	    	if(child.isDirectory()){
	    		rwalk(child);
	    	}    	else{
	    		ExtensionFileFilter ff = MediaTypes.VIDEO_FILES;
	    		if(ff.accept(child)){
	    			if(child.getName().startsWith("sample-") || child.getParentFile().getName().equalsIgnoreCase("Sample")){
	    			System.out.println("skip:"+child);	
	    			}else{
	    			checkFile(child);
	    			}
	    		}
	    }}
	}

	private Object checkFile(File child) throws Exception {
		File f = child;
		String key = getKeyFor(f);
//		System.out.println(key);
		
		SeriesMatch sm = new SeriesMatch(f.getName());
		if(!sm.isMatch()){
			System.out.println(f);
			System.out.println(f.getName());
			System.out.println(sm.getR());
			f=f.getParentFile();
			sm = new SeriesMatch(f.getName());
			System.out.println(f);
			System.out.println(f.getName());
			System.out.println(sm.getR());
			sm = new SeriesMatch(f.getName());
		}
		return key;

	}

	private String getKeyFor(File f) {
		return rootDir.toURI().relativize(f.toURI()).getPath();
	}

}
