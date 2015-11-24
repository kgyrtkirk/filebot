package hu.rxd.filebot;

import java.io.File;

import net.sf.ehcache.CacheManager;

public class ScanBot {
	
	public static void main(String...args) throws Exception{
		addEHCacheShutdownHook();
		DirectoryScanner ds = new DirectoryScanner(new File("d1/series"),new File("d1/series/Minority.Report.S01E03.HDTV.x264-FLEET"));
		
	}

	private static void addEHCacheShutdownHook() {
		// make sure to orderly shutdown cache
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				try {
					CacheManager.getInstance().shutdown();
				} catch (Exception e) {
					// ignore, shutting down anyway
				}
			}
		});
	}

}
