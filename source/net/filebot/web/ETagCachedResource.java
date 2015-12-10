package net.filebot.web;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapdb.DB;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public abstract class ETagCachedResource<T extends Serializable> extends CachedResource<T> {

	public ETagCachedResource(String resource, Class<T> type) {
		super(resource, type, ONE_WEEK, 2, 1000);
	}

	public ETagCachedResource(String resource, Class<T> type, long expirationTime, int retryCountLimit, long retryWaitTime) {
		super(resource, type, expirationTime, retryCountLimit, retryWaitTime);
	}

	
	@Override
	protected ByteBuffer fetchData(URL url, long lastModified) throws IOException {
		String etagKey = "ETag1" + ":" + url.toString();
		DB db = getCache();
		Map<String,Element> cache = db.createHashMap("e"+expirationTime)
				.expireAfterWrite(expirationTime)
				.makeOrGet();
		try{
		Element etag = cache.get(etagKey);

		Map<String, String> requestParameters = new HashMap<String, String>();
		if (etag != null && etag.getObjectValue() != null) {
			requestParameters.put("If-None-Match", etag.getObjectKey().toString());
		}

		// If-Modified-Since must not be set if If-None-Match is set
		Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();
		ByteBuffer data = WebRequest.fetch(url, requestParameters.size() > 0 ? -1 : lastModified, requestParameters, responseHeaders);
		
		if(data == null){
			if(etag!=null){
				return ByteBuffer.wrap((byte[]) etag.getObjectValue());
			}else{
				throw new IllegalStateException();
			}
		}

		if (responseHeaders.containsKey("ETag")) {
//			throw new RuntimeException("X");
			cache.put(etagKey,new Element(responseHeaders.get("ETag").get(0), data.array()));
		}
		db.commit();
		return data;
		}finally{
			db.rollback();
		}
	}


}
