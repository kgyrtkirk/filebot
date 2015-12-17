package hu.rxd.filebot.tree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.rxd.filebot.normalization.INormalization;
import net.sf.ehcache.search.expression.InCollection;

public class MediaSection {

	public static interface ISection {

		/**
		 * returns the current name of the file (depends on applied normalizations)
		 * 
		 * @return
		 */
		String getName();
		/**
		 * returns an absolute file
		 * @return
		 */
		File getAbsoluteFile();
		/**
		 * returns the original filename
		 * @return
		 */
		String getOriginalName();
		/**
		 * adds the tag with its value
		 * 
		 * @param key
		 * @param value
		 */
		public <T>void addTag(MediaTagType<T> key,T value);
		/**
		 * returns the value for the given key
		 * 
		 * @param key
		 * @return
		 */
		public <T>T getTag(MediaTagType<T> key);

		java.util.Collection<ISection> getChildren();

		ISection getSubsection(String name);

		ISection getEntry(String name);

		ISection getParent();

		
		public <T>boolean hasTag(MediaTagType<T> key);

		void addNormalization(INormalization suffixRemoval);
		void addSearchKey(MediaTagType<?> tag, float weight, String head);
		java.util.Collection<String> getSearchKeys(MediaTagType<?> series);
		Path getPath();
		ISection getShadowEntry(File file);
		
	}

	public static class  MediaCollection implements ISection{
		protected String path;
		protected MediaCollection parent;
		private Map<String,ISection> children=new HashMap<>();
		private Map<MediaTagType<?>,Object> tags2=new HashMap<>();
		private List<INormalization> normalizations=new ArrayList<>();
		private String normalizedName;
		private Map<MediaTagType<?>, List<String>> searchKeys =new HashMap<>();
		

		MediaCollection(MediaCollection parent, String name) {
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
				children.put(name, section = new MediaCollection(this,name));
				section.addTag(MediaTag.dir,true);
			}
			return section;
		}

		public ISection getEntry(String name) {
			
			ISection section=children.get(name);
			if(section==null){
				children.put(name, section = new MediaCollection(this,name));
				section.addTag(MediaTag.entry,true);
			}
			return section;
		}
		@Override
		public ISection getShadowEntry(File file) {
			String name = file.getName();
			String key = "shadow::"+name;
			ISection section=children.get(key);
			if(section==null){
				children.put(key, section = new ShadowMediaCollection(this,name,file));
				section.addTag(MediaTag.entry,true);
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
		public String toString() {
			return String.format("%s ; %s; tags: %s", normalizedName,path,tags2);
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
		public <T>void addTag(MediaTagType<T> key,T value){
			tags2.put(key, value);
		}
		@Override
		public <T>T getTag(MediaTagType<T> key){
			return (T)tags2.get(key);
		}
		@Override
		public <T>boolean hasTag(MediaTagType<T> key){
			return tags2.containsKey(key);
		}
		
		@Override
		public void addSearchKey(MediaTagType<?> tag, float weight,String key) {
			List<String> li = searchKeys.get(tag);
			if(li==null){
				searchKeys.put(tag,li=new ArrayList<>());
			}
			li.add(generalTrimmer(key));
		}
		@Override
		public java.util.Collection<String> getSearchKeys(MediaTagType<?> tag) {
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
	public static class Root extends MediaCollection{

		public Root(String path) {
			super(null,path);
			super.addTag(MediaTag.dir,true);
			super.addTag(MediaTag.isRoot,true);
			parent=this;
		}
		public File getAbsoluteFile() {
			return new File(path);
		}
	}

	public static class ShadowMediaCollection extends MediaCollection{

		private File f;
		public ShadowMediaCollection(MediaCollection parent,String name,File f) {
			super(parent,name);
			this.f = f;
		}
		public File getAbsoluteFile() {
			return f;
		}
	}

}
