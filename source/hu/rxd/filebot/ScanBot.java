package hu.rxd.filebot;

import java.io.File;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import hu.rxd.filebot.classifiers.SeriesOutputLinker;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.visitor.BasicVisitorRunner;
import net.sf.ehcache.CacheManager;

public class ScanBot {
	
	public static void main(String...args) throws Exception{
//		addEHCacheShutdownHook();
		ScanBot bot = new ScanBot();
		
		CmdLineParser parser = new CmdLineParser(bot);
		try{
			parser.parseArgument(args);
			System.out.println(bot.srcDirs);
			System.out.println(bot.seriesOutputDir);
			bot.run();
//			DirectoryScanner ds = new DirectoryScanner(new File("d1/series"),new File("d1/series/Minority.Report.S01E03.HDTV.x264-FLEET"));
//		DirectoryScanner ds2 = new DirectoryScanner(new File("d1/series"),new File("d1/series/Minority.Report.S01E03.HDTV.x264-FLEET//Minority.Report.S01E03.HDTV.x264-FLEET.mp4"));
		}catch(CmdLineException e){
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
		}finally{
		CacheManager.getInstance().shutdown();
		}
	}
	
    
    @Option(name="-src",usage="source directories",required=true)
    public List<String> srcDirs;
    
    @Option(name="-so",aliases={"--series-output"},required=true,usage="series output directory")
    public String seriesOutputDir;

    @Option(name="-sp",aliases={"--series-pattern"},usage="series pattern directory")
    public String seriesPattern = "{n}/{s00e00}.{t}";
    
	private void run() throws Exception {
		for (String srcDir : srcDirs) {
			DirectoryScanner ds = new DirectoryScanner(new File(srcDir), new File(srcDir));
			Root root = ds.getRoot();
			
			new BasicVisitorRunner(new SeriesOutputLinker(seriesOutputDir))
				.having(new MediaTag(MediaTagKey.seriesOutput))
				.having(new MediaTag(MediaTagKey.isVideo))
				.having(new MediaTag(MediaTagKey.entry))
				.exclude(TypeTags.JUNK)
				.run(root);;

		}
	}


}
