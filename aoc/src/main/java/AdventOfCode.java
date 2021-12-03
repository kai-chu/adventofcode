import java.io.IOException;

public interface AdventOfCode {
    String[] getInput(Day day) throws IOException;
    String[] getInput(String filename) throws IOException;
}
