package hu.rxd.filebot;

import java.io.File;

import hu.rxd.filebot.classifiers.ExtensionClassifier;
import hu.rxd.filebot.classifiers.JunkClassifier;
import hu.rxd.filebot.classifiers.MiscDataClassifier;
import hu.rxd.filebot.classifiers.ReleasePrefixClassifier;
import hu.rxd.filebot.classifiers.SeasonEpisodeClassifier;
import hu.rxd.filebot.classifiers.SeriesDirClassifier;
import hu.rxd.filebot.classifiers.SeriesDirParentPopulator;
import hu.rxd.filebot.classifiers.SeriesIdentifactor;
import hu.rxd.filebot.classifiers.SeriesMatcher;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.visitor.BasicVisitorRunner;
import net.filebot.WebServices;
import net.filebot.WebServices.TheTVDBClientWithLocalSearch;

public class DirectoryScanner {

	private File rootDir;
	private MediaSection.Root root;

	public DirectoryScanner(File rootDir, File dir) throws Exception {
		this.rootDir = rootDir;
		root = new MediaSection.Root(dir.getPath());
		rwalk(dir, root);

		tagDecorator1(root);
		
		
//		new ExtensionClassifier().run(root);
//		new JunkClassifier().run(root);
		
	}

	public MediaSection.Root getRoot() {
		return root;
	}

	public static void tagDecorator1(MediaSection.Root root) throws Exception {
		//		runClassifier(root, new ExtensionClassifier());
				new BasicVisitorRunner(new ExtensionClassifier()).run(root);
				new BasicVisitorRunner(new JunkClassifier()).run(root);
				new BasicVisitorRunner(new ReleasePrefixClassifier()).run(root);
				// subtitle
				
				new BasicVisitorRunner(new MiscDataClassifier()).run(root);
				
				new BasicVisitorRunner(new YearIdentifier()).run(root);
				
				new BasicVisitorRunner(new SeasonEpisodeClassifier()).run(root);

				new BasicVisitorRunner(new SeriesDirClassifier())
					.having(TypeTags.DIRECTORY)
					.run(root);

				new BasicVisitorRunner(new SeriesDirParentPopulator())
					.having(TypeTags.ENTRY)
					.run(root);
		
				
				new BasicVisitorRunner(new MovieMatcher())
					.having(TypeTags.VIDEO)
					.exclude(TypeTags.JUNK)
					.run(root);
				
		//		new BasicVisitorRunner(new MiscDataClassifier()).run(root);;
				new BasicVisitorRunner(new SeriesMatcher())
					.having(TypeTags.VIDEO)
					.exclude(TypeTags.JUNK)
					.run(root);
				
				
				new BasicVisitorRunner(new SeriesIdentifactor())
				.having(new MediaTag(MediaTagKey.canBeSeries))
				.having(new MediaTag(MediaTagKey.season))
				.having(new MediaTag(MediaTagKey.episode))
				.having(new MediaTag(MediaTagKey.entry))
					.exclude(TypeTags.JUNK)
					.run(root);
				
				System.out.println("positive:");
				new BasicVisitorRunner(new PrintThem())
				.having(new MediaTag(MediaTagKey.canBeMovie))
					.run(root);;
				
//				new BasicVisitorRunner(new PrintThem())
//				.exclude(new MediaTag(MediaTagKey.seriesOutput))
//				.having(new MediaTag(MediaTagKey.isVideo))
//				.having(new MediaTag(MediaTagKey.entry))
//					.exclude(TypeTags.JUNK)
//					.run(root);;

	}


	private void rwalk(File dir, ISection iSection) throws Exception {
		File[] children = dir.listFiles();
		for (File child : children) {
			if (child.isDirectory()) {
				rwalk(child, iSection.getSubsection(child.getName()));
			} else {
				ISection entry = iSection.getEntry(child.getName());
				
//				ExtensionFileFilter ff = MediaTypes.VIDEO_FILES;
//				if (ff.accept(child)) {
//					if (child.getName().startsWith("sample-")
//							|| child.getParentFile().getName().equalsIgnoreCase("Sample")) {
//						System.out.println("skip:" + child);
//					} else {
//						checkFile(child);
//					}
//				}
			}
		}
	}

	private Object checkFile(File child) throws Exception {
		File f = child;
		String key = getKeyFor(f);
		// System.out.println(key);

		SeriesMatch sm = new SeriesMatch(f.getName());
		if (!sm.isMatch()) {
			System.out.println(f);
			System.out.println(f.getName());
			System.out.println(sm.getR());
			f = f.getParentFile();
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
