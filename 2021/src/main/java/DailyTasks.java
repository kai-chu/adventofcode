import java.io.IOException;
import java.util.*;
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

    static class Board {
        int[] ele = new int[25]; // board
        Map<Integer, Integer> index = new HashMap<>();
        boolean[] marked = new boolean[25];
        int sum = 0;
        int last;
        int currSum;

        int row(int index) {
            return index / 5;
        }

        int col(int index) {
            return index % 5;
        }

        int index(int row, int col) {
            return row * 5 + col;
        }


        public Board(String[] arr) {
            int i = 0;
            for (String line : arr) {
                for (String e : line.trim().split("\\s+")) {
                    Integer int_e = Integer.parseInt(e);
                    ele[i] = int_e;
                    index.put(int_e, i);
                    sum += int_e;
                    i++;
                }
            }
            currSum = sum;
        }

        public boolean checkRow(int index) {
            int row = row(index);
            for (int col = 0; col < 5; col++) {
                if (!marked[index(row, col)]) {
                    return false;
                }
            }
            return true;
        }

        public boolean checkCol(int index) {
            int col = col(index);
            for (int row = 0; row < 5; row++) {
                if (!marked[index(row, col)]) {
                    return false;
                }
            }
            return true;
        }

        public boolean mark(int target) {
            last = target;
            Integer targetIndex = index.get(target);
            if (targetIndex != null) {
                if (!marked[targetIndex]) {
                    currSum = currSum - target;
                    marked[targetIndex] = true;
                    if (checkRow(targetIndex)) {
                        return true;
                    }
                    if (checkCol(targetIndex)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public int getResult() {
            return last * currSum;
        }

        public void sum() {
            int sum = 0;
            for (int i = 0; i < 25; i++) {
                if (!marked[i]) {
                    sum += ele[i];
                }
            }
            ConsoleUtil.println(sum);
        }

        public static List<Board> readBoards(String[] inputs) {
            List<Board> boards = new ArrayList<>();

            for (int i = 1; i < inputs.length; i += 6) {
                String[] boardArr = new String[5];
                for (int board = 1; board < 6; board++) {
                    boardArr[board - 1] = inputs[i + board];
                }
                boards.add(new Board(boardArr));
            }
            return boards;
        }

    }

    @Advent(day = Day.Day_4)
    public int day4Part1(String[] inputs) throws IOException {
        int[] int_inputs = Arrays.stream(inputs[0].split(",")).mapToInt(Integer::parseInt).toArray();

        List<Board> boards = Board.readBoards(inputs);

        for (int e : int_inputs) {
            for (Board board : boards) {
                if (board.mark(e)) {
                    return board.getResult();
                }
            }
        }

        return 0;
    }

    @Advent(day = Day.Day_4, part = Part.two)
    public int day4Part2(String[] inputs) throws IOException {
        int[] int_inputs = Arrays.stream(inputs[0].split(",")).mapToInt(Integer::parseInt).toArray();

        List<Board> boards = Board.readBoards(inputs);
        Set<Board> wins = new HashSet<>();
        Board lastWin = null;
        for (int e : int_inputs) {
            for (Board board : boards) {
                if (!wins.contains(board)) {
                    if (board.mark(e)) {
                        wins.add(board);
                        lastWin = board;
                    }
                }
            }
        }

        return lastWin.getResult();
    }

    @Advent(day = Day.Day_5, part = Part.one)
    public int day5Part1(String[] inputs) throws IOException {
        int[][] area = new int[1000][1000];
        int count = 0;
        for (String line : inputs) {
            String[] cor = line.split("->");
            int[] start = Arrays.stream(cor[0].trim().split(",")).mapToInt(Integer::parseInt).toArray();
            int[] end = Arrays.stream(cor[1].trim().split(",")).mapToInt(Integer::parseInt).toArray();

            if (start[0] == end[0]) {
                int max = Math.max(start[1], end[1]);
                int min = Math.min(start[1], end[1]);
                for (int i = min; i <= max; i++) {
                    area[start[0]][i]++;

                    if (area[start[0]][i] == 2) {
                        count++;
                    }
                }
            } else if (start[1] == end[1]) {
                int max = Math.max(start[0], end[0]);
                int min = Math.min(start[0], end[0]);
                for (int i = min; i <= max; i++) {
                    area[i][start[1]]++;

                    if (area[i][start[1]] == 2) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    @Advent(day = Day.Day_5, part = Part.two)
    public int day5Part2(String[] inputs) throws IOException {
        int[][] area = new int[1000][1000];
        int count = 0;
        for (String line : inputs) {
            String[] cor = line.split("->");
            int[] start = Arrays.stream(cor[0].trim().split(",")).mapToInt(Integer::parseInt).toArray();
            int[] end = Arrays.stream(cor[1].trim().split(",")).mapToInt(Integer::parseInt).toArray();

            if (start[0] == end[0]) {
                int max = Math.max(start[1], end[1]);
                int min = Math.min(start[1], end[1]);
                for (int i = min; i <= max; i++) {
                    area[start[0]][i]++;

                    if (area[start[0]][i] == 2) {
                        count++;
                    }
                }
            } else if (start[1] == end[1]) {
                int max = Math.max(start[0], end[0]);
                int min = Math.min(start[0], end[0]);
                for (int i = min; i <= max; i++) {
                    area[i][start[1]]++;

                    if (area[i][start[1]] == 2) {
                        count++;
                    }
                }
            } else if (Math.abs(start[0] - end[0]) == Math.abs(start[1] - end[1])) {
                int[] left = start[0] < end[0] ? start : end;
                int[] right = start[0] > end[0] ? start : end;

                for (int i = 0; i <= right[0] - left[0]; i++) {
                    int col = 0;
                    if (left[1] > right[1]) {
                        col = left[1] - i;
                    } else {
                        col = left[1] + i;
                    }
                    area[i + left[0]][col]++;
                    if (area[i + left[0]][col] == 2) {
                        count++;
                    }
                }
            }
        }

        return count;
    }


    private long day6(String[] inputs, int days) {
        long[] daysRemain = new long[9];
        for (int i = 0; i < daysRemain.length; i++) daysRemain[i] = 0;

        for (int e : Arrays.stream(inputs[0].split(",")).mapToInt(Integer::parseInt).toArray()) {
            daysRemain[e]++;
        }

        while (days > 0) {
            long preZeroDays = daysRemain[0];

            for (int i = 1; i < daysRemain.length; i++) {
                daysRemain[i - 1] = daysRemain[i];
            }

            daysRemain[6] += preZeroDays;
            daysRemain[8] = preZeroDays;

            days--;
        }

        return Arrays.stream(daysRemain).sum();
    }

    @Advent(day = Day.Day_6)
    public long day6Part1(String[] inputs) throws IOException {
        return day6(inputs, 80);
    }


    @Advent(day = Day.Day_6, part = Part.two)
    public long day6Part2(String[] inputs) throws IOException {
        return day6(inputs, 256);
    }

    @Advent(day = Day.Day_7)
    public long day7Part1(String[] inputs) throws IOException {
        // Brute force
        int [] arr = Arrays.stream(inputs[0].split(",")).mapToInt(Integer::parseInt).toArray();
        long min = Long.MAX_VALUE;
        int max = 0;
        for(int e: arr) {
            if(e > max) {
                max = e;
            }
        }
        for(int step = 0; step <= max; step ++) {
            long sum = 0;
            for(int e: arr) {
                sum += Math.abs(e - step);
            }
            if(sum < min) {
                min = sum;
            }
        }
        return min;
    }


    @Advent(day = Day.Day_7, part = Part.two)
    public long day7Part2(String[] inputs) throws IOException {
        int [] arr = Arrays.stream(inputs[0].split(",")).mapToInt(Integer::parseInt).toArray();

        long min = Long.MAX_VALUE;
        int max = 0;
        for(int e: arr) {
            if(e > max) {
                max = e;
            }
        }
        for(int step = 0; step <= max; step ++) {
            long sum = 0;
            for(int e: arr) {
                long n = Math.abs(e - step);
                sum += (n * (n + 1)) / 2;
            }
            if(sum < min) {
                min = sum;
            }
        }
        return min;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new DailyTasks());
    }
}
