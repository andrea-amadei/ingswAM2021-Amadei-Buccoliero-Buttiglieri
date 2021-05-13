package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String timeZone = "Z";

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
                            BackgroundColor.RESET);
    }

    public static void log(String message, Severity severity) {
        System.out.println( "[" + getCurrentTime() + "] " +
                severity + ": " +
                message +
                BackgroundColor.RESET);
    }

    public static void log(String message, Severity severity, ForegroundColor foreground) {
        System.out.println( "[" + getCurrentTime() + "] " +
                foreground +
                severity + ": " +
                message +
                BackgroundColor.RESET);
    }

    public static void log(String message, Severity severity, ForegroundColor foreground, BackgroundColor background) {
        System.out.println( "[" + getCurrentTime() + "] " +
                foreground + background +
                severity + ": " +
                message +
                BackgroundColor.RESET);
    }
}
