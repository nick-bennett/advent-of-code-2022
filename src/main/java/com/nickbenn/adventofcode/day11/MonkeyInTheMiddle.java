package com.nickbenn.adventofcode.day11;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonkeyInTheMiddle {

  private static final int PART_1_ROUND_LIMIT = 20;
  private static final int PART_2_ROUND_LIMIT = 10_000;
  private static final int PART_1_DIVISOR = 3;
  private static final int PART_2_DIVISOR = 1;
  private static final int INSPECTIONS_PRODUCT_COUNT = 2;

  private final List<Monkey> monkeys;

  public MonkeyInTheMiddle() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public MonkeyInTheMiddle(String inputFile) throws IOException {
    try (
        Stream<String> paragraphs = new DataSource.Builder()
            .setInputFile(inputFile)
            .setContext(getClass())
            .build()
            .paragraphs()
    ) {
      monkeys = paragraphs
          .map(Monkey::new)
          .collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println(new MonkeyInTheMiddle().getTopProduct(PART_1_ROUND_LIMIT, PART_1_DIVISOR));
    System.out.println(new MonkeyInTheMiddle().getTopProduct(PART_2_ROUND_LIMIT, PART_2_DIVISOR));
  }

  public long getTopProduct(int numRounds, int divisor) {
    long leastCommonMultiple = monkeys
        .stream()
        .mapToLong(Monkey::getModulus)
        .reduce(1, (a, b) -> a * b);
    for (int round = 0; round < numRounds; round++) {
      for (Monkey monkey : monkeys) {
        monkey.inspect(divisor, leastCommonMultiple);
      }
    }
    return monkeys
        .stream()
        .sorted(Comparator.comparing(Monkey::getInspections).reversed())
        .limit(INSPECTIONS_PRODUCT_COUNT)
        .mapToLong(Monkey::getInspections)
        .reduce(1, (a, b) -> a * b);
  }

  private static class Monkey {

    private static final String ID_GROUP = "id";
    private static final String ITEMS_GROUP = "items";
    private static final String OPERATION_GROUP = "operation";
    private static final String OPERAND_GROUP = "operand";
    private static final String MODULUS_GROUP = "modulus";
    private static final String TRUE_TARGET_GROUP = "trueTarget";
    private static final String FALSE_TARGET_GROUP = "falseTarget";
    private static final Pattern SPECIFICATION_PATTERN = Pattern.compile(
        "^Monkey\\s+(?<" + ID_GROUP + ">\\d+):"
            + "\\s+Starting\\s+items:\\s*(?<" + ITEMS_GROUP + ">\\d+(?:\\s*,\\s*\\d+)*)?"
            + "\\s+Operation:\\s*new\\s*=\\s*old\\s*(?<" + OPERATION_GROUP + ">[+*])"
            + "\\s*(?:old|(?<" + OPERAND_GROUP + ">\\d+))"
            + "\\s+Test:\\s*divisible\\s+by\\s+(?<" + MODULUS_GROUP + ">\\d+)"
            + "\\s+If\\s+true:\\s*throw\\s+to\\s+monkey\\s+(?<" + TRUE_TARGET_GROUP + ">\\d+)"
            + "\\s+If\\s+false:\\s*throw\\s+to\\s+monkey\\s+(?<" + FALSE_TARGET_GROUP + ">\\d+).*$",
        Pattern.DOTALL
    );
    private static final Pattern ITEM_DELIMITER = Pattern.compile("\\s*,\\s*");
    private static final String TO_STRING_FORMAT =
        "%s{id=%d, operation=%s, operand=%s, modulus=%d, truetarget=%d, falseTarget=%d, inspections=%d}";

    private static final Map<Integer, Monkey> monkeyMap = new HashMap<>();

    private final int id;
    private final Deque<Item> items;
    private final Operation operation;
    private final Integer operand;
    private final int modulus;
    private final int trueTarget;
    private final int falseTarget;

    private long inspections;

    public Monkey(String input) {
      Matcher matcher = SPECIFICATION_PATTERN.matcher(input);
      if (!matcher.matches()) {
        throw new IllegalArgumentException();
      }
      id = Integer.parseInt(matcher.group(ID_GROUP));
      items = ITEM_DELIMITER
          .splitAsStream(matcher.group(ITEMS_GROUP))
          .mapToInt(Integer::parseInt)
          .mapToObj(Item::new)
          .collect(Collectors.toCollection(LinkedList::new));
      operation = Operation.valueOf(matcher.group(OPERATION_GROUP).charAt(0));
      operand = (matcher.group(OPERAND_GROUP) != null)
          ? Integer.valueOf(matcher.group(OPERAND_GROUP))
          : null;
      modulus = Integer.parseInt(matcher.group(MODULUS_GROUP));
      trueTarget = Integer.parseInt(matcher.group(TRUE_TARGET_GROUP));
      falseTarget = Integer.parseInt(matcher.group(FALSE_TARGET_GROUP));
      monkeyMap.put(id, this);
    }

    public void inspect(int divisor, long normFactor) {
      while (!items.isEmpty()) {
        Item item = items.removeFirst();
        int target = (item.update(operation, operand, divisor, normFactor) % modulus == 0)
            ? trueTarget
            : falseTarget;
        monkeyMap.get(target).items.add(item);
        inspections++;
      }
    }

    public int getModulus() {
      return modulus;
    }

    public long getInspections() {
      return inspections;
    }

    @Override
    public String toString() {
      return String.format(TO_STRING_FORMAT, getClass().getSimpleName(), id, operation, operand,
          modulus, trueTarget, falseTarget, inspections);
    }

  }

  private static class Item {

    private static final String TO_STRING_FORMAT = "%s{worry=%d}";

    private long worry;

    public Item(long worry) {
      this.worry = worry;
    }

    public long update(Operation operation, Integer operand, int divisor, long modulus) {
      long result = operation.applyAsLong(worry, (operand != null) ? operand : worry) / divisor;
      if (divisor == 1 && modulus > 1) {
        result %= modulus;
      }
      return worry = result;
    }

    @Override
    public String toString() {
      return String.format(TO_STRING_FORMAT, getClass().getSimpleName(), worry);
    }

  }

  private enum Operation implements LongBinaryOperator {

    ADDITION('+') {
      @Override
      public long applyAsLong(long left, long right) {
        return left + right;
      }
    },
    MULTIPLICATION('*') {
      @Override
      public long applyAsLong(long left, long right) {
        return left * right;
      }
    };

    private static final Map<Character, Operation> SYMBOL_MAP;

    private final char symbol;

    static {
      SYMBOL_MAP = Arrays.stream(values())
          .collect(Collectors.toMap(Operation::getSymbol, Function.identity()));
    }

    Operation(char symbol) {
      this.symbol = symbol;
    }

    public static Operation valueOf(char symbol) {
      return SYMBOL_MAP.get(symbol);
    }

    public char getSymbol() {
      return symbol;
    }

  }

}
