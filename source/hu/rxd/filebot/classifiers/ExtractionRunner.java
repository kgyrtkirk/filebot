package hu.rxd.filebot.classifiers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.google.common.collect.Sets;

import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.visitor.ISectionVisitor;
import net.filebot.cli.CmdlineOperations;
import net.filebot.cli.ConflictAction;

public class ExtractionRunner implements ISectionVisitor {
	
	private File extractionRoot;
	public ExtractionRunner(File extractionRoot) {
		this.extractionRoot = extractionRoot;
	}
	
	@Override
	public void visit(ISection node) throws Exception {
		
		CmdlineOperations cmdline = new CmdlineOperations();
		File extractDir = new File(extractionRoot,node.getName());
		List<File> ef = cmdline.extract(Sets.newHashSet(node.getAbsoluteFile()),
				extractDir.getAbsolutePath(),
				ConflictAction.AUTO.toString(),
				null,
				false);
		
//		flatFileList(extractDir);
		
		for (File file : flatFileList(extractDir)) {
			node.getParent().getShadowEntry(file);
		}
		System.out.println(ef);
		System.out.println(flatFileList(extractDir));
//		MediaCollection c = (MediaCollection)node.getParent();
		
//		c.getEntry(name);
		
	}

	private Collection<File> flatFileList(File dir) {
		List<File>	ret=new ArrayList<>();
		
		Queue<File>	q=new PriorityQueue<>();
		q.add(dir);
		while(!q.isEmpty()){
			File f = q.poll();
			if(f.isDirectory()){
				q.add(f);
			}else{
				ret.add(f);
			}
		}
		return ret;
	}

}
