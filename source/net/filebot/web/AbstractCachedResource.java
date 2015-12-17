package net.filebot.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import hu.rxd.filebot.CacheBackplane;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public abstract class AbstractCachedResource<R, T extends Serializable> {

	public static final long ONE_MINUTE = 60 * 1000;
	public static final long ONE_HOUR = 60 * ONE_MINUTE;
	public static final long ONE_DAY = 24 * ONE_HOUR;
	public static final long ONE_WEEK = 7 * ONE_DAY;
	public static final long ONE_MONTH = 30 * ONE_DAY;

	protected final String resource;
	protected final Class<T> type;
	protected final long expirationTime;

	protected final int retryCountLimit;
	protected final long retryWaitTime;

	public AbstractCachedResource(String resource, Class<T> type, long expirationTime, int retryCountLimit, long retryWaitTime) {
		this.resource = resource;
		this.type = type;
		this.expirationTime = expirationTime;
		this.retryCountLimit = retryCountLimit;
		this.retryWaitTime = retryWaitTime;
	}

	/**
	 * Convert resource data into usable data
	 */
	protected abstract R fetchData(URL url, long lastModified) throws IOException;

	protected abstract T process(R data) throws Exception;

	private static DB db;

	protected final DB getCache() {
		return CacheBackplane.getDB();
	}

	public static DB getDB() {
		if(db==null){
			db = DBMaker.newFileDB(new File("/tmp/a12.db")).closeOnJvmShutdown().make();
		}
		return db;
		
	}

	public synchronized T get() throws IOException {
		String cacheKey = type.getName() + ":" + resource.toString();
		T element = null;
		long lastUpdateTime = 0;

		DB db = getCache();
		String	cacheTableName=getClass().getName()+"_"+type.getName()+".2";
		Map<String,T> cache = db.createHashMap(cacheTableName)
				.expireAfterWrite(expirationTime)
				.makeOrGet();
		try{
		try{
//			Cache cache = getCache();
			element = cache.get(cacheKey);

			// fetch from cache
			if (element != null) {
				return element;
			}
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.FINEST, e.getMessage());
		}

		// fetch and process resource
		R data = null;
		T product = null;
		IOException networkException = null;

		try {
			long lastModified = element != null ? lastUpdateTime : 0;
			URL url = getResourceLocation(resource);

			// DEBUG
			 System.out.println(String.format("CachedResource.resourceLocation => %s (If-Modified-Since: %s)", url, java.time.Instant.ofEpochMilli(lastModified)));

			data = fetch(url, lastModified, element != null ? 0 : retryCountLimit);
		} catch (IOException e) {
			networkException = e;
			throw e;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		try {
			product = process(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		cache.put(cacheKey,product);
		db.commit();
		return product;
		}finally{
			db.rollback();
		}
	}

	protected URL getResourceLocation(String resource) throws IOException {
		return new URL(resource);
	}

	protected R fetch(URL url, long lastModified, int retries) throws IOException, InterruptedException {
		for (int i = 0; retries < 0 || i <= retries; i++) {
			try {
				if (i > 0) {
					Thread.sleep(retryWaitTime);
				}
				return fetchData(url, lastModified);
			} catch (FileNotFoundException e) {
				// if the resource doesn't exist no need for retries
				throw e;
			} catch (IOException e) {
				if (i >= 0 && i >= retries) {
					throw e;
				}
			}
		}
		return null; // can't happen
	}
}
