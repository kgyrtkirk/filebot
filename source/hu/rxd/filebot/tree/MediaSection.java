package hu.rxd.filebot.tree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.media.jfxmedia.Media;

import hu.rxd.filebot.normalization.INormalization;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;

public class MediaSection {

	public static interface ISection {

		String getName();
		File getAbsoluteFile();
		String getOriginalName();

		@Deprecated
		void addTag(MediaTag tag);
		
		public <T>void addTag1(MediaTagKey2<T> key,T value);
		public <T>T getTag(MediaTagKey2<T> key);

		java.util.Collection<ISection> getChildren();

		ISection getSubsection(String name);

		ISection getEntry(String name);

		ISection getParent();

		@Deprecated
		MediaTag getTagByName(String string);
		@Deprecated
		boolean hasTag(MediaTag junk);
		
		boolean hasTag(MediaTagKey junk);
		public <T>boolean hasTag1(MediaTagKey2<T> key);

		void addNormalization(INormalization suffixRemoval);
		MediaTag getTag(MediaTagKey series);
		void addSearchKey(MediaTagKey2<?> tag, String head);
		java.util.Collection<String> getSearchKeys(MediaTagKey2<?> series);
		Path getPath();
		
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
		protected String path;
		protected Collection parent;
		private Map<String,ISection> children=new HashMap<>();
		private Map<MediaTagKey,MediaTag> tags=new HashMap<>();
		private Map<MediaTagKey2,Object> tags2=new HashMap<>();
		private List<INormalization> normalizations=new ArrayList<>();
		private String normalizedName;
		private Map<MediaTagKey2<?>, List<String>> searchKeys =new HashMap<>();
		

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
				section.addTag1(MediaTagKey.dir,true);
			}
			return section;
		}

		public ISection getEntry(String name) {
			
			ISection section=children.get(name);
			if(section==null){
				children.put(name, section = new Collection(this,name));
				section.addTag1(MediaTagKey.entry,true);
			}
			return section;
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
				c = generalTrimmer(c);
			}
			normalizedName=c;
			
		}
		private String generalTrimmer(String c) {
			c=c.replaceAll("^[.\\-_ ]+", "");
			c=c.replaceAll("[.\\-_ ]+$", "");
			return c;
		}
		@Override
		public MediaTag getTagByName(String string) {
			return (MediaTag)tags.get(MediaTagKey.valueOf(string));
		}
		@Override
		public boolean hasTag(MediaTagKey junk) {
			return tags.get(junk)!=null;
		}
		@Override
		public void addTag(MediaTag tag) {
			tags.put(tag.getKey(), tag);
		}
		
		@Override
		public <T>void addTag1(MediaTagKey2<T> key,T value){
			tags2.put(key, value);
		}
		@Override
		public <T>T getTag(MediaTagKey2<T> key){
			return (T)tags2.get(key);
		}
		@Override
		public <T>boolean hasTag1(MediaTagKey2<T> key){
			return tags2.containsKey(key);
		}
		
		@Override
		public MediaTag getTag(MediaTagKey key) {
			return (MediaTag) tags.get(key);
		}
		@Override
		public void addSearchKey(MediaTagKey2<?> tag, String key) {
			List<String> li = searchKeys.get(tag);
			if(li==null){
				searchKeys.put(tag,li=new ArrayList<>());
			}
			li.add(generalTrimmer(key));
		}
		@Override
		public java.util.Collection<String> getSearchKeys(MediaTagKey2<?> tag) {
			List<String> li = searchKeys.get(tag);
			if(li==null){
				return new ArrayList<>();
			}
			return li;
		}
		@Override
		public Path getPath() {
			File f=getAbsoluteFile();
			return f.toPath();
		}
		@Override
		public File getAbsoluteFile() {
			return new File(getParent().getAbsoluteFile(),getOriginalName());
		}
	}
	public static class Root extends Collection{

		public Root(String path) {
			super(null,path);
			super.addTag1(MediaTagKey.dir,true);
			super.addTag(new MediaTag(MediaTagKey.isRoot));
			parent=this;
		}
		@Override
		public void addTag(MediaTag tag) {
			System.out.println("ignoring tag on root: "+tag);
		}
		public File getAbsoluteFile() {
			return new File(path);
		}
	}

}
