package com.nickbenn.adventofcode.day14;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RegolithReservoir {

  private static final Pattern POINT_SPLITTER = Pattern.compile("\\s*->\\s*");
  private static final Pattern COORDINATE_SPLITTER = Pattern.compile("\\s*,\\s*");
  private static final int STARTING_X = 500;
  private static final int STARTING_Y = 0;

  private final Map<Integer, List<Integer>> occupied;

  public RegolithReservoir() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public RegolithReservoir(String inputFile) throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      occupied = new TreeMap<>();
      lines
          .forEach((line) -> {
            int[] previous = {-1, -1};
            POINT_SPLITTER
                .splitAsStream(line)
                .map((coordinates) ->
                    COORDINATE_SPLITTER
                        .splitAsStream(coordinates)
                        .mapToInt(Integer::parseInt)
                        .toArray()
                )
                .forEach((point) -> {
                  if (previous[1] >= 0) {
                    int stepX = (int) Math.signum(point[0] - previous[0]);
                    int stepY = (int) Math.signum(point[1] - previous[1]);
                    int x = previous[0];
                    int y = previous[1];
                    do {
                      x += stepX;
                      y += stepY;
                      addRock(x, y);
                    } while (x != point[0] || y != point[1]);
                  } else {
                    addRock(point[0], point[1]);
                  }
                  previous[0] = point[0];
                  previous[1] = point[1];
                });
          });
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println(new RegolithReservoir().dropSandIntoAbyss());
    System.out.println(new RegolithReservoir().dropSandUntilBlocked());
  }

  public int dropSandIntoAbyss() {
    int count = 0;
    drop:
    do {
      int x = STARTING_X;
      int y = STARTING_Y;
      while (true) {
        int depth = settleDepth(x, y);
        if (depth == y) {
          x--;
          depth = settleDepth(x, y);
          if (depth == y) {
            x += 2;
            depth = settleDepth(x, y);
          }
        }
        if (depth == y) {
          addRock(x - 1, depth);
          count++;
          break;
        } else if (depth < Integer.MAX_VALUE) {
          y = depth;
        } else {
          break drop;
        }
      }
    } while (true);
    return count;
  }

  public int dropSandUntilBlocked() {
    int floorDepth = maxDepth() + 2;
    int count = 0;
    while (occupied.getOrDefault(STARTING_X, List.of(Integer.MAX_VALUE)).get(0) != STARTING_Y) {
      int x = STARTING_X;
      int y = STARTING_Y;
      while (true) {
        int depth = settleDepth(x, y);
        if (depth == y) {
          x--;
          depth = settleDepth(x, y);
          if (depth == y) {
            x += 2;
            depth = settleDepth(x, y);
          }
        }
        if (depth == y) {
          addRock(x - 1, depth);
          count++;
          break;
        } else if (depth < Integer.MAX_VALUE) {
          y = depth;
        } else {
          addRock(x, floorDepth - 1);
          count++;
          break;
        }
      }
    }
    return count;
  }

  private void addRock(int x, int y) {
    List<Integer> column = occupied.computeIfAbsent(x, (position) -> new ArrayList<>());
    int position = binarySearch(column, y);
    if (position < 0) {
      column.add(~position, y);
    }
  }

  private int settleDepth(int x, int y) {
    int result = y;
    List<Integer> column = occupied.get(x);
    if (column != null) {
      int searchResult = ~binarySearch(column, y + 1);
      if (searchResult >= column.size()) {
        result = Integer.MAX_VALUE;
      } else if (searchResult >= 0) {
        result = column.get(searchResult) - 1;
      }
    } else {
      result = Integer.MAX_VALUE;
    }
    return result;
  }

  private static int binarySearch(List<Integer> haystack, int needle) {
    return binarySearch(haystack, needle, 0, haystack.size());
  }

  private static int binarySearch(List<Integer> haystack, int needle, int from, int to) {
    int position;
    if (to > from) {
      int midpoint = (from + to) / 2;
      int test = haystack.get(midpoint);
      if (test == needle) {
        position = midpoint;
      } else if (test < needle) {
        position = binarySearch(haystack, needle, midpoint + 1, to);
      } else {
        position = binarySearch(haystack, needle, from, midpoint);
      }
    } else {
      position = ~from;
    }
    return position;
  }

  private int maxDepth() {
    return occupied
        .values()
        .stream()
        .filter(Predicate.not(List::isEmpty))
        .mapToInt((column) -> column.get(column.size() - 1))
        .max()
        .orElseThrow();
  }

}
