import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class TestAdventOfCode {
    @Test
    public void test() throws IOException {
        String[] lines = AOCFactory.aoc2021.getInput("test.txt");
        assertArrayEquals(new String[]{"a", "b", "c"}, lines);
    }

}
