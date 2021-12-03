public class ConsoleUtil {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void println(Object info) {
        System.out.println(info);
    }

    public static void log(Object info) {
        System.out.println(ANSI_GREEN + info + ANSI_RESET);
    }

    public static void warn(Object info) {
        System.out.println(ANSI_YELLOW + info + ANSI_RESET);
    }
}
