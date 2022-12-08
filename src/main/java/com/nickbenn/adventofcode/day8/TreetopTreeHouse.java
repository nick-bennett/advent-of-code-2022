package com.nickbenn.adventofcode.day8;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.stream.Stream;

public class TreetopTreeHouse {

  private final int[][] forest;

  public TreetopTreeHouse() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public TreetopTreeHouse(String inputFile) throws IOException {
    try (
        Stream<int[]> lines = new DataSource.Builder()
            .setInputFile(inputFile)
            .setContext(getClass())
            .build()
            .digits()
    ) {
      forest = lines.toArray(int[][]::new);
    }
  }

  public static void main(String[] args) throws IOException {
    TreetopTreeHouse treetop = new TreetopTreeHouse();
    System.out.println(treetop.getVisibleTrees());
    System.out.println(treetop.getMaxScenicScore());
  }

  public int getVisibleTrees() throws IOException {
    int[][] fromWest = new int[forest.length][forest[0].length];
    int[][] fromEast = new int[forest.length][forest[0].length];
    int[][] fromNorth = new int[forest.length][forest[0].length];
    int[][] fromSouth = new int[forest.length][forest[0].length];
    computeNorthSouthThresholds(fromNorth, fromSouth);
    computeEastWestThresholds(fromWest, fromEast);
    return countVisible(fromNorth, fromEast, fromSouth, fromWest);
  }

  public int getMaxScenicScore() throws IOException {
    int bestScore = 0;
    for (int row = 1; row < forest.length - 1; row++) {
      for (int col = 1; col < forest[row].length - 1; col++) {
        bestScore = Math.max(bestScore, getScenicScore(row, col));
      }
    }
    return bestScore;
  }

  private void computeEastWestThresholds(int[][] fromWest, int[][] fromEast) {
    for (int row = 0; row < forest.length; row++) {
      int length = forest[row].length;
      fromWest[row][0] = -1;
      fromEast[row][forest[row].length - 1] = -1;
      for (int left = 1, right = length - 2; left < length; left++, right--) {
        fromWest[row][left] = Math.max(fromWest[row][left - 1], forest[row][left - 1]);
        fromEast[row][right] = Math.max(fromEast[row][right + 1], forest[row][right + 1]);
      }
    }
  }

  private void computeNorthSouthThresholds(int[][] fromNorth, int[][] fromSouth) {
    for (int col = 0; col < forest[0].length; col++) {
      fromNorth[0][col] = -1;
      fromSouth[forest.length - 1][col] = -1;
      for (int top = 1, bttm = forest.length - 2; top < forest.length; top++, bttm--) {
        fromNorth[top][col] = Math.max(fromNorth[top - 1][col], forest[top - 1][col]);
        fromSouth[bttm][col] = Math.max(fromSouth[bttm + 1][col], forest[bttm + 1][col]);
      }
    }
  }

  private int countVisible(int[][] fromNorth, int[][] fromEast, int[][] fromSouth, int[][] fromWest) {
    int count = 0;
    for (int row = 0; row < forest.length; row++) {
      for (int col = 0; col < forest[row].length; col++) {
        if (forest[row][col] > fromNorth[row][col]
            || forest[row][col] > fromEast[row][col]
            || forest[row][col] > fromSouth[row][col]
            || forest[row][col] > fromWest[row][col]) {
          count++;
        }
      }
    }
    return count;
  }

  private int getScenicScore(int row, int col) {
    int height = forest[row][col];
    int viewScore = 1;
    for (Direction dir : Direction.values()) {
      viewScore *= getVisibleTrees(row, col, height, dir);
    }
    return viewScore;
  }

  private int getVisibleTrees(int row, int col, int viewerHeight, Direction direction) {
    int rowInc = direction.rowIncrement;
    int colInc = direction.colIncrement;
    int viewTrees = 0;
    for (int viewRow = row + rowInc, viewCol = col + colInc;
        inBounds(viewRow, viewCol);
        viewRow += rowInc, viewCol += colInc) {
      viewTrees++;
      if (forest[viewRow][viewCol] >= viewerHeight) {
        break;
      }
    }
    return viewTrees;
  }

  private boolean inBounds(int row, int col) {
    return row >= 0 && row < forest.length
        && col >= 0 && col < forest[row].length;
  }

  private enum Direction {

    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    private final int rowIncrement;
    private final int colIncrement;

    Direction(int rowIncrement, int colIncrement) {
      this.rowIncrement = rowIncrement;
      this.colIncrement = colIncrement;
    }

  }

}
