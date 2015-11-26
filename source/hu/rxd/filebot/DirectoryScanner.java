package hu.rxd.filebot;

import java.io.File;

import hu.rxd.filebot.classifiers.ExtensionClassifier;
import hu.rxd.filebot.classifiers.JunkClassifier;
import hu.rxd.filebot.classifiers.MiscDataClassifier;
import hu.rxd.filebot.classifiers.ReleasePrefixClassifier;
import hu.rxd.filebot.classifiers.SeriesMatcher;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.BasicVisitorRunner;
import net.filebot.media.SmartSeasonEpisodeMatcher;
import net.filebot.similarity.SeasonEpisodeMatcher;

public class DirectoryScanner {

	private File rootDir;

	public DirectoryScanner(File rootDir, File dir) throws Exception {
		this.rootDir = rootDir;
		MediaSection.Root root = new MediaSection.Root(dir.getPath());
		rwalk(dir, root);

//		runClassifier(root, new ExtensionClassifier());
		new BasicVisitorRunner(new ExtensionClassifier()).run(root);;
		new BasicVisitorRunner(new JunkClassifier()).run(root);;
		new BasicVisitorRunner(new ReleasePrefixClassifier()).run(root);;
		// subtitle
		new BasicVisitorRunner(new MiscDataClassifier()).run(root);;
		
//		new BasicVisitorRunner(new SeasonEpisodeClassifier()).run(root);;

		
//		new BasicVisitorRunner(new MiscDataClassifier()).run(root);;
		new BasicVisitorRunner(new SeriesMatcher())
			.having(TypeTags.VIDEO)
			.exclude(TypeTags.JUNK)
			.run(root);;
		
		
//		new ExtensionClassifier().run(root);
//		new JunkClassifier().run(root);
		
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
