import java.io.IOException;
import java.util.*;

public class Day_21Tasks {
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

    @Advent(day = Day.Day_21, part = Part.one)
    public int Day_21Part1(String[] inputs) throws IOException {
        int space1 = inputs[0].charAt(inputs[0].length() - 1) - 48;
        int space2 = inputs[1].charAt(inputs[1].length() - 1) - 48;

        int total1 = 0, total2 = 0;
        int dice = 6;
        final int increment = 9;
        int i = 1;
        while (true) {
            if (i % 2 == 1) {
                // 1
                space1 = (space1 + dice - 1) % 10 + 1;
                total1 += space1;
            } else {
                // 2
                space2 = (space2 + dice - 1) % 10 + 1;
                total2 += space2;
            }

            if (total1 >= 1000 || total2 >= 1000)
                break;
            dice += increment;
            i++;
        }
        System.out.println(i + "," + total1 + ", " + total2);
        return i * 3 * Math.min(total1, total2);
    }

    class Room {
        int player1Space;
        int player2Space;
        int total1;
        int total2;
        char nextMove = '1';

        public Room(int player1Space, int player2Space, int total1, int total2) {
            this.player1Space = player1Space;
            this.player2Space = player2Space;
            this.total1 = total1;
            this.total2 = total2;
        }

        public boolean isPlayer1Turn() {
            return nextMove == '1';
        }

        public void switchTurn() {
            this.nextMove = '2';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Room room = (Room) o;
            return player1Space == room.player1Space &&
                    player2Space == room.player2Space &&
                    total1 == room.total1 &&
                    total2 == room.total2 &&
                    nextMove == room.nextMove;
        }

        @Override
        public int hashCode() {
            return Objects.hash(player1Space, player2Space, total1, total2, nextMove);
        }
    }

    private long[] whoWin(Room room, HashMap<Room, long[]> knownResult, List<Integer> universes) {
        if (knownResult.containsKey(room)) {
            return knownResult.get(room);
        }
        if (room.total1 >= 21) {
            return new long[]{1, 0};
        } else if (room.total2 >= 21) {
            return new long[]{0, 1};
        }

        long[] result = {0, 0};
        for (int change : universes) {
            Room uRoom;
            if (room.isPlayer1Turn()) {
                int player1Space = (room.player1Space + change - 1) % 10 + 1;
                int total1 = player1Space + room.total1;
                uRoom = new Room(player1Space, room.player2Space, total1, room.total2);
                uRoom.switchTurn();
            } else {
                int player2Space = (room.player2Space + change - 1) % 10 + 1;
                int total2 = player2Space + room.total2;
                uRoom = new Room(room.player1Space, player2Space, room.total1, total2);
            }

            long[] r = whoWin(uRoom, knownResult, universes);
            result[0] = result[0] + r[0];
            result[1] = result[1] + r[1];
        }

        knownResult.put(room, result);
        System.out.println(result[0] + "," + result[1]);
        return result;
    }

    @Advent(day = Day.Day_21, part = Part.two)
    public long Day_21Part2(String[] inputs) throws IOException {
        int play1 = inputs[0].charAt(inputs[0].length() - 1) - 48;
        int play2 = inputs[1].charAt(inputs[1].length() - 1) - 48;

        List<Integer> universes = new ArrayList<>();
        for (int i : new int[]{1, 2, 3}) {
            for (int j : new int[]{1, 2, 3}) {
                for (int k : new int[]{1, 2, 3}) {
                    universes.add(i + j + k);
                }
            }
        }

        Room start = new Room(play1, play2, 0, 0);
        HashMap<Room, long[]> knownResult = new HashMap<>();

        long[] winCount = whoWin(start, knownResult, universes);

        return Math.max(winCount[0], winCount[1]);

        /***
         *
         * 1
         * 1    2    3
         * 123 123  123
         * 345 456  567
         *
         * 2
         * 1    2    3
         * 123 123  123
         * 456 567  678
         *
         * 3
         * 1    2    3
         * 123 123  123
         * 567 678  789
         *
         * for each time, there are only following possible changes to each player
         *
         * 345 456 567 678 789
         * 2   5   8   1   4
         *
         *
         * We need to test which path to go
         */

    }

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        System.out.print(Math.pow(3, 331));
        platform.bootstrap(new Day_21Tasks());
    }
}
