import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day_14Tasks {
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

    static class Node {
        char value;
        Node next;

        public Node() {
        }

        public Node(char value) {
            this.value = value;
        }

    }

    @Advent(day = Day.Day_14, part = Part.one)
    public int Day_14Part1(String[] inputs) throws IOException {
        Node head = new Node();
        Node curr = head;
        Map<Character, Integer> countMap = new HashMap<>();

        for (char c : inputs[0].toCharArray()) {
            curr.next = new Node(c);
            countMap.put(c, countMap.getOrDefault(c, 0) + 1);
            curr = curr.next;
        }

        Map<String, Character> ops = new HashMap<>();
        for (int i = 2; i < inputs.length; i++) {
            String[] opsArr = inputs[i].split("->");
            ops.put(opsArr[0].trim(), opsArr[1].trim().charAt(0));
            // System.out.println(opsArr[0]);
        }

        int step = 40;
        for (int i = 1; i <= step; i++) {
            curr = head.next;
            while (curr.next != null) {
                String pair = new String(new char[]{curr.value, curr.next.value});

                Character c = ops.get(pair);
                if (c != null) {
                    countMap.put(c, countMap.getOrDefault(c, 0) + 1);
                    Node iNode = new Node(c);
                    iNode.next = curr.next;
                    curr.next = iNode;
                    curr = curr.next;
                }
                curr = curr.next;
            }
        }

        List<Map.Entry<Character, Integer>> l = countMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
        /*curr = head.next;
        while(curr != null) {
            System.out.print(curr.value);
            curr = curr.next;
        }

        System.out.println("");*/

        return l.get(l.size() - 1).getValue() - l.get(0).getValue();
    }

    @Advent(day = Day.Day_14, part = Part.two)
    public long Day_14Part2(String[] inputs) throws IOException {
        Map<Character, Long> countMap = new HashMap<>();
        Map<String, Long> countPairMap = new HashMap<>();
        long max = 0;
        long min = Long.MAX_VALUE;

        String target = inputs[0];
        countMap.put(target.charAt(0), 1l);
        for (int i = 1; i < target.length(); i++) {
            String pair = new String(new char[]{target.charAt(i - 1), target.charAt(i)});
            countPairMap.compute(pair, (k, v) -> (v == null ? 1 : v + 1));
            Long update = countMap.compute(target.charAt(i), (k, v) -> (v == null ? 1 : v + 1));

            if(update > max) max = update;
            if(update < min) min = update;
        }

        Map<String, Character> ops = new HashMap<>();
        for (int i = 2; i < inputs.length; i++) {
            String[] opsArr = inputs[i].split("->");
            ops.put(opsArr[0].trim(), opsArr[1].trim().charAt(0));
        }

        int step = 40;
        for (int i = 1; i <= step; i++) {
            Map<String, Long> tmpPairMap = new HashMap<>(countPairMap);
            Iterator<Map.Entry<String, Long>> it = tmpPairMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Long> entry = it.next();

                Character insertChar = ops.get(entry.getKey());
                if (insertChar != null) {
                    // add new pair to countPair
                    String pairOne = new String(new char[]{entry.getKey().charAt(0), insertChar});
                    String pairTwo = new String(new char[]{insertChar, entry.getKey().charAt(1)});
                    // unpair it
                    Long leftCount = countPairMap.get(entry.getKey()) - entry.getValue();
                    countPairMap.compute(entry.getKey(), (k, v) -> leftCount == 0l ? null : leftCount);

                    countPairMap.compute(pairOne, (k, v) -> (v == null ? entry.getValue() : v + entry.getValue()));
                    countPairMap.compute(pairTwo, (k, v) -> (v == null ? entry.getValue() : v + entry.getValue()));
                    Long update = countMap.compute(insertChar, (k, v) -> (v == null ? entry.getValue() : v + entry.getValue()));

                    if(update > max) max = update;
                    if(update < min) min = update;
                }
            }

        }

        //List<Map.Entry<Character, Long>> l = countMap.entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getValue)).collect(Collectors.toList());
        /*curr = head.next;
        while(curr != null) {
            System.out.print(curr.value);
            curr = curr.next;
        }

        System.out.println("");*/

        return max - min;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_14Tasks());
    }
}
