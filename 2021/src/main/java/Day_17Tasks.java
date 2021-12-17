import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Day_17Tasks {
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

    private int[] parseCoordination(String line) {
        return Arrays.stream(line.trim().split("=")[1].split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
    }

    private int distance(int x, int k) {
        return k * x - k * (k - 1) / 2;
    }

    interface StateCal extends Consumer<Pair>, Supplier<Integer> {

    }


    class Pair {
        int a;
        int b;

        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }


    private int allPair(String[] inputs, StateCal cal) {
        String target[] = inputs[0].split(":")[1].split(",");

        int[] targetX = parseCoordination(target[0]);
        int[] targetY = parseCoordination(target[1]);

        Set<Pair> steps = new HashSet<>();

        for (int x = 1; x <= targetX[1]; x++) {
            for (int k = 1; k <= x + 1; k++) { // when k = x+1, velocity on x is 0
                int xk = distance(x, k);
                if (xk > targetX[1]) {
                    break;
                } else if (xk >= targetX[0]) {
                    steps.add(new Pair(x, k));
                }
            }
        }

        int low = targetY[0];
        int high = targetY[1];

        for (Pair p : steps) {
            int k = p.b;
            int x = p.a;
            for (int y = low; y <= -low; y++) {
                int yk = distance(y, k);
                if (k == x + 1) { // Vx = 0, we can continue fall with gravity until in the area
                    int tk = k;
                    while (yk > high) {
                        tk++;
                        yk = distance(y, tk);
                    }
                }

                if (yk < low) {
                    continue; // cross the area
                } else if (yk <= high) { // within the area, we should fall down more as well to make sure next one is in the area as well
                    cal.accept(new Pair(x, y));
                }
            }
        }
        return cal.get();
    }

    @Advent(day = Day.Day_17, part = Part.one)
    public int Day_17Part1(String[] inputs) throws IOException {
        return allPair(inputs, new StateCal() {

            int maxY = 0;

            @Override
            public void accept(Pair pair) {
                final int i = distance(pair.b, pair.b + 1);// when k = y+1, Vy = 0
                maxY = Math.max(maxY, i);
            }

            @Override
            public Integer get() {
                return maxY;
            }
        });
    }

    @Advent(day = Day.Day_17, part = Part.two)
    public int Day_17Part2(String[] inputs) throws IOException {
        return allPair(inputs, new StateCal() {

            int maxY = 0;
            int count = 0;
            boolean xSet[][] = new boolean[150][500];

            @Override
            public void accept(Pair pair) {
                if (!xSet[pair.a][pair.b + 160]) {
                    count++;
                    xSet[pair.a][pair.b + 160] = true;
                }
            }

            @Override
            public Integer get() {
                return count;
            }
        });
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_17Tasks());
    }

    /**
     * x, y
     * step k
     * (0 + k-1) * k / 2
     * after k step, xk = 0 + (x + x-1 + x-2 + x - (k-1)) = kx - (k-1) * k / 2
     * after k step, yk = ky - (k-1) * k / 2
     *
     * target:
     *  xmin < xk  < xmax, ymin < yk < ymax
     *
     * optimize: yk to largest
     *      . given all k which make x fall into scope, calculate y
     *
     *      test all x, find k, 1 <= k <= x, 1 <= x <= xmax
     *      kx - 1/2 * k^2 = m
     *
     *      tesk if any k make yk follow into the scope and find the larget y
     *      -ymax <= y <= ymax
     *
     *
     *
     */
}
