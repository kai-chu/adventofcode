import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DailyTasks {
    private static final AdventOfCode aoc2021 = AOCFactory.aoc2021;

    @Advent(day = Day.Day_2)
    public int day2Part1(String[] inputs) throws IOException {
        int[] position = new int[]{0, 0};
        int horizontal = 0, depth = 1;

        for (int i = 0; i < inputs.length; i++) {
            String[] cmd = inputs[i].split(" ");
            String direction = cmd[0];
            Integer distance = Integer.parseInt(cmd[1]);

            switch (direction) {
                case "forward":
                    position[horizontal] += distance;
                    break;
                case "down":
                    position[depth] += distance;
                    break;
                case "up":
                    position[depth] -= distance;
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            if (position[depth] < 0)
                throw new IllegalArgumentException();
        }

        ConsoleUtil.println(position[horizontal] + "," + position[depth]);
        int r = position[horizontal] * position[depth];
        ConsoleUtil.println(r);
        return r;
    }


    @Advent(day = Day.Day_2, part = Part.two)
    public int day2Part2(String[] inputs) throws IOException {
        int[] position = new int[]{0, 0, 0};
        int horizontal = 0, depth = 1, aim = 2;


        for (int i = 0; i < inputs.length; i++) {
            String[] cmd = inputs[i].split(" ");
            String direction = cmd[0];
            Integer distance = Integer.parseInt(cmd[1]);

            switch (direction) {
                case "forward":
                    position[horizontal] += distance;
                    position[depth] += distance * position[aim];
                    break;
                case "down":
                    position[aim] += distance;
                    break;
                case "up":
                    position[aim] -= distance;
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            if (position[depth] < 0)
                throw new IllegalArgumentException();
        }
        ConsoleUtil.println(position[horizontal] + "," + position[depth] + "," + position[aim]);
        int r = position[horizontal] * position[depth];
        ConsoleUtil.println(r);
        return r;
    }

    @Advent(day = Day.Day_1)
    public int day1Example(String[] inputs) throws IOException {
        int increased = 0;
        if (inputs.length <= 1) return increased;

        for (int i = 1; i < inputs.length; i++) {
            increased += Integer.parseInt(inputs[i]) > Integer.parseInt(inputs[i - 1]) ? 1 : 0;
        }
        return increased;
    }

    @Advent(day = Day.Day_1, part = Part.two)
    public int day1Part2(String[] inputs) throws IOException {
        int increased = 0;
        if (inputs.length <= 3) return increased;
        int sumOfWindow = 0;
        for (int i = 0; i < inputs.length; i++) {
            if (i < 3) sumOfWindow += Integer.parseInt(inputs[i]);
            else {
                int currentOfWindow = sumOfWindow + Integer.parseInt(inputs[i]) - Integer.parseInt(inputs[i - 3]);
                increased += currentOfWindow > sumOfWindow ? 1 : 0;
                sumOfWindow = currentOfWindow;
            }
        }
        ConsoleUtil.println(increased);
        return increased;
    }


    @Advent(day = Day.Day_3, part = Part.one)
    public int day3Part1(String[] inputs) throws IOException {
        int c = 0;
        int d = 0;
        for (int i = 0; i < inputs[0].length(); i++) {
            int one = 0;
            for (int j = 0; j < inputs.length; j++) {
                one += inputs[j].charAt(i) == '1' ? 1 : 0;
            }
            if (one > inputs.length - one)
                c |= 1 << inputs[0].length() - i - 1;
            else
                d |= 1 << inputs[0].length() - i - 1;
        }
        ConsoleUtil.println(c);
        ConsoleUtil.println(d);
        return c * d;
    }


    @Advent(day = Day.Day_3, part = Part.two)
    public int day3Part2(String[] inputs) throws IOException {
        List<String> sets = new ArrayList<String>(Arrays.asList(inputs));
        final int bitSize = inputs[0].length();
        for (int i = 0; i < bitSize && sets.size() > 1; i++) {
            final int index = i;
            List<String> oneSet = sets.stream().filter(e -> e.charAt(index) == '1').collect(Collectors.toList());

            if (oneSet.size() >= sets.size() - oneSet.size()) {
                sets = oneSet;
            } else {
                sets.removeAll(oneSet);
            }
            ConsoleUtil.println(sets);
            ConsoleUtil.println(oneSet);
        }

        List<String> sets1 = new ArrayList<String>(Arrays.asList(inputs));
        for (int i = 0; i < bitSize && sets1.size() > 1; i++) {
            final int index = i;
            List<String> oneSet = sets1.stream().filter(e -> e.charAt(index) == '1').collect(Collectors.toList());

            if (oneSet.size() >= sets1.size() - oneSet.size()) {
                sets1.removeAll(oneSet);
            } else {
                sets1 = oneSet;
            }
            ConsoleUtil.println(sets1);
            ConsoleUtil.println(oneSet);
        }

        Integer a = Integer.parseInt(sets.get(0), 2);
        Integer b = Integer.parseInt(sets1.get(0), 2);
        ConsoleUtil.println(a);
        ConsoleUtil.println(b);
        Integer c = a * b;
        return c;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new DailyTasks());
    }
}
