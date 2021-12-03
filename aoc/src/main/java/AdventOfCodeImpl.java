import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCodeImpl implements AdventOfCode {
    private final String adventOfCodeLinkTemplate = "https://adventofcode.com/%d/day/%d/input";
    private Year year;

    public AdventOfCodeImpl(Year year) {
        this.year = year;
    }

    @Override
    public String[] getInput(Day day) throws IOException {
        String adventOfCodeLink = String.format(adventOfCodeLinkTemplate, year.value, day.value);
        try {
            URL dayUrl = new URL(adventOfCodeLink);
            return getInput(dayUrl.openStream());

        } catch (MalformedURLException e) {
            throw new IOException("Cannot parse the input from " + adventOfCodeLink);
        }
    }

    @Override
    public String[] getInput(String filename) throws IOException {
        InputStream stream = this.getClass().getResourceAsStream(filename);
        return getInput(stream);
    }

    private String[] getInput(InputStream is) throws IOException {
        List<String> inputs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                inputs.add(line);
            }
        } catch (IOException e) {
            throw e;
        }
        return inputs.toArray(new String[0]);
    }
}
