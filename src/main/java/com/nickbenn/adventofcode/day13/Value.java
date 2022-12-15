package com.nickbenn.adventofcode.day13;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

abstract class Value implements Comparable<Value> {

  private static final Pattern DELIMITER = Pattern.compile(
      "(?<=[\\[\\]])\\s*,*\\s*(?=[\\[\\]])"
          + "|(?<=\\d)\\s*,*\\s*(?=[\\[\\]])"
          + "|(?<=[\\[\\]])\\s*,*\\s*(?=\\d)"
          + "|(?<=\\d)\\s*,\\s*(?=\\d)");

  public static Value parse(String input) {
    Deque<Value> stack = new LinkedList<>();
    DELIMITER
        .splitAsStream(input)
        .map((str) ->
            switch (str) {
              case "[" -> Bracket.LEFT_BRACE;
              case "]" -> Bracket.RIGHT_BRACE;
              default -> new Scalar(Integer.parseInt(str));
            }
        )
        .forEach((token) -> {
          if (token == Bracket.RIGHT_BRACE) {
            List<Value> values = new LinkedList<>();
            for (Value value = stack.pop(); value != Bracket.LEFT_BRACE; value = stack.pop()) {
              values.add(0, value);
            }
            stack.push(new Vector(values));
          } else {
            stack.push(token);
          }
        });
    return stack.pop();
  }

  private static class Bracket extends Value {

    private static final Value LEFT_BRACE = new Bracket();
    private static final Value RIGHT_BRACE = new Bracket();

    @Override
    public int compareTo(@NotNull Value o) {
      return 0;
    }
  }

  private static class Scalar extends Value {

    private final int value;

    private Scalar(int value) {
      this.value = value;
    }

    @Override
    public int compareTo(@NotNull Value other) {
      return (other instanceof Scalar)
          ? Integer.compare(value, ((Scalar) other).value)
          : -other.compareTo(this);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @Override
    public int hashCode() {
      return 31 * value;
    }

    @Override
    public boolean equals(Object obj) {
      boolean result;
      if (this == obj) {
        result = true;
      } else if (obj instanceof Scalar other) {
        result = (other.value == value);
      } else {
        result = false;
      }
      return result;
    }

  }

  private static class Vector extends Value {

    private final List<Value> values;

    private Vector(Value value) {
      values = List.of(value);
    }

    private Vector(List<Value> values) {
      this.values = values;
    }

    @Override
    public int compareTo(@NotNull Value other) {
      int comparison = 0;
      if (other instanceof Vector) {
        Iterator<Value> leftIterator = values.iterator();
        Iterator<Value> rightIterator = ((Vector) other).values.iterator();
        while (leftIterator.hasNext() && rightIterator.hasNext()) {
          Value left = leftIterator.next();
          Value right = rightIterator.next();
          comparison = left.compareTo(right);
          if (comparison != 0) {
            break;
          }
        }
        if (comparison == 0) {
          comparison = (!leftIterator.hasNext() ? -1 : 0) + (!rightIterator.hasNext() ? 1 : 0);
        }
      } else if (other instanceof Scalar) {
        comparison = compareTo(new Vector(other));
      }
      return comparison;
    }

    @Override
    public String toString() {
      return values.toString();
    }

    @Override
    public int hashCode() {
      return values.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      boolean result;
      if (this == obj) {
        result = true;
      } else if (obj instanceof Vector other) {
        result = (other.values == values);
      } else if (obj instanceof Scalar other) {
        result = equals(new Vector(other));
      } else {
        result = false;
      }
      return result;
    }

  }

}
