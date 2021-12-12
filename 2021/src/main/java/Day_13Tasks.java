import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day_13Tasks {
    private static final AdventOfCode aoc2021 = AOCFactory.aoc2021;

    private static int[] toInt(String[] arr) {
        return Arrays.stream(arr).mapToInt(Integer::parseInt).toArray();
    }

    private static void printArr(Object[] arr) {
        Arrays.stream(arr).forEach(e -> {
            System.out.print(e + ",");
        });
        System.out.println("");
    }

    @Advent(day = Day.Day_13, part = Part.one)
    public int Day_13Part1(String[] inputs) throws IOException {
        return 0;
    }

    @Advent(day = Day.Day_13, part = Part.two)
    public int Day_13Part2(String[] inputs) throws IOException {
        return 0;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_13Tasks());
    }
}
