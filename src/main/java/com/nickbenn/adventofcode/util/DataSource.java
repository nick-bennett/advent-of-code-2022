package com.nickbenn.adventofcode.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DataSource {

  private static final Pattern PARAGRAPH_SPLITTER = Pattern.compile("\\r?\\n\\s*?\\r?\\n");
  private static final boolean DEFAULT_TRIMMED = true;
  private static final boolean DEFAULT_STRIPPED = true;

  private final Path path;

  public DataSource(Class<?> clazz, String inputFile) {
    try {
      //noinspection DataFlowIssue
      path = Paths.get(clazz.getResource(inputFile).toURI());
    } catch (URISyntaxException e) { // Should never happen with Class.getResource(String).toURI().
      throw new RuntimeException(e);
    }
  }

  public Stream<String> lines() throws IOException {
    return lines(DEFAULT_TRIMMED, DEFAULT_STRIPPED);
  }

  public Stream<String> lines(boolean trimmed, boolean stripped) throws IOException {
    Stream<String> lines = Files.lines(path);
    if (trimmed) {
      lines = lines.map(String::trim);
    }
    if (stripped) {
      lines = lines.filter(Predicate.not(String::isEmpty));
    }
    return lines;
  }

  public Stream<String> blocks(Pattern splitter) throws IOException {
    return blocks(splitter, DEFAULT_TRIMMED, DEFAULT_STRIPPED);
  }

  public Stream<String> blocks(Pattern splitter, boolean trimmed, boolean stripped)
      throws IOException {
    Stream<String> blocks = splitter.splitAsStream(Files.readString(path));
    if (trimmed) {
      blocks = blocks.map(String::trim);
    }
    if (stripped) {
      blocks = blocks.filter(Predicate.not(String::isEmpty));
    }
    return blocks;
  }

  public Stream<Stream<String>> blockLines(Pattern splitter) throws IOException {
    return blockLines(splitter, DEFAULT_TRIMMED, DEFAULT_STRIPPED);
  }

  public Stream<Stream<String>> blockLines(Pattern splitter, boolean trimmed, boolean stripped)
      throws IOException {
    return expand(blocks(splitter, trimmed, stripped), trimmed, stripped);
  }

  public Stream<String> paragraphs() throws IOException {
    return paragraphs(DEFAULT_TRIMMED, DEFAULT_STRIPPED);
  }

  public Stream<String> paragraphs(boolean trimmed, boolean stripped) throws IOException {
    return blocks(PARAGRAPH_SPLITTER, trimmed, stripped);
  }

  public Stream<Stream<String>> paragraphLines() throws IOException {
    return paragraphLines(DEFAULT_TRIMMED, DEFAULT_STRIPPED);
  }

  public Stream<Stream<String>> paragraphLines(boolean trimmed, boolean stripped)
      throws IOException {
    return expand(paragraphs(trimmed, stripped), trimmed, stripped);
  }

  private Stream<Stream<String>> expand(Stream<String> input, boolean trimmed, boolean stripped) {
    return input
        .map((block) ->
            block
                .lines()
                .map(trimmed ? String::trim : Function.identity())
                .filter(stripped ? Predicate.not(String::isEmpty) : (line) -> true)
        );
  }

}
