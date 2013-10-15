package net.sourceforge.filebot.web;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CachedXmlResource extends AbstractCachedResource<String, String> {

	public CachedXmlResource(String resource) {
		super(resource, String.class, 24 * 60 * 60 * 1000, 2, 1000);
	}

	@Override
	protected Cache getCache() {
		return CacheManager.getInstance().getCache("web-persistent-datasource");
	}

	public Document getDocument() throws IOException {
		try {
			return WebRequest.getDocument(get());
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String process(String data) throws Exception {
		return data;
	}

	@Override
	protected String fetchData(URL url, long lastModified) throws IOException {
		ByteBuffer data = WebRequest.fetchIfModified(url, lastModified);

		if (data == null)
			return null; // not modified

		return Charset.forName("UTF-8").decode(data).toString();
	}

}
