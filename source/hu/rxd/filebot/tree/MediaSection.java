package hu.rxd.filebot.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.rxd.filebot.normalization.INormalization;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;

public class MediaSection {

	public static interface ISection {

		String getName();
		String getOriginalName();

		@Deprecated
		void tag(MediaTag tag);
		void addTag(MediaTag tag);

		java.util.Collection<ISection> getChildren();

		ISection getSubsection(String name);

		ISection getEntry(String name);

		ISection getParent();

		@Deprecated
		MediaTag getTagByName(String string);
		@Deprecated
		boolean hasTag(MediaTag junk);
		
		boolean hasTag(MediaTagKey junk);

		void addNormalization(INormalization suffixRemoval);
		MediaTag getTag(MediaTagKey series);
		
	}
//	public static class  Entry implements ISection{
//		private String path;
//		private Collection parent;
//
//		public Entry(Collection parent, String name) {
//			this.parent = parent;
//			path = name;
//		}
//
//		@Override
//		public String getName() {
//			return path;
//		}
//		
//	}

	public static class  Collection implements ISection{
		private String path;
		protected Collection parent;
		private Map<String,ISection> children=new HashMap<>();
		private Map<MediaTagKey,MediaTag> tags=new HashMap<>();
		private List<INormalization> normalizations=new ArrayList<>();
		private String normalizedName;
		

		Collection(Collection parent, String name) {
			this.parent = parent;
			path = name;
			renormalize();
		}
		@Override
		public String getName() {
			return normalizedName;
		}
		@Override
		public String getOriginalName() {
			return path;
		}

		public ISection getSubsection(String name) {
			ISection section=children.get(name);
			if(section==null){
				children.put(name, section = new Collection(this,name));
				section.tag(TypeTags.DIRECTORY);
			}
			return section;
		}

		public ISection getEntry(String name) {
			
			ISection section=children.get(name);
			if(section==null){
				children.put(name, section = new Collection(this,name));
				section.tag(TypeTags.ENTRY);
			}
			return section;
		}
		@Override
		public void tag(MediaTag tag) {
			tags.put(tag.getKey(), tag);
			
		}
		@Override
		public java.util.Collection<ISection> getChildren() {
			return children.values();
			
		}
		@Override
		public ISection getParent() {
			return parent;
		}
		@Override
		public boolean hasTag(MediaTag junk) {
			return tags.containsKey(junk.getKey());
		}

		@Override
		public String toString() {
			return String.format("%s ; %s; tags: %s", normalizedName,path,tags);
		}
		@Override
		public void addNormalization(INormalization suffixRemoval) {
			normalizations.add(suffixRemoval);
			renormalize();
		}
		private void renormalize() {
			String c=path;
			for (INormalization iNormalization : normalizations) {
				c=iNormalization.apply(c);
				c=c.replaceAll("^[.\\-_ ]+", "");
				c=c.replaceAll("[.\\-_ ]+$", "");
			}
			normalizedName=c;
			
		}
		@Override
		public MediaTag getTagByName(String string) {
			return tags.get(MediaTagKey.valueOf(string));
		}
		@Override
		public boolean hasTag(MediaTagKey junk) {
			return tags.get(junk)!=null;
		}
		@Override
		public void addTag(MediaTag tag) {
			tag(tag);
		}
		@Override
		public MediaTag getTag(MediaTagKey key) {
			return tags.get(key);
		}
	}
	public static class Root extends Collection{

		public Root(String path) {
			super(null,path);
			super.tag(TypeTags.DIRECTORY);
			parent=this;
		}
		@Override
		public void tag(MediaTag tag) {
			System.out.println("ignoring tag on root: "+tag);
		}
	}

}
