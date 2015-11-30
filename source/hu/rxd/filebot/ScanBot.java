package hu.rxd.filebot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import hu.rxd.filebot.classifiers.ExtensionClassifier;
import hu.rxd.filebot.classifiers.JunkClassifier;
import hu.rxd.filebot.classifiers.MiscDataClassifier;
import hu.rxd.filebot.classifiers.MovieIdentifactor;
import hu.rxd.filebot.classifiers.ReleasePrefixClassifier;
import hu.rxd.filebot.classifiers.SeasonEpisodeClassifier;
import hu.rxd.filebot.classifiers.SeriesDirClassifier;
import hu.rxd.filebot.classifiers.SeriesDirParentPopulator;
import hu.rxd.filebot.classifiers.SeriesIdentifactor;
import hu.rxd.filebot.classifiers.SeriesMatcher;
import hu.rxd.filebot.classifiers.OutputLinker;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.tree.TypeTags;
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
	
    
    @Option(name="-src",usage="source directory",required=true)
    public String srcDir;

    // FIXME: enable this later
//    @Option(name="-sd",aliases={"--state-dir"},usage="state directory (defaults to be inside series-output)")
//    public String stateDir;
    
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
		stateFile = new File(src,STATEFILE_NAME);
		
		if(!src.exists() || !src.isDirectory()){
			throw new IllegalArgumentException(String.format("source directory: %s doesnt exists, or not a directory", srcDir));
		}
		
		Root root = getRoot();
		
		new BasicVisitorRunner(new FileTreeWalker()).run(root);
		
		
		
		new BasicVisitorRunner(new ExtensionClassifier()).run(root);
		new BasicVisitorRunner(new JunkClassifier()).run(root);
		new BasicVisitorRunner(new ReleasePrefixClassifier()).run(root);
		// subtitle
		
		new BasicVisitorRunner(new MiscDataClassifier()).run(root);
		
		new BasicVisitorRunner(new YearIdentifier()).run(root);

		new BasicVisitorRunner(new MultipartClassifier()).run(root);

//		new SubtreeVis
//		new BasicVisitorRunner(new DirTree()).run(root);
		
		
		new BasicVisitorRunner(new MovieMatcher())
		.having(TypeTags.VIDEO)
		.exclude(TypeTags.JUNK)
		.run(root);
		
		new BasicVisitorRunner(new SeasonEpisodeClassifier()).run(root);

		new BasicVisitorRunner(new SeriesDirClassifier())
			.having(TypeTags.DIRECTORY)
			.run(root);

		new BasicVisitorRunner(new SeriesDirParentPopulator())
			.having(TypeTags.ENTRY)
			.run(root);

		
		
//		new BasicVisitorRunner(new MiscDataClassifier()).run(root);;
		new BasicVisitorRunner(new SeriesMatcher())
			.having(TypeTags.VIDEO)
			.having(new MediaTag(MediaTagKey.episode))
			.exclude(TypeTags.JUNK)
			.run(root);
		
		new BasicVisitorRunner(new SeriesIdentifactor())
			.having(new MediaTag(MediaTagKey.canBeSeries))
			.having(new MediaTag(MediaTagKey.season))
			.having(new MediaTag(MediaTagKey.episode))
			.having(new MediaTag(MediaTagKey.entry))
			.exclude(new MediaTag(MediaTagKey.canBeMovie))
			.exclude(TypeTags.JUNK)
			.run(root);
		
		new BasicVisitorRunner(new MovieIdentifactor())
			.having(new MediaTag(MediaTagKey.canBeMovie))
			.having(new MediaTag(MediaTagKey.movie))
			.exclude(new MediaTag(MediaTagKey.canBeSeries))
			.exclude(TypeTags.JUNK)
			.run(root);

		System.out.println("series AND movie (undecided):");
		new BasicVisitorRunner(new PrintThem())
		.having(new MediaTag(MediaTagKey.canBeMovie))
		.having(new MediaTag(MediaTagKey.canBeSeries))
			.run(root);;

//		System.out.println("positive:");
//		new BasicVisitorRunner(new PrintThem())
//		.having(new MediaTag(MediaTagKey.canBeMovie))
//			.run(root);;
		
		
			
		new BasicVisitorRunner(new OutputLinker(seriesOutputDir,MediaTagKey.seriesOutput))
			.having(new MediaTag(MediaTagKey.seriesOutput))
			.having(new MediaTag(MediaTagKey.isVideo))
			.having(new MediaTag(MediaTagKey.entry))
			.exclude(TypeTags.JUNK)
			.run(root);
		new BasicVisitorRunner(new OutputLinker(movieOutputDir,MediaTagKey.movieOutput))
			.having(new MediaTag(MediaTagKey.movieOutput))
			.having(new MediaTag(MediaTagKey.isVideo))
			.having(new MediaTag(MediaTagKey.entry))
			.exclude(TypeTags.JUNK)
			.run(root);
		
		System.out.println("N/A");
		new BasicVisitorRunner(new PrintThem())
		.exclude(new MediaTag(MediaTagKey.movieOutput))
		.exclude(new MediaTag(MediaTagKey.seriesOutput))
		.having(new MediaTag(MediaTagKey.isVideo))
		.having(new MediaTag(MediaTagKey.entry))
			.exclude(TypeTags.JUNK)
			.run(root);;

			System.out.println("mov+");
			new BasicVisitorRunner(new PrintThem())
			.having(new MediaTag(MediaTagKey.movieOutput))
				.run(root);;
		
		saveState(root);

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
