import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class DailyTasks {
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


    private long day7Private(String[] inputs, BiFunction<Integer, Integer, Integer> cost) {
        // Brute force
        int[] arr = toInt(inputs[0].split(","));
        long min = Long.MAX_VALUE;
        int max = 0;
        for (int e : arr) {
            if (e > max) {
                max = e;
            }
        }
        for (int location = 0; location <= max; location++) {
            long sum = 0;
            for (int e : arr) {
                sum += cost.apply(e, location);
            }
            if (sum < min) {
                min = sum;
            }
        }
        return min;
    }

    @Advent(day = Day.Day_7)
    public long day7Part1(String[] inputs) throws IOException {
        return day7Private(inputs, (e, location) -> Math.abs(e - location));
    }


    @Advent(day = Day.Day_7, part = Part.two)
    public long day7Part2(String[] inputs) throws IOException {
        return day7Private(inputs, (e, location) -> {
            int n = Math.abs(e - location);
            return (n * (n + 1)) / 2;
        });
    }


    @Advent(day = Day.Day_8)
    public long day8Part1(String[] inputs) throws IOException {
        int unique = 0;
        for (String line : inputs) {
            String[] parts = line.split("\\|");
            String[] part2 = parts[1].trim().split(" ");
            for (String comb : part2) {
                //4 4
                //1 2
                //8 7
                //7 3
                int len = comb.length();
                if (len == 4 || len == 2 || len == 7 || len == 3) {
                    unique++;
                }
            }
        }
        return unique;
    }


    private Set<Character> getSet(String comb) {
        Set<Character> target = new HashSet();
        for (char c : comb.toCharArray()) {
            target.add(c);
        }
        return target;
    }

    @Advent(day = Day.Day_8, part = Part.two)
    public long day8Part2(String[] inputs) throws IOException {
        HashMap<Integer, Set<Character>> bags = new HashMap<>();
        long total = 0;
        for (String line : inputs) {
            String[] parts = line.split("\\|");
            String[] part1 = parts[0].trim().split(" ");

            Map<String, Integer> find = new HashMap<>();

            for (String comb : part1) {
                //4 4
                //1 2
                //8 7
                //7 3
                int len = comb.length();
                if (len == 4) {
                    bags.put(4, getSet(comb));
                    find.put(comb, 4);
                } else if (len == 2) {
                    bags.put(1, getSet(comb));
                    find.put(comb, 1);
                } else if (len == 7) {
                    bags.put(8, getSet(comb));
                    find.put(comb, 8);
                } else if (len == 3) {
                    bags.put(7, getSet(comb));
                    find.put(comb, 7);
                }
            }
            Set<Character> fo = new HashSet();
            fo.addAll(bags.get(4));
            fo.removeAll(bags.get(1));

            for (String comb : part1) {
                if (!find.containsKey(comb)) {
                    Set<Character> combSet = getSet(comb);
                    if (combSet.containsAll(bags.get(1))) {
                        if (comb.length() == 6) {
                            if (combSet.containsAll(bags.get(4))) {
                                bags.put(9, combSet);
                                find.put(comb, 9);
                            } else {
                                bags.put(0, combSet);
                                find.put(comb, 0);
                            }
                        } else {
                            bags.put(3, combSet);
                            find.put(comb, 3);
                        }
                    } else {
                        if (comb.length() == 6) {
                            bags.put(6, combSet);
                            find.put(comb, 6);
                        } else {
                            if (combSet.containsAll(fo)) {
                                bags.put(5, combSet);
                                find.put(comb, 5);
                            } else {
                                bags.put(2, combSet);
                                find.put(comb, 2);
                            }

                        }
                    }
                }
            }

            String[] part2 = parts[1].trim().split(" ");

            int number = 0;
            int b = 3;
            Integer num = 0;
            for (String comb : part2) {
                int len = comb.length();
                if (len == 4) {
                    num = 4;
                } else if (len == 2) {
                    num = 1;
                } else if (len == 7) {
                    num = 8;
                } else if (len == 3) {
                    num = 7;
                } else {
                    Set<Character> combSet = getSet(comb);
                    for (Map.Entry<Integer, ?> entry : bags.entrySet()) {
                        if (combSet.equals(entry.getValue())) {
                            num = entry.getKey();
                        }
                    }
                }
                System.out.print(num);
                number += num * Math.pow(10, b);
                b--;
            }
            System.out.println(" : " + number);

            total += number;
        }

        //4 4
        //1 2
        //8 7
        //7 3
        //acedgfb: 8
        /*cdfbe: 5
        gcdfa: 2
        fbcad: 3
        //dab: 7
        cefabd: 9
        cdfgeb: 6
        //eafb: 4
        cagedb: 0*/
        //ab: 1
        return total;

    }

    private boolean checkAdj(char[][] map, int i, int j) {
        if (i - 1 >= 0) {
            if (map[i - 1][j] <= map[i][j]) return false;
        }
        if (i + 1 < map.length) {
            if (map[i + 1][j] <= map[i][j]) return false;
        }
        if (j - 1 >= 0) {

            if (map[i][j - 1] <= map[i][j]) return false;
        }
        if (j + 1 < map[0].length) {
            if (map[i][j + 1] <= map[i][j]) return false;
        }
        return true;
    }

    @Advent(day = Day.Day_9, part = Part.one)
    public long day9Part1(String[] inputs) throws IOException {
        int r = 0;
        int len = inputs[0].length();
        int line = inputs.length;
        char[][] map = new char[line][len];
        for (int i = 0; i < inputs.length; i++) {
            map[i] = inputs[i].toCharArray();

        }
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < len; j++) {
                if (checkAdj(map, i, j)) {
                    r += (map[i][j] - 47);
                }
            }
        }
        return r;
    }

    private void basin(char[][] map, int i, int j, int[][] basin, int currentBasin,  Map<Integer, Integer>  basinBuckets) {
        if (basin[i][j] != 0) {
            return;
        }

        if (map[i][j] == 57) {
            basin[i][j] = -1;
            return;
        }

        basin[i][j] = 1;
        basinBuckets.put(currentBasin, basinBuckets.getOrDefault(currentBasin, 0) + 1);

        if (i - 1 >= 0) {
            if (map[i - 1][j] > map[i][j])
                basin(map, i - 1, j, basin, currentBasin, basinBuckets);
        }
        if (i + 1 < map.length) {
            if (map[i + 1][j] > map[i][j])
                basin(map, i + 1, j, basin, currentBasin, basinBuckets);
        }
        if (j - 1 >= 0) {
            if (map[i][j - 1] > map[i][j])
                basin(map, i, j - 1, basin, currentBasin, basinBuckets);
        }
        if (j + 1 < map[0].length) {
            if (map[i][j + 1] > map[i][j])
                basin(map, i, j + 1, basin, currentBasin, basinBuckets);
        }
    }

    @Advent(day = Day.Day_9, part = Part.two)
    public long day9Part2(String[] inputs) throws IOException {
        int len = inputs[0].length();
        int line = inputs.length;
        char[][] map = new char[line][len];
        for (int i = 0; i < inputs.length; i++) {
            map[i] = inputs[i].toCharArray();

        }
        int[][] basin = new int[line][len];
        Map<Integer, Integer> bucket = new HashMap<>();
        int currentBasin = 1;
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < len; j++) {
                if (checkAdj(map, i, j)) {
                    basin(map, i, j, basin, currentBasin, bucket);
                    currentBasin++;
                }
            }
        }

        return bucket.entrySet().stream().sorted((o1, o2) -> {
                if(o1.getValue() == o2.getValue()) return 0;
                return o1.getValue() < o2.getValue() ? 1: -1 ;
        }).limit(3).map(e-> e.getValue()).reduce(1,(a,b)-> a * b);
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new DailyTasks());
    }
}
