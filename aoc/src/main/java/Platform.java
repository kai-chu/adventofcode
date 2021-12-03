import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.ZoneId;
import java.util.*;

public class Platform {

    private Optional<Map<Part, Method>> resolveAdventOfDay(Class<?> clazz, Day day) {
        return Optional.ofNullable(resolveAdvent(clazz).get(day));
    }

    private Map<Day, Map<Part, Method>> resolveAdvent(Class<?> clazz) {
        Map<Day, Map<Part, Method>> adventMaps = new HashMap<>();
        for (final Method method : clazz.getMethods()) {
            Advent adventInstance = method.getAnnotation(Advent.class);
            if (adventInstance != null) {
                Day annotatedDay = adventInstance.day();
                Part annotatedPart = adventInstance.part();
                adventMaps.computeIfAbsent(annotatedDay, k -> new HashMap<>())
                        .put(annotatedPart, method);
            }
        }
        return adventMaps;
    }


    public void bootstrap(Object obj) throws IOException {
        Class<?> clazz = obj.getClass();
        int dayOfMonth = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("America/New_York"))).get(Calendar.DAY_OF_MONTH);
        Day day = Day.byValue(dayOfMonth).get();
        if (day != null) {
            ConsoleUtil.log("== Day: " + day);
            for (Map.Entry<Part, Method> partMethodEntry : resolveAdventOfDay(clazz, day).orElse(new HashMap<>()).entrySet()) {
                String[] lines;
                ConsoleUtil.log("=== Part: " + partMethodEntry.getKey());

                for (String filename : new String[]{day.name() + "-test.txt", day.name() + ".txt"}) {
                    URL filenameURL = this.getClass().getResource(filename);
                    if (filenameURL != null) {
                        ConsoleUtil.log("Run with " + filename);
                        lines = AOCFactory.aoc2021.getInput(filename);

                        try {
                            Object o = partMethodEntry.getValue().invoke(obj, new Object[]{lines});
                            ConsoleUtil.log("Result: " + o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ConsoleUtil.warn("Not found filename " + filename);
                    }
                }
            }
        }
    }
}
