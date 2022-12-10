package com.nickbenn.adventofcode.day10;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CathodeRayTube {

  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^noop|addx\\s+(-?\\d+).*$");
  private static final int INITIAL_X = 1;
  private static final int NOOP_CYCLES = 1;
  private static final int ADD_CYCLES = 2;
  private static final int SAMPLE_OFFSET = 20;
  private static final int SAMPLE_SIZE = 40;
  private static final int SCAN_LINE_LENGTH = 40;
  private static final int OVERLAP_TOLERANCE = 1;
  private static final char OVERLAP_CHARACTER = '#';
  private static final char NON_OVERLAP_CHARACTER = '.';
  private static final char SCAN_LINE_DELIMITER = '\n';

  private final String inputFile;

  public CathodeRayTube() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public CathodeRayTube(String inputFile) throws IOException {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    CathodeRayTube tube = new CathodeRayTube();
    System.out.println(tube.getStrengthProductSum());
    System.out.println(tube.getScan());
  }

  public int getStrengthProductSum() throws IOException {
    try (Stream<String> instructions = DataSource.simpleLines(inputFile, getClass())) {
      Accumulator accumulator = new Accumulator();
      return instructions
          .map(INSTRUCTION_PATTERN::matcher)
          .filter(Matcher::matches)
          .map(Instruction::new)
          .mapToInt((instruction) -> getSample(accumulator, instruction))
          .sum();
    }
  }

  public String getScan() throws IOException {
    try (Stream<String> instructions = DataSource.simpleLines(inputFile, getClass())) {
      Accumulator accumulator = new Accumulator();
      String scan = instructions
          .map(INSTRUCTION_PATTERN::matcher)
          .filter(Matcher::matches)
          .map(Instruction::new)
          .map((instruction) -> getPixels(accumulator, instruction))
          .collect(Collectors.joining());
      return splitLines(scan);
    }
  }

  private static int getSample(Accumulator accumulator, Instruction instruction) {
    int sample = 0;
    for (int i = 0; i < instruction.getCycles(); i++) {
      int ticks = accumulator.tick();
      sample += (ticks % SAMPLE_SIZE == SAMPLE_OFFSET) ? (ticks * accumulator.getX()) : 0;
    }
    accumulator.increment(instruction.getIncrement());
    return sample;
  }

  private static String getPixels(Accumulator accumulator, Instruction instruction) {
    StringBuilder pixels = new StringBuilder(2);
    for (int i = 0; i < instruction.getCycles(); i++) {
      pixels.append(
          overlapping(accumulator.getTicks() % SCAN_LINE_LENGTH, accumulator.getX())
              ? OVERLAP_CHARACTER
              : NON_OVERLAP_CHARACTER
      );
      accumulator.tick();
    }
    accumulator.increment(instruction.getIncrement());
    return pixels.toString();
  }

  private static boolean overlapping(int scanPosition, int spritePosition) {
    return scanPosition >= spritePosition - OVERLAP_TOLERANCE
        && scanPosition <= spritePosition + OVERLAP_TOLERANCE;
  }

  private static String splitLines(String scan) {
    StringBuilder scanLines = new StringBuilder(scan);
    for (int position = SCAN_LINE_LENGTH;
        position < scanLines.length();
        position += SCAN_LINE_LENGTH + 1) {
      scanLines.insert(position, SCAN_LINE_DELIMITER);
    }
    return scanLines.toString();
  }

  private static class Instruction {

    private final int cycles;
    private final int increment;

    public Instruction(Matcher matcher) {
      if (matcher.group(1) != null) {
        cycles = ADD_CYCLES;
        increment = Integer.parseInt(matcher.group(1));
      } else {
        cycles = NOOP_CYCLES;
        increment = 0;
      }
    }

    public int getCycles() {
      return cycles;
    }

    public int getIncrement() {
      return increment;
    }

  }

  private static class Accumulator {

    private int ticks = 0;
    private int x = INITIAL_X;

    public int tick() {
      return ++ticks;
    }

    public void increment(int increment) {
      x += increment;
    }

    public int getTicks() {
      return ticks;
    }

    public int getX() {
      return x;
    }

  }

}
