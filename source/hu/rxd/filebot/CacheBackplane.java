package hu.rxd.filebot;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class CacheBackplane {

	private static DB db;
	private static File databaseFile;

	public static DB getDB() {
		if (db == null) {
			db = DBMaker.newFileDB(getDatabaseFile()).closeOnJvmShutdown().make();
		}
		return db;
	}

	public static File getDatabaseFile() {
		if (databaseFile == null) {
			File tmpDir = new File(System.getProperty("java.io.tmpdir"));
			if (!tmpDir.exists()) {
				throw new RuntimeException("tmpdir doesnt exists? (java.io.tmpdir)" + tmpDir);
			}
			return new File(tmpDir, "filebot.db");
		}
		return databaseFile;
	}

	public static void setDatabaseFile(File databaseFile) {
		if (db != null) {
			throw new RuntimeException("db already exists; this must be called prior to first use");
		}
		CacheBackplane.databaseFile = databaseFile;
	}

}
