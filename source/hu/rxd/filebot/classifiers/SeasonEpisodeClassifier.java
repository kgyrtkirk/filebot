package hu.rxd.filebot.classifiers;

import java.util.List;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaTag;
import hu.rxd.filebot.tree.MediaTagKey;
import hu.rxd.filebot.tree.TypeTags;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.media.SmartSeasonEpisodeMatcher;
import net.filebot.similarity.SeasonEpisodeMatcher;
import net.filebot.similarity.SeasonEpisodeMatcher.SxE;

public class SeasonEpisodeClassifier implements ISectionVisitor {

	private SmartSeasonEpisodeMatcher sem;
	public SeasonEpisodeClassifier() {
		sem = new SmartSeasonEpisodeMatcher(SeasonEpisodeMatcher.DEFAULT_SANITY, false);
	}
	@Override
	public void visit(ISection node) throws Exception {
		String name=node.getName().replaceAll("(19[0-9]{2}|2[01][0-9]{2})", "");
		List<SxE> a = sem.match(name);
		if(a!=null && a.size() > 0){
			SxE f = a.get(0);
			if(f.episode>=0){
			node.addTag(new MediaTag(MediaTagKey.episode, String.valueOf(f.episode)));
			}
			if(f.season>=0){
			node.addTag(new MediaTag(MediaTagKey.season, String.valueOf(f.season)));
			}
			String head = sem.head(name);
			if(head!=null) {
				node.addSearchKey(MediaTagKey.series,head );
			}
		}

	}

}
