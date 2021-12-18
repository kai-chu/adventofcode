import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

public class Day_18Tasks {
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

    /**
     * explode happens when nested is 5
     * [[[][]],[]]
     * with counting [, the value 5 means the next
     * root
     * 3
     * 2,
     * 1,
     * 7, 3
     * <p>
     * root
     * 6
     * 5
     * 4
     * 3, 2
     * <p>
     * [[[[[9,8],1],2],3],4]
     * root
     * root 4
     * root  3
     * root  2
     * root   1
     * 9, 8
     * <p>
     * [7,[6,[5,[4,[3,2]]]]]
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * root
     * 7   root
     * 6   root
     * 5  root
     * 4  root
     * 3 2
     * split happens after explode
     *
     * @param inputs
     * @return
     * @throws IOException
     */

    static class Node {
        int val;
        Node left;
        Node right;
        Node parent;

        boolean exploded = false;

        boolean isLeaf() {
            return val >= 0;
        }

        public Node(int val, Node left, Node right) {
            this.val = val;
            this.right = right;
            this.left = left;
            this.parent = null;
            if (left != null)
                this.left.parent = this;
            if (right != null)
                this.right.parent = this;
        }
    }

    private static Node constructTree(String line) {
        Stack<Node> stack = new Stack<>();

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '[') {
                continue;
            } else if (line.charAt(i) == ',') {
                continue;
            } else if (line.charAt(i) == ']') {
                Node right = stack.pop();
                Node left = stack.pop();
                Node node = new Node(-1, left, right);
                stack.push(node);
            } else {
                stack.push(new Node(line.charAt(i) - 48, null, null));
            }
        }
        return stack.peek();
    }


    private static Node getPrev(Node node) {
        if (node.parent == null) {
            return null;
        }

        Node p = node.parent;
        while (p != null && p.left == node) {
            node = p;
            p = p.parent;
        }

        if (p == null || p.left == null) return null;

        Node curr = p.left;
        while (curr.right != null) {
            curr = curr.right;
        }

        return curr;
    }

    private static Node getNext(Node node) {
        Node p = node.parent;
        while (p != null && p.right == node) {
            node = p;
            p = p.parent;
        }

        if (p == null || p.right == null) return null;

        Node curr = p.right;
        while (curr.left != null) {
            curr = curr.left;
        }

        return curr;
    }

    private static void preOrder(Node node) {
        if (node != null) {
            if (node.isLeaf()) {
                System.out.print(node.val);
            } else {
                System.out.print("[");
            }
            preOrder(node.left);
            //System.out.print((prev != null ? prev.val : -2) + " < " + node.val + " < " + (next != null ? next.val : -2));
            if (!node.isLeaf())
                System.out.print(",");
            preOrder(node.right);
            if (node.parent != null && node.parent.right == node)
                System.out.print("]");


        }

    }

    private static long magnitude(Node root) {
        if (root.left == null && root.right == null) {
            return root.val;
        }

        return 3l * magnitude(root.left) + 2l * magnitude(root.right);
    }

    private static boolean adjust(Node root, int height, int mode) {
        if (root != null) {
            if (mode == 1) {
                if (height == 4) {
                    if (root.left != null && root.right != null) {
                        Node prev = getPrev(root);
                        if (prev != null) {
                            prev.val += root.left.val;
                        }
                        Node next = getNext(root);
                        if (next != null) {
                            next.val += root.right.val;
                        }
                        //ConsoleUtil.println("explode:" + root.left.val + "-" + root.right.val);

                        root.left = null;
                        root.right = null;
                        root.val = 0;
                        return true;
                    }
                }
            } else if (mode == 2) {
                if (root.val >= 10) {
                    Node left = new Node(root.val / 2, null, null);
                    Node right = new Node(root.val % 2 == 0 ? left.val : left.val + 1, null, null);

                    root.val = -1;
                    root.left = left;
                    root.right = right;
                    left.parent = root;
                    right.parent = root;
                    return true;
                }
            }


            boolean changes;
            changes = adjust(root.left, height + 1, mode);
            if (changes) return true;
            changes = adjust(root.right, height + 1, mode);
            return changes;
        }
        return false;
    }

    private static Node reduce(Node t1, Node t2) {
        Node root = null;

        if (t1 == null || t2 == null) {
            if (t1 != null) root = t1;
            if (t2 != null) root = t2;
        } else {
            root = new Node(-1, t1, t2);
        }

        boolean change = true;
        while (change) {
            change = false;
            while (adjust(root, 0, 1)) {
                change = true;
                //System.out.println("after exploded");
                //preOrder(root);
            }

            if (adjust(root, 0, 2)) {
                change = true;
                //System.out.println("after split");
                //preOrder(root);
            }

        }
        return root;
    }

    @Advent(day = Day.Day_18, part = Part.one)
    public long Day_18Part1(String[] inputs) throws IOException {
        Node sum = null;
        for (String line : inputs) {
            Node tree = constructTree(line);
            sum = reduce(sum, tree);
        }
        preOrder(sum);
        return magnitude(sum);
    }

    @Advent(day = Day.Day_18, part = Part.two)
    public long Day_18Part2(String[] inputs) throws IOException {
        long max = 0;
        for (int i = 0; i < inputs.length; i++) {
            for (int j = i + 1; j < inputs.length; j++) {
                Node root = reduce(constructTree(inputs[i]), constructTree(inputs[j]));
                Node reverse = reduce(constructTree(inputs[j]), constructTree(inputs[i]));
                max = Math.max(Math.max(magnitude(root), magnitude(reverse)), max);
            }
        }
        return max;
    }

    public static void main(String[] args) throws IOException {
        //Node root = constructTree("[[[[4,9],0],[[4,4],9]],[[[6,1],[8,9]],[7,[2,3]]]]");
        //Node root1 = constructTree("[[[[4,9],0],[[4,4],9]],[[[6,1],[8,9]],[7,[2,3]]]]");
        //root = reduce(root, null);

        //ConsoleUtil.println(a);
        //preOrder(root);


        //ConsoleUtil.println(magnitude(root));

        final Platform platform = new Platform();
        platform.bootstrap(new Day_18Tasks());
    }
}
