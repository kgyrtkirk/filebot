
package net.sourceforge.filebot.ui.panel.rename;


import static java.awt.datatransfer.DataFlavor.*;
import static net.sourceforge.filebot.ui.transfer.FileTransferable.*;
import static net.sourceforge.tuned.FileUtilities.*;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import net.sourceforge.filebot.MediaTypes;
import net.sourceforge.filebot.hash.HashType;
import net.sourceforge.filebot.hash.VerificationFileScanner;
import net.sourceforge.filebot.torrent.Torrent;
import net.sourceforge.filebot.ui.transfer.ArrayTransferable;
import net.sourceforge.filebot.ui.transfer.FileTransferablePolicy;
import net.sourceforge.filebot.web.Episode;
import net.sourceforge.tuned.FastFile;


class NamesListTransferablePolicy extends FileTransferablePolicy {
	
	private static final DataFlavor episodeArrayFlavor = ArrayTransferable.flavor(Episode.class);
	
	private final List<Object> model;
	

	public NamesListTransferablePolicy(List<Object> model) {
		this.model = model;
	}
	

	@Override
	protected void clear() {
		model.clear();
	}
	

	@Override
	public boolean accept(Transferable tr) throws Exception {
		return tr.isDataFlavorSupported(stringFlavor) || hasFileListFlavor(tr);
	}
	

	@Override
	protected boolean accept(List<File> files) {
		return true;
	}
	

	@Override
	public void handleTransferable(Transferable tr, TransferAction action) throws Exception {
		if (action == TransferAction.PUT) {
			clear();
		}
		
		if (tr.isDataFlavorSupported(episodeArrayFlavor)) {
			// episode array transferable
			model.addAll(Arrays.asList((Episode[]) tr.getTransferData((episodeArrayFlavor))));
		} else if (hasFileListFlavor(tr)) {
			// file transferable
			load(getFilesFromTransferable(tr));
		} else if (tr.isDataFlavorSupported(stringFlavor)) {
			// string transferable
			load((String) tr.getTransferData(stringFlavor));
		}
	}
	

	protected void load(String string) {
		List<String> values = new ArrayList<String>();
		
		Scanner scanner = new Scanner(string);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			
			if (line.length() > 0) {
				values.add(line);
			}
		}
		
		model.addAll(values);
	}
	

	@Override
	protected void load(List<File> files) throws IOException {
		List<Object> values = new ArrayList<Object>();
		
		if (containsOnly(files, MediaTypes.getFilter("application/list"))) {
			// list files
			loadListFiles(files, values);
		} else if (containsOnly(files, MediaTypes.getFilter("verification"))) {
			// verification files
			loadVerificationFiles(files, values);
		} else if (containsOnly(files, MediaTypes.getFilter("application/torrent"))) {
			// torrent files
			loadTorrentFiles(files, values);
		} else if (containsOnly(files, FOLDERS)) {
			// load files from each folder
			for (File folder : files) {
				values.addAll(FastFile.foreach(folder.listFiles()));
			}
		} else {
			// just add all given files
			values.addAll(FastFile.foreach(files));
		}
		
		model.addAll(values);
	}
	

	protected void loadListFiles(List<File> files, List<Object> values) throws FileNotFoundException {
		for (File file : files) {
			// don't use new Scanner(File) because of BUG 6368019 (http://bugs.sun.com/view_bug.do?bug_id=6368019)
			Scanner scanner = new Scanner(new FileInputStream(file), "UTF-8");
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				
				if (line.length() > 0) {
					values.add(line);
				}
			}
			
			scanner.close();
		}
	}
	

	protected void loadVerificationFiles(List<File> files, List<Object> values) throws IOException {
		for (File file : files) {
			HashType format = HashType.forName(getExtension(file));
			
			// check if format is valid
			if (format == null)
				continue;
			
			// add all file names from verification file
			VerificationFileScanner scanner = new VerificationFileScanner(file, format.getFormat());
			
			try {
				while (scanner.hasNext()) {
					values.add(new AbstractFile(scanner.next().getKey().getName(), -1));
				}
			} finally {
				scanner.close();
			}
		}
	}
	

	protected void loadTorrentFiles(List<File> files, List<Object> values) throws IOException {
		for (File file : files) {
			Torrent torrent = new Torrent(file);
			
			for (Torrent.Entry entry : torrent.getFiles()) {
				values.add(new AbstractFile(entry.getName(), entry.getLength()));
			}
		}
	}
	

	@Override
	public String getFileFilterDescription() {
		return "text files, verification files, torrent files";
	}
	
}
