import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day_20Tasks {
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

    private int originPixIndex(List<StringBuilder> origin, int i, int j, int mod) {
        int k = 0;
        int[] shifts = {-1, 0, 1};
        for (int x : shifts) {
            for (int y : shifts) {
                if (x + i >= 0 && j + y >= 0 && x + i < origin.size() && j + y < origin.get(0).length()) {
                    if (origin.get(x + i).charAt(y + j) == '#') {
                        k = (k << 1) | 1;
                    } else {
                        k <<= 1;
                    }
                } else {
                    k = (k << 1) | mod;
                }
            }
        }
        return k;
    }

    @Advent(day = Day.Day_20, part = Part.one)
    public long Day_20Part1(String[] inputs) throws IOException {
        List<Character> map = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<StringBuilder> image = new ArrayList<>();
        int imageLineNum = 0;
        boolean imageMode = false;
        for (String line : inputs) {
            if (line.trim().isEmpty()) {
                imageMode = true;
            } else if (imageMode) {
                image.add(new StringBuilder());
                image.get(imageLineNum++).append(line);
            } else {
                sb.append(line);
            }
        }

        List<StringBuilder> swap = new ArrayList<>();

        long count = 0;

        /**
         * 1 , 0
         * 2, charAt(0)
         * 3, prev == 1 ? charAt(511) : 0
         * 4, prev == 1 ? charAt(511) : 0;
         */
        int mod = 0;
        int mod2 = sb.charAt(0) == '.' ? 0 : 1;

        System.out.println(mod + "," + mod2);

        for (int i = -1; i <= imageLineNum; i++) {
            StringBuilder imageLine = new StringBuilder();
            swap.add(imageLine);
            for (int j = -1; j <= image.get(0).length(); j++) {
                int index = originPixIndex(image, i, j, mod);
                imageLine.append(sb.charAt(index));
            }
        }


        for (int i = -1; i <= swap.size(); i++) {
            for (int j = -1; j <= swap.get(0).length(); j++) {
                int index = originPixIndex(swap, i, j, mod2);
                if (sb.charAt(index) == '#') {
                    count++;
                }
                System.out.print(sb.charAt(index));
            }
            System.out.println("");
        }


        return count;
    }

    @Advent(day = Day.Day_20, part = Part.two)
    public long Day_20Part2(String[] inputs) throws IOException {
        List<Character> map = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<StringBuilder> image = new ArrayList<>();
        int imageLineNum = 0;
        boolean imageMode = false;
        for (String line : inputs) {
            if (line.trim().isEmpty()) {
                imageMode = true;
            } else if (imageMode) {
                image.add(new StringBuilder());
                image.get(imageLineNum++).append(line);
            } else {
                sb.append(line);
            }
        }


        long count = 0;

        /**
         * 1 , 0
         * 2, charAt(0)
         * 3, prev == 1 ? charAt(511) : 0
         * 4, prev == 1 ? charAt(511) : 0;
         */

        int total = 50;
        int mod = 0;

        List<StringBuilder> swap = null;
        for (int c = 1; c <= total; c++) {

            swap = new ArrayList<>();
            for (int i = -1; i <= image.size(); i++) {
                StringBuilder imageLine = new StringBuilder();
                swap.add(imageLine);
                for (int j = -1; j <= image.get(0).length(); j++) {
                    int index = originPixIndex(image, i, j, mod);
                    imageLine.append(sb.charAt(index));
                }
            }

            image = swap;

            mod = (mod == 1 ? (sb.charAt(511) == '#' ? 1 : 0) : (sb.charAt(0) == '#' ? 1 : 0));
        }


        for (int i = 0; i < swap.size(); i++) {
            for (int j = 0; j < swap.get(0).length(); j++) {
                if (swap.get(i).charAt(j) == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_20Tasks());
    }
}
