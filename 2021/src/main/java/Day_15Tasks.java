import java.io.Console;
import java.io.IOException;
import java.util.*;

public class Day_15Tasks {
    private static final AdventOfCode aoc2021 = AOCFactory.aoc2021;


    public int[][] readGraph(String[] inputs) {
        int[][] graph = new int[inputs.length][inputs[0].length()];
        int k = 0;
        for (String line : inputs) {
            int[] arr = new int[line.length()];
            for (int i = 0; i < line.length(); i++) {
                arr[i] = line.charAt(i) - 48;
            }
            graph[k++] = arr;
        }

        return graph;
    }

    public void printGraph(int[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                System.out.print(graph[i][j]);

            }
            System.out.println("");
        }
    }

    public void printGraph(char[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                System.out.print(graph[i][j]);

            }
            System.out.println("");
        }
    }

    public void initArr(int[][] memo, int initial) {
        for (int i = memo.length - 1; i >= 0; i--) {
            for (int j = memo[0].length - 1; j >= 0; j--) {
                memo[i][j] = initial;
            }
        }
    }

    class Node {
        int i;
        int j;
        int val;

        public Node(int i, int j, int val) {
            this.i = i;
            this.j = j;
            this.val = val;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return i == node.i &&
                    j == node.j &&
                    val == node.val;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j, val);
        }
    }


    private int findPathDijkstra(int[][] graph) {
        int[][] distance = new int[graph.length][graph[0].length];
        initArr(distance, 10000);
        distance[0][0] = 0;

        int[][] adj = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        Queue<Node> queue = new PriorityQueue<>(20, Comparator.comparingInt(x -> distance[x.i][x.j]));
        queue.offer(new Node(0, 0, 0));
        Set<Node> visited = new HashSet<>();
        while (visited.size() != graph.length * graph[0].length) {
            Node minNode = queue.poll();

            if(visited.contains(minNode)) continue;

            visited.add(minNode);

            System.out.println(visited.size());
            for (int[] move : adj) {
                int x = minNode.i + move[0];
                int y = minNode.j + move[1];
                if (x >= 0 && y >= 0 && x < graph.length && y < graph[0].length) {
                    Node node = new Node(x, y, graph[x][y]);
                    if (!visited.contains(node)) {
                        if (distance[minNode.i][minNode.j] + node.val < distance[node.i][node.j]) {
                            distance[node.i][node.j] = distance[minNode.i][minNode.j] + node.val;
                        }
                        queue.add(node);
                    }
                }
            }
        }

        return distance[graph.length - 1][graph[0].length - 1];
    }

    @Advent(day = Day.Day_15, part = Part.one)
    public int Day_15Part1(String[] inputs) throws IOException {
        int[][] graph = readGraph(inputs);

        int r = findPathDijkstra(graph);

        return r;
    }

    private void shiftCopy(int[][] graph, int originX, int originY, int[][] target, int supplyment) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                int t = (graph[i][j] + supplyment);
                target[originX + i][originY + j] = t > 9 ? t % 9 : t;
            }
        }
    }

    @Advent(day = Day.Day_15, part = Part.two)
    public int Day_15Part2(String[] inputs) throws IOException {
        int[][] graph = readGraph(inputs);

        int row = inputs.length;
        int col = inputs[0].length();

        int[][] fullGraph = new int[inputs.length * 5][inputs[0].length() * 5];
        for (int i = 0; i < fullGraph.length; i += row) {
            for (int j = 0; j < fullGraph[0].length; j += col) {
                shiftCopy(graph, i, j, fullGraph, j / col + i / row);
            }
        }

        int r = findPathDijkstra(fullGraph);

        return r;
    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new Day_15Tasks());
    }
}
