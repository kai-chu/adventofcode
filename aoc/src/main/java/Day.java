import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Optional;

public enum Day {
    Day_1(1),
    Day_2(2),
    Day_3(3),
    Day_4(4),
    Day_5(5),
    Day_6(6),
    Day_7(7),
    Day_8(8),
    Day_9(9),
    Day_10(10),
    Day_11(11),
    Day_12(12),
    Day_13(13),
    Day_14(14),
    Day_15(15),
    Day_16(16),
    Day_17(17),
    Day_18(18),
    Day_19(19),
    Day_20(20),
    Day_21(21),
    Day_22(22),
    Day_23(23),
    Day_24(24),
    Day_25(25);

    public final int value;

    Day(int value) {
        this.value = value;
    }

    public static Optional<Day> byValue(int value) {
        for(Day day: Day.values()) {
            if(day.value == value) return Optional.of(day);
        }
        return Optional.empty();
    }
}
