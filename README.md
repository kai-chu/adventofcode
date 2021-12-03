# adventofcode

## Feature 

1. Add two files when task is published from Adventofcode
```
2021
+ src
++ main 
+++ java
++++ DailyTasks.java <- the place you should put your code
+++ resources
++++ day_3.txt       <- your input data for the puzzle
++++ day_3-test.txt  <- demo data
```

2. About DailyTasks.java

The file should have following structure to use the Platform which 
* Help to read input from file into String[]
* Run the task for TODAY 
* Print the result returned from Annotated method

```
public class DailyTasks {
    private static final AdventOfCode aoc2021 = AOCFactory.aoc2021;

    public static void main(String[] args) throws IOException {
        final Platform platform = new Platform();
        platform.bootstrap(new DailyTasks());
    }
    ...
}
```

3. Add code for next day when you finished for today so that you can start to code tomorrow morning, e.g. Day4
```
public class DailyTasks {
    ...

    @Advent(day = Day.Day_4)
    public int day4Part1(String[] inputs) throws IOException {
        return 0; <- whatever returned will be printed
    }

    @Advent(day = Day.Day_4, part = Part.two)
    public int day4Part2(String[] inputs) throws IOException {
        return 0;
    }
}
```




