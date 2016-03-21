package net.filebot.util;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;

public class FileSet extends AbstractSet<Path> {

	private static final int ROOT_LEVEL = -1;

	private final Map<Path, FileSet> folders = new HashMap<Path, FileSet>(4, 2);
	private final Set<Path> files = new HashSet<Path>(4, 2);

	private boolean add(Path e, int depth) {
		// add new leaf element
		if (e.getNameCount() - 1 == depth) {
			return files.add(e.getFileName());
		}

		// add new node element
		return folders.computeIfAbsent(depth == ROOT_LEVEL ? e.getRoot() : e.getName(depth), k -> new FileSet()).add(e, depth + 1);
	}

	@Override
	public boolean add(Path e) {
		return add(e, ROOT_LEVEL);
	}

	public boolean add(File e) {
		return add(e.toPath());
	}

	public boolean add(String e) {
		return add(getPath(e));
	}

	public void feed(Stream<? extends Object> stream) {
		stream.forEach(path -> add(path.toString()));
	}

	private boolean contains(Path e, int depth) {
		// add new leaf element
		if (e.getNameCount() - depth == 1) {
			return files.contains(e.getFileName());
		}

		// add new node element
		if (e.getNameCount() - depth > 1) {
			FileSet subSet = folders.get(e.getName(depth));
			return subSet == null ? false : subSet.contains(e, depth + 1);
		}

		return false;
	}

	public boolean contains(Path e) {
		return contains(e, 0);
	};

	public boolean contains(File e) {
		return contains(e.toPath());
	}

	public boolean contains(String e) {
		return contains(getPath(e));
	}

	@Override
	public boolean contains(Object e) {
		return contains(e.toString());
	};

	protected Path getPath(String path) {
		return Paths.get(path);
	}

	public Map<Path, List<Path>> getRoots() {
		if (folders.size() != 1 || files.size() > 0) {
			return emptyMap();
		}

		Entry<Path, FileSet> entry = folders.entrySet().iterator().next();
		Path parent = entry.getKey();
		Map<Path, List<Path>> next = entry.getValue().getRoots();
		if (next.size() > 0) {
			// resolve children
			return next.entrySet().stream().collect(toMap(it -> {
				return parent.resolve(it.getKey());
			}, it -> it.getValue()));
		}

		// resolve children
		return folders.entrySet().stream().collect(toMap(it -> it.getKey(), it -> it.getValue().stream().collect(toList())));
	}

	@Override
	public int size() {
		return folders.values().stream().mapToInt(f -> f.size()).sum() + files.size();
	}

	@Override
	public Stream<Path> stream() {
		Stream<Path> descendants = folders.entrySet().stream().flatMap(node -> {
			return node.getValue().stream().map(f -> {
				return node.getKey().resolve(f);
			});
		});

		Stream<Path> children = files.stream();

		return Stream.concat(descendants, children);
	}

	@Override
	public Spliterator<Path> spliterator() {
		return stream().spliterator();
	}

	@Override
	public Iterator<Path> iterator() {
		return stream().iterator();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

}
