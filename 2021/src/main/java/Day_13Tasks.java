import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> fold = new ArrayList<>();


        int maxX = 0;
        int maxY = 0;
        for (String line : inputs) {
            if (!line.contains("fold") && !line.trim().isEmpty()) {
                int[] cor = toInt(line.split(","));
                if (cor[0] > maxX) {
                    maxX = cor[0];
                }
                if (cor[1] > maxY) {
                    maxY = cor[1];
                }
            }
        }

        char[][] paper = new char[maxY + 1][maxX + 1];


        for (int i = 0; i < inputs.length; i++) {
            String line = inputs[i];

            if (!line.contains("fold") && !line.trim().isEmpty()) {
                int[] cor = toInt(line.split(","));
                paper[cor[1]][cor[0]] = '#';
            } else if (line.contains("fold"))
                fold.add(line);
        }

        String[] cmd = fold.get(0).replace("fold along", "").trim().split("=");
        if (cmd[0].equals("x")) {
            int x = Integer.parseInt(cmd[1]);

            for (int i = 1; i + x <= maxX && x - i >= 0; i++) {
                for (int j = 0; j <= maxY; j++) {
                    if (paper[j][x + i] == '#')
                        paper[j][x - i] = paper[j][x + i];
                }

            }

            maxX = x - 1;

        } else {
            int y = Integer.parseInt(cmd[1]);

            for (int i = 1; i + y <= maxY && y - i >= 0; i++) {
                for (int j = 0; j <= maxX; j++) {
                    if (paper[y + i][j] == '#')
                        paper[y - i][j] = paper[y + i][j];
                }

            }

            maxY = y - 1;
        }

        int count = 0;
        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {
                if (paper[i][j] == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    @Advent(day = Day.Day_13, part = Part.two)
    public int Day_13Part2(String[] inputs) throws IOException {
        List<String> fold = new ArrayList<>();


        int maxX = 0;
        int maxY = 0;
        for (String line : inputs) {
            if (!line.contains("fold") && !line.trim().isEmpty()) {
                int[] cor = toInt(line.split(","));
                if (cor[0] > maxX) {
                    maxX = cor[0];
                }
                if (cor[1] > maxY) {
                    maxY = cor[1];
                }
            }
        }

        char[][] paper = new char[maxY + 1][maxX + 1];


        for (int i = 0; i < inputs.length; i++) {
            String line = inputs[i];

            if (!line.contains("fold") && !line.trim().isEmpty()) {
                int[] cor = toInt(line.split(","));
                paper[cor[1]][cor[0]] = '#';
            } else if (line.contains("fold"))
                fold.add(line);
        }

        for(String foldStr: fold) {
            String[] cmd = foldStr.replace("fold along", "").trim().split("=");
            if (cmd[0].equals("x")) {
                int x = Integer.parseInt(cmd[1]);

                for (int i = 1; i + x <= maxX && x - i >= 0; i++) {
                    for (int j = 0; j <= maxY; j++) {
                        if (paper[j][x + i] == '#')
                            paper[j][x - i] = paper[j][x + i];
                    }

                }

                maxX = x - 1;

            } else {
                int y = Integer.parseInt(cmd[1]);

                for (int i = 1; i + y <= maxY && y - i >= 0; i++) {
                    for (int j = 0; j <= maxX; j++) {
                        if (paper[y + i][j] == '#')
                            paper[y - i][j] = paper[y + i][j];
                    }

                }

                maxY = y - 1;
            }


        }

        int count = 0;
        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {
                if(paper[i][j]=='#')
                    System.out.print(paper[i][j]);
                else
                    System.out.print(".");
            }
            System.out.println("");
        }

        /**
         * Run with Day_13.txt
         * ####.#..#...##.#..#..##..####.#..#.###..
         * ...#.#..#....#.#..#.#..#.#....#..#.#..#.
         * ..#..#..#....#.#..#.#..#.###..####.#..#.
         * .#...#..#....#.#..#.####.#....#..#.###..
         * #....#..#.#..#.#..#.#..#.#....#..#.#....
         * ####..##...##...##..#..#.#....#..#.#....
         *
         */
        return count;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_13Tasks());
    }
}
