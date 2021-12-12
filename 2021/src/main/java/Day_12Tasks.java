import java.io.IOException;
import java.util.*;

public class Day_12Tasks {
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

    class Graph {
        private Map<String, LinkedHashSet<String>> map = new HashMap();

        public void addEdge(String node1, String node2) {
            LinkedHashSet<String> adjacent = map.get(node1);
            if (adjacent == null) {
                adjacent = new LinkedHashSet();
                map.put(node1, adjacent);
            }
            adjacent.add(node2);
        }

        public void addTwoWayVertex(String node1, String node2) {
            addEdge(node1, node2);
            addEdge(node2, node1);
        }

        public boolean isConnected(String node1, String node2) {
            Set adjacent = map.get(node1);
            if (adjacent == null) {
                return false;
            }
            return adjacent.contains(node2);
        }

        public LinkedList<String> adjacentNodes(String last) {
            LinkedHashSet<String> adjacent = map.get(last);
            if (adjacent == null) {
                return new LinkedList();
            }
            return new LinkedList<String>(adjacent);
        }
    }

    private final String END = "end";
    private final String START = "start";

    private int depthFirst(Graph graph, LinkedList<String> visited) {
        String curr = visited.getLast();
        // examine adjacent nodes
        //for (String node : nodes) {
        //if (visited.contains(node)) {
        //   continue;
        //}
        int count = 0;

        if (curr.equals(END)) {
            printPath(visited);

            //visited.removeLast();
            return 1;
        }
        //}

        LinkedList<String> nodes = graph.adjacentNodes(curr);
        for (String node : nodes) {
            if (node.equals(START)) continue;

            if (!node.toUpperCase().equals(node) && visited.contains(node)) {
                continue;
            }
            visited.addLast(node);
            count += depthFirst(graph, visited);
            visited.removeLast();
        }

        return count;
    }

    private boolean allowAnyVisit(String node) {
        return node.toUpperCase().equals(node);
    }

    private int depthFirst2(Graph graph, LinkedList<String> visited, boolean twice) {
        String curr = visited.getLast();
        int count = 0;

        if (curr.equals(END)) {
            printPath(visited);
            return 1;
        }
        //}

        LinkedList<String> nodes = graph.adjacentNodes(curr);
        for (String node : nodes) {
            if (node.equals(START)) continue;
            boolean nodeTwice = twice;
            if (!allowAnyVisit(node) && visited.contains(node)) {
                if (twice)
                    continue;
                else
                    nodeTwice = true;
            }
            visited.addLast(node);
            count += depthFirst2(graph, visited, nodeTwice);
            visited.removeLast();
        }

        return count;
    }

    private void printPath(LinkedList<String> visited) {
        for (String node : visited) {
            System.out.print(node);
            System.out.print(" ");
        }
        System.out.println();
    }

    @Advent(day = Day.Day_12, part = Part.one)
    public int Day_12Part1(String[] inputs) throws IOException {
        Graph graph = new Graph();
        for (String s : inputs) {
            String[] nodes = s.split("-");

            graph.addTwoWayVertex(nodes[0], nodes[1]);
        }
        LinkedList<String> visited = new LinkedList();
        visited.add(START);

        int c = depthFirst(graph, visited);

        return c;
    }

    @Advent(day = Day.Day_12, part = Part.two)
    public int Day_12Part2(String[] inputs) throws IOException {
        Graph graph = new Graph();
        for (String s : inputs) {
            String[] nodes = s.split("-");

            graph.addTwoWayVertex(nodes[0], nodes[1]);

        }
        LinkedList<String> visited = new LinkedList();
        visited.add(START);

        int c = depthFirst2(graph, visited, false);

        return c;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_12Tasks());
    }
}
