package com.markphilpot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filesystem {

  public record File(String name, int size) {}

  public static class Directory {
    private final String name;
    private final Map<String, Directory> childDirs;
    private final List<File> childFiles;
    private final Directory parent;

    public Directory(String name, Directory parent) {
      this.name = name;
      this.parent = parent;
      this.childDirs = new HashMap<String, Directory>();
      this.childFiles = new ArrayList<File>();
    }

    public int getSize() {
      return childFiles.stream().map(File::size).reduce(0, Integer::sum)
          + childDirs.values().stream().map(Directory::getSize).reduce(0, Integer::sum);
    }

    public Directory getParent() {
      return parent;
    }

    public Directory getDir(String name) {
      if (!childDirs.containsKey(name)) {
        throw new IllegalArgumentException("No directory found: %s".formatted(name));
      }
      return childDirs.get(name);
    }

    public void addDir(Directory dir) {
      childDirs.put(dir.name, dir);
    }

    public void addFile(File file) {
      childFiles.add(file);
    }

    public List<Directory> getAllDirs() {
      var dirs = new ArrayList<Directory>();

      for (var d : childDirs.values()) {
        dirs.add(d);
        dirs.addAll(d.getAllDirs());
      }

      return dirs;
    }

    @Override
    public String toString() {
      return "Directory{"
          + "name='"
          + name
          + '\''
          + ", childDirs="
          + childDirs
          + ", childFiles="
          + childFiles
          + ", parent="
          + (parent != null ? parent.name : "null")
          + '}';
    }
  }

  public static Directory getRoot() {
    return new Directory("/", null);
  }
}
