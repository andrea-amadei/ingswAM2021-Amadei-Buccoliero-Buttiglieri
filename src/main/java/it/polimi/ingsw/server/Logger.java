package it.polimi.ingsw.server;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String timeZone = "Z";

    private static final String RESET = "\033[0m";

    public enum Format {
        BLACK("\033[0;30m"),
        RED("\033[0;31m"),
        GREEN("\033[0;32m"),
        YELLOW("\033[0;33m"),
        BLUE("\033[0;34m"),
        PURPLE("\033[0;35m"),
        CYAN("\033[0;36m"),
        WHITE("\033[0;37m"),

        BLACK_BOLD("\033[1;30m"),
        RED_BOLD("\033[1;31m"),
        GREEN_BOLD("\033[1;32m"),
        YELLOW_BOLD("\033[1;33m"),
        BLUE_BOLD("\033[1;34m"),
        PURPLE_BOLD("\033[1;35m"),
        CYAN_BOLD("\033[1;36m"),
        WHITE_BOLD("\033[1;37m"),

        BLACK_BACKGROUND("\033[40m"),
        RED_BACKGROUND("\033[41m"),
        GREEN_BACKGROUND("\033[42m"),
        YELLOW_BACKGROUND("\033[43m"),
        BLUE_BACKGROUND("\033[44m"),
        PURPLE_BACKGROUND("\033[45m"),
        CYAN_BACKGROUND("\033[46m"),
        WHITE_BACKGROUND("\033[47m"),

        BLACK_BRIGHT("\033[0;90m"),
        RED_BRIGHT("\033[0;91m"),
        GREEN_BRIGHT("\033[0;92m"),
        YELLOW_BRIGHT("\033[0;93m"),
        BLUE_BRIGHT("\033[0;94m"),
        PURPLE_BRIGHT("\033[0;95m"),
        CYAN_BRIGHT("\033[0;96m"),
        WHITE_BRIGHT("\033[0;97m"),

        BLACK_BOLD_BRIGHT("\033[1;90m"),
        RED_BOLD_BRIGHT("\033[1;91m"),
        GREEN_BOLD_BRIGHT("\033[1;92m"),
        YELLOW_BOLD_BRIGHT("\033[1;93m"),
        BLUE_BOLD_BRIGHT("\033[1;94m"),
        PURPLE_BOLD_BRIGHT("\033[1;95m"),
        CYAN_BOLD_BRIGHT("\033[1;96m"),
        WHITE_BOLD_BRIGHT("\033[1;97m"),

        BLACK_BACKGROUND_BRIGHT("\033[0;100m"),
        RED_BACKGROUND_BRIGHT("\033[0;101m"),
        GREEN_BACKGROUND_BRIGHT("\033[0;102m"),
        YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
        BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
        PURPLE_BACKGROUND_BRIGHT("\033[0;105m"),
        CYAN_BACKGROUND_BRIGHT("\033[0;106m"),
        WHITE_BACKGROUND_BRIGHT("\033[0;107m");

        private final String value;

        Format(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Severity {
        INFO("INFO"),
        WARNING("WARN"),
        ERROR("ERROR");

        private final String value;

        Severity(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private Logger() { }

    private static String getCurrentTime() {
        return ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneId.of(timeZone))
            .format(formatter);
    }

    public static void log(String message) {
        System.out.println( "[" + getCurrentTime() + "] " +
                            Severity.INFO + ": " +
                            message +
                            RESET);
    }

    public static void log(String message, Severity severity) {
        System.out.println( "[" + getCurrentTime() + "] " +
                severity + ": " +
                message +
                RESET);
    }

    public static void log(String message, Severity severity, Format format) {
        System.out.println( "[" + getCurrentTime() + "] " +
                format +
                severity + ": " +
                message +
                RESET);
    }
}
