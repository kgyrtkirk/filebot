package hu.rxd.filebot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.cedarsoftware.util.io.JsonWriter;

import hu.rxd.filebot.classifiers.ExtractionRunner;
import hu.rxd.filebot.classifiers.ExtensionClassifier;
import hu.rxd.filebot.classifiers.JunkClassifier;
import hu.rxd.filebot.classifiers.MiscDataClassifier;
import hu.rxd.filebot.classifiers.MovieIdentifactor;
import hu.rxd.filebot.classifiers.MultipartClassifier;
import hu.rxd.filebot.classifiers.NfoReader;
import hu.rxd.filebot.classifiers.OutputLinker;
import hu.rxd.filebot.classifiers.PrintThem;
import hu.rxd.filebot.classifiers.ReleasePrefixClassifier;
import hu.rxd.filebot.classifiers.SeasonEpisodeClassifier;
import hu.rxd.filebot.classifiers.SeriesDirByVote;
import hu.rxd.filebot.classifiers.SeriesDirClassifier;
import hu.rxd.filebot.classifiers.SeriesDirParentPopulator;
import hu.rxd.filebot.classifiers.SeriesIdentifactor;
import hu.rxd.filebot.classifiers.SeriesMatcher;
import hu.rxd.filebot.classifiers.YearIdentifier;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.visitor.BasicVisitorRunner;
import net.sf.ehcache.CacheManager;

public class ScanBot {
	
	private static final String STATEFILE_NAME = ".mbstate.json";

	public static void main(String...args) throws Exception{
		ScanBot bot = new ScanBot();
		
		CmdLineParser parser = new CmdLineParser(bot);
		try{
			parser.parseArgument(args);
			System.out.println(bot.srcDir);
			System.out.println(bot.seriesOutputDir);
			bot.run();
		}catch(CmdLineException e){
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
		}finally{
		CacheManager.getInstance().shutdown();
		}
	}
	
    
    @Option(name="-src",usage="source directory; threated as read-only",required=true)
    public String srcDir;

    // FIXME: enable this later
    @Option(name="-sd",aliases={"--data-dir"},usage="data directory; will contain: application state info/caches and extracted archives",required=true)
    public String stateDir;
    
    @Option(name="-so",aliases={"--series-output"},required=true,usage="series output directory")
    public String seriesOutputDir;

    @Option(name="-mo",aliases={"--movie-output"},required=true,usage="movie output directory")
    public String movieOutputDir;

    @Option(name="-sp",aliases={"--series-pattern"},usage="series pattern directory")
    public String seriesPattern = "{n}/{s00e00}.{t}";

	private File src;

	private File stateFile;
    
	private void run() throws Exception {
		
		src = new File(srcDir);
		
		CacheBackplane.setDatabaseFile(new File(stateDir,".cache.db"));
		stateFile = new File(stateDir,STATEFILE_NAME);
		
		
		if(!src.exists() || !src.isDirectory()){
			throw new IllegalArgumentException(String.format("source directory: %s doesnt exists, or not a directory", srcDir));
		}
		
		Root root = getRoot();
		
		new BasicVisitorRunner(new FileTreeWalker()).run(root);
		
		
		
		runIdentification(root);

		System.out.println("series AND movie (undecided):");
		new BasicVisitorRunner(new PrintThem())
		.having((MediaTag.canBeMovie))
		.having((MediaTag.canBeSeries))
			.run(root);;

//		System.out.println("positive:");
//		new BasicVisitorRunner(new PrintThem())
//		.having(new MediaTag(MediaTagKey.canBeMovie))
//			.run(root);;
		
		
			
		new BasicVisitorRunner(new OutputLinker(seriesOutputDir,MediaTag.seriesOutput))
			.having((MediaTag.seriesOutput))
			.having((MediaTag.isVideo))
			.having((MediaTag.entry))
			.exclude(MediaTag.isJunk)
			.run(root);
		new BasicVisitorRunner(new OutputLinker(movieOutputDir,MediaTag.movieOutput))
			.having((MediaTag.movieOutput))
			.having((MediaTag.isVideo))
			.having((MediaTag.entry))
			.exclude(MediaTag.isJunk)
			.run(root);
		
		System.out.println("N/A");
		new BasicVisitorRunner(new PrintThem())
		.exclude((MediaTag.movieOutput))
		.exclude((MediaTag.seriesOutput))
		.having((MediaTag.isVideo))
		.having((MediaTag.entry))
			.exclude(MediaTag.isJunk)
			.run(root);;

//			System.out.println("mov+");
//			new BasicVisitorRunner(new PrintThem())
//			.having((MediaTagKey.movieOutput))
//				.run(root);;
		
		saveState(root);

	}


	public static void runIdentification(Root root) throws Exception {
		new BasicVisitorRunner(new ExtensionClassifier()).run(root);
		
		File dataDir=new File("/tmp/e1");
		new BasicVisitorRunner(new ExtractionRunner(dataDir))
		.having(MediaTag.isArchive)
			.run(root);
		// re-run extension classifier on decompressed
		new BasicVisitorRunner(new ExtensionClassifier()).run(root);
		
		new BasicVisitorRunner(new JunkClassifier()).run(root);
		new BasicVisitorRunner(new ReleasePrefixClassifier()).run(root);
		
		new BasicVisitorRunner(new MiscDataClassifier()).run(root);
		

		new BasicVisitorRunner(new MultipartClassifier()).run(root);

		new BasicVisitorRunner(new SeriesDirByVote())
		.exclude(MediaTag.isRoot)
		.having(MediaTag.dir)
			.run(root);
//		new SubtreeVis
//		new BasicVisitorRunner(new DirTree()).run(root);

		new BasicVisitorRunner(new SeriesDirClassifier())
			.having(MediaTag.dir)
			.run(root);
		
		new BasicVisitorRunner(new SeasonEpisodeClassifier()).run(root);
		
		new BasicVisitorRunner(new YearIdentifier()).run(root);
		
		new BasicVisitorRunner(new NfoReader())
			.having(MediaTag.isNfo)
			.having(MediaTag.entry)
			.run(root);
		

		new BasicVisitorRunner(new SeriesDirParentPopulator())
			.having(MediaTag.entry)
			.run(root);

		
		new BasicVisitorRunner(new MovieMatcher())
			.having(MediaTag.isVideo)
			.exclude(MediaTag.isJunk)
			.exclude(MediaTag.isSeries)
			.run(root);
		


		
		
//		new BasicVisitorRunner(new MiscDataClassifier()).run(root);;
		new BasicVisitorRunner(new SeriesMatcher())
			.having(MediaTag.isVideo)
			.having(MediaTag.episode)
			.exclude(MediaTag.isJunk)
			.run(root);
		
		new BasicVisitorRunner(new SeriesIdentifactor())
			.having((MediaTag.canBeSeries))
			.having((MediaTag.season))
			.having((MediaTag.episode))
			.having((MediaTag.entry))
			.exclude((MediaTag.canBeMovie))
			.exclude(MediaTag.isJunk)
			.run(root);
		
		new BasicVisitorRunner(new MovieIdentifactor(true))
		.having((MediaTag.canBeMovie))
		.having((MediaTag.movie))
		.exclude((MediaTag.canBeSeries))
		.exclude((MediaTag.isSeries))
		.exclude(MediaTag.isJunk)
		.run(root);

		new BasicVisitorRunner(new MovieIdentifactor(false))
		.having(MediaTag.isVideo)
		.exclude(MediaTag.movieOutput)
		.exclude((MediaTag.canBeSeries))
		.exclude((MediaTag.isSeries))
		.exclude(MediaTag.isJunk)
		.run(root);
	}


	private Root getRoot() throws Exception {
//		if(stateFile.exists()){
//			System.out.println("restoring state from: "+stateFile);
//			return (Root) JsonReader.jsonToJava(new FileInputStream(stateFile), new HashMap<>());
//		}
		Root root = new MediaSection.Root(src.getPath());
		return root;
		
	}

	private void saveState(Root root) throws IOException {
		Map<String, Object> exportOptions=new HashMap<>();
		exportOptions.put("PRETTY_PRINT", "true");
		
		FileWriter fos = new FileWriter(stateFile);
		fos.write(JsonWriter.objectToJson(root,exportOptions));
		fos.close();
	}

}
