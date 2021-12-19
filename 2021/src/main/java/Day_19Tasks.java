import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * x y z scanner
 * beacon o p q,  relative to scanner (o - x, p - y, q - z)
 * detected at most 1000
 * overlap beacons, in 2d, we can calculate each position
 * x right,left, y, up, down,  shift,
 * take x,y pair from A, take x1, y1 from B, calculate the shift = (x1 - x, y1-y)
 * -2 , 1 - -5, -2 = 3, 3 xi, yi - shift = pari in A
 * <p>
 * x rotate, x,y,z -> x, z, -y 90, x, -y, z 180, x, -z, y 270
 * y rotate  x,y,z -> z, y, -x 90, -x, y, z 180, -z, y, x 270
 * <p>
 * first we need to know the direction of two scanner
 * we cannot really know this, we need to brute force to check each scanner   24^2 hard to calculate
 * x,y,z direction(x,y,z)
 * aligning the direction
 * rotates x,y,z rotate(x,y,z, direction)
 * check overlap
 * shift to same coordinates on each location, and check if others are aligning,
 * take 1 from B
 * shift = B - A
 * calcualate the reset if they are the same divide into groups
 */
public class Day_19Tasks {
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

    class Location {
        int x, y, z;

        public Location(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Location(int[] arr) {
            this.x = arr[0];
            this.y = arr[1];
            this.z = arr[2];
        }

        public Location shift(Location l) {
            return new Location(this.x - l.x, this.y - l.y, this.z - l.z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Location location = (Location) o;
            return x == location.x &&
                    y == location.y &&
                    z == location.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return x + "," + y + "," + z;
        }

        public int distance(Location another) {
            return Math.abs(x - another.x) + Math.abs(y - another.y) + Math.abs(z - another.z);
        }

    }

    Map<Integer, Function<Location, Location>> directionOps = new HashMap<>();

    private void setDirectionOps() {
        int i = 0;
        for (int factor : new int[]{1, -1}) {
            for (int factor1 : new int[]{1, -1}) {
                for (int factor2 : new int[]{1, -1}) {
                    directionOps.put(i++, location -> new Location(factor * location.x, factor1 * location.y, factor2 * location.z));
                    directionOps.put(i++, location -> new Location(factor * location.x, factor1 * location.z, factor2 * location.y));
                    directionOps.put(i++, location -> new Location(factor * location.y, factor1 * location.x, factor2 * location.z));
                    directionOps.put(i++, location -> new Location(factor * location.y, factor1 * location.z, factor2 * location.x));
                    directionOps.put(i++, location -> new Location(factor * location.z, factor1 * location.x, factor2 * location.y));
                    directionOps.put(i++, location -> new Location(factor * location.z, factor1 * location.y, factor2 * location.x));
                }
            }
        }
    }

    private AbstractMap.SimpleEntry<Location, Set<Location>> findCommon(List<Location> scanner1, List<Location> scanner2, Function<Location, Location> ops) {
        Map<Location, Set<Location>> shifts = new HashMap<>();
        Location matchShift = null;
        for (int i = 0; i < scanner1.size(); i++) {
            for (int j = 0; j < scanner2.size(); j++) {
                Location shift = ops.apply(scanner2.get(j)).shift(scanner1.get(i));

                //System.out.println(shift);
                Set<Location> sets = shifts.getOrDefault(shift, new HashSet<>());
                sets.add(scanner2.get(j));
                shifts.put(shift, sets);

                if (sets.size() >= 12) {
                    matchShift = shift;
                }
            }
        }

        if (matchShift == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<Location, Set<Location>>(matchShift, shifts.get(matchShift));
    }

    private Map.Entry<Location, List<Location>> align(List<Location> scanner1, List<Location> scanner2) {
        Map.Entry<Location, Set<Location>> entry = null;
        int k = -1;
        for (int i = 0; i < directionOps.size(); i++) {
            entry = findCommon(scanner1, scanner2, directionOps.get(i));

            if (entry != null) {
                k = i;
                break;
            }
        }

        if (entry != null) {
            List<Location> adjusted = new ArrayList<>();
            final int m = k;
            for (Location e : scanner2) {
                adjusted.add(directionOps.get(m).apply(e).shift(entry.getKey()));
            }

            return new AbstractMap.SimpleEntry<>(entry.getKey(), adjusted);
        }

        return null;
    }

    @Advent(day = Day.Day_19, part = Part.one)
    public int Day_19Part1(String[] inputs) throws IOException {
        setDirectionOps();

        List<List<Location>> scanners = new ArrayList<>();
        for (String line : inputs) {
            if (line.contains("scanner")) {
                scanners.add(new ArrayList<>());
            } else if (!line.trim().isEmpty()) {
                int[] location = toInt(line.split(","));
                scanners.get(scanners.size() - 1).add(new Location(location));
            }
        }
        //for(int i=0;i<scanners.size();i++) {
        //System.out.println("Scanner " + i + ", size:" + scanners.get(i).size());
        Queue<Integer> adjusting = new LinkedList<>();
        adjusting.add(0);
        Set<Integer> done = new HashSet<>();

        Set<Location> unique = new HashSet<>(scanners.get(0));

        while (!adjusting.isEmpty()) {
            Integer head = adjusting.poll();
            done.add(head);

            for (int j = 1; j < scanners.size(); j++) {

                if (!done.contains(j) && !adjusting.contains(j)) {
                    // align second scanner with the first
                    Map.Entry<Location, List<Location>> rotated = align(scanners.get(head), scanners.get(j));
                    if (rotated != null) {
                        System.out.println(j + " overlap with " + head);
                        scanners.set(j, rotated.getValue());
                        unique.addAll(rotated.getValue());
                        adjusting.add(j);
                    }
                }
            }
        }

        return unique.size();
    }

    @Advent(day = Day.Day_19, part = Part.two)
    public int Day_19Part2(String[] inputs) throws IOException {
        setDirectionOps();

        List<List<Location>> scanners = new ArrayList<>();
        for (String line : inputs) {
            if (line.contains("scanner")) {
                scanners.add(new ArrayList<>());
            } else if (!line.trim().isEmpty()) {
                int[] location = toInt(line.split(","));
                scanners.get(scanners.size() - 1).add(new Location(location));
            }
        }

        Queue<Integer> adjusting = new LinkedList<>();
        adjusting.add(0);
        Set<Integer> done = new HashSet<>();

        List<Location> unique = new ArrayList();
        unique.add(new Location(0, 0, 0));

        while (!adjusting.isEmpty()) {
            Integer head = adjusting.poll();
            done.add(head);

            for (int j = 1; j < scanners.size(); j++) {
                if (!done.contains(j) && !adjusting.contains(j)) {
                    // align second scanner with the first
                    Map.Entry<Location, List<Location>> rotated = align(scanners.get(head), scanners.get(j));
                    if (rotated != null) {
                        scanners.set(j, rotated.getValue());
                        unique.add(rotated.getKey());
                        adjusting.add(j);
                    }
                }
            }
        }

        System.out.print("adjusted " + done.size());
        int max = 0;
        for (int i = 0; i < unique.size(); i++) {
            for (int j = i + 1; j < unique.size(); j++) {
                max = Math.max(max, unique.get(i).distance(unique.get(j)));
            }
        }

        return max;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_19Tasks());
    }
}
