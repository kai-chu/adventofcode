import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day_16Tasks {
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

    private String toBinary(String hex) {

        // variable to store the converted
        // Binary Sequence
        String binary = "";

        // converting the accepted Hexadecimal
        // string to upper case
        hex = hex.toUpperCase();

        // initializing the HashMap class
        HashMap<Character, String> hashMap
                = new HashMap<Character, String>();

        // storing the key value pairs
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");

        int i;
        char ch;

        // loop to iterate through the length
        // of the Hexadecimal String
        for (i = 0; i < hex.length(); i++) {
            // extracting each character
            ch = hex.charAt(i);

            // checking if the character is
            // present in the keys
            if (hashMap.containsKey(ch))

                // adding to the Binary Sequence
                // the corresponding value of
                // the key
                binary += hashMap.get(ch);

                // returning Invalid Hexadecimal
                // String if the character is
                // not present in the keys
            else {
                binary = "Invalid Hexadecimal String";
                return binary;
            }
        }

        // returning the converted Binary
        return binary;
    }

    class Node {
        int endIndex;
        long value;

        public Node(int endIndex, long value) {
            this.endIndex = endIndex;
            this.value = value;
        }

    }

    private long cal(List<Node> nodes, int type) {
        switch (type) {
            case 0:
                return nodes.stream().map(e -> e.value).reduce(0l, (a, b) -> (a + b));
            case 1:
                return nodes.stream().map(e -> e.value).reduce(1l, (a, b) -> (a * b));
            case 2:
                return nodes.stream().mapToLong(e -> e.value).min().getAsLong();
            case 3:
                return nodes.stream().mapToLong(e -> e.value).max().getAsLong();
            case 5:
                return nodes.get(0).value > nodes.get(1).value ? 1l : 0l;
            case 6:
                return nodes.get(0).value < nodes.get(1).value ? 1l : 0l;
            case 7:
                return nodes.get(0).value == nodes.get(1).value ? 1l : 0l;
            default:
                ConsoleUtil.println("wrong data");
        }
        return -1;
    }

    private Node parse(String bl, int start, int[] versions) {
        if (start >= bl.length() - 6) new Node(bl.length(), -1);

        int version = Integer.parseInt(bl.substring(start, start + 3), 2);
        int id = Integer.parseInt(bl.substring(start + 3, start + 6), 2);

        System.out.println(start + ", v " + version + "," + id);
        versions[version]++;


        if (id == 4) {
            ConsoleUtil.println("literal");
            int i = start + 6;
            StringBuilder sb = new StringBuilder();
            boolean last = false;
            while (!last && i <= bl.length() - 5) {
                if (bl.charAt(i) == '0') {
                    last = true;
                }
                sb.append(bl, i + 1, i + 5);
                i += 5;
            }
            long value = Long.parseLong(sb.toString(), 2);
            return new Node(i - 1, value);
        } else {
            char label = bl.charAt(start + 6);
            int i = start + 7;

            if (label == '0') {
                //15
                if (i + 15 > bl.length()) return new Node(bl.length(), -1);

                String su = bl.substring(i, i + 15);
                ConsoleUtil.println(su);

                int len = Integer.parseInt(su, 2);
                ConsoleUtil.println("type " + id + " len" + len);
                i += 15;
                int k = i + len;

                List<Node> subNodes = new ArrayList<>();
                while (i != k && i < bl.length()) {
                    Node node = parse(bl, i, versions);
                    if(node.endIndex < bl.length())
                        subNodes.add(node);
                    i = node.endIndex + 1;
                }

                long value = cal(subNodes, id);

                return new Node(i - 1, value);
            } else if (label == '1') {
                if (i + 11 > bl.length()) new Node(bl.length(), -1);

                int num = Integer.parseInt(bl.substring(i, i + 11), 2);
                ConsoleUtil.println("type 1 num:" + num);
                i += 11;

                List<Node> subNodes = new ArrayList<>();
                while (num > 0 && i < bl.length()) {
                    Node node = parse(bl, i, versions);
                    if(node.endIndex < bl.length())
                        subNodes.add(node);
                    i = node.endIndex + 1;
                    num--;
                }
                long value = cal(subNodes, id);
                return new Node(i - 1, value);
            }
        }

        return new Node(bl.length(), -1);
    }

    @Advent(day = Day.Day_16, part = Part.one)
    public int Day_16Part1(String[] inputs) throws IOException {
        String line = inputs[0];
        String bl = toBinary(line);
        ConsoleUtil.println(bl);

        int[] versions = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        parse(bl, 0, versions);

        int total = 0;
        for (int i = 1; i < versions.length; i++) {
            total += i * versions[i];
        }
        for (int e : versions) {
            System.out.println(e);
        }
        return total;
    }

    @Advent(day = Day.Day_16, part = Part.two)
    public long Day_16Part2(String[] inputs) throws IOException {
        String line = inputs[0];
        String bl = toBinary(line);
        ConsoleUtil.println(bl);

        int[] versions = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        Node node = parse(bl, 0, versions);
        return node.value;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_16Tasks());
    }
}
