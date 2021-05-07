package it.polimi.ingsw.utils;

public enum ForegroundColors {
    RESET("\033[0m"),

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
    WHITE_BOLD_BRIGHT("\033[1;97m");

    private final String value;

    ForegroundColors(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
