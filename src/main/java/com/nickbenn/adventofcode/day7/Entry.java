package com.nickbenn.adventofcode.day7;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class Entry {

  private static final Pattern ENTRY_PATTERN =
      Pattern.compile("^\\s*(?:dir|(\\d+))\\s+(\\S+)\\s*$");

  private final Directory parent;
  private final String name;

  protected Entry(Directory parent, String name) {
    this.parent = parent;
    this.name = name;
  }

  public static Directory makeRoot() {
    return new Directory(null, null);
  }

  public String name() {
    return name;
  }

  public abstract int size();

  public Directory parent() {
    return parent;
  }

  public static final class File extends Entry {

    private final int size;

    private File(Directory parent, String name, int size) {
      super(parent, name);
      this.size = size;
    }

    @Override
    public int size() {
      return size;
    }

  }

  public static final class Directory extends Entry {

    private final Map<String, File> files;
    private final Map<String, Directory> subdirectories;

    private Integer size;

    private Directory(Directory parent, String name) {
      super(parent, name);
      files = new LinkedHashMap<>();
      subdirectories = new LinkedHashMap<>();
    }

    @Override
    public int size() {
      if (size == null) {
        size = Stream.concat(
                files.values().stream(),
                subdirectories.values().stream()
            )
            .mapToInt(Entry::size)
            .sum();
      }
      return size;
    }

    public void parse(String input) {
      Matcher matcher = ENTRY_PATTERN.matcher(input);
      if (matcher.matches()) {
        if (matcher.group(1) == null) {
          Directory dir = new Directory(this, matcher.group(2));
          addSubdirectory(dir);
        } else {
          File file = new File(this, matcher.group(2), Integer.parseInt(matcher.group(1)));
          addFile(file);
        }
      }
    }

    private void addFile(File child) {
      files.put(child.name(), child);
    }

    private void addSubdirectory(Directory child) {
      subdirectories.put(child.name(), child);
    }

    public Directory getSubdirectory(String name) {
      return subdirectories.get(name);
    }

    public File getFile(String name) {
      return files.get(name);
    }

    public Stream<Directory> flatTree() {
      return Stream.concat(
          Stream.of(this),
          subdirectories
              .values()
              .stream()
              .flatMap(Directory::flatTree)
      );
    }

  }

}
