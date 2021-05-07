package it.polimi.ingsw.utils;

public enum BackgroundColor {
    RESET("\033[0m"),

    BLACK("\033[40m"),
    RED("\033[41m"),
    GREEN("\033[42m"),
    YELLOW("\033[43m"),
    BLUE("\033[44m"),
    PURPLE("\033[45m"),
    CYAN("\033[46m"),
    WHITE("\033[47m"),

    BLACK_BRIGHT("\033[0;100m"),
    RED_BRIGHT("\033[0;101m"),
    GREEN_BRIGHT("\033[0;102m"),
    YELLOW_BRIGHT("\033[0;103m"),
    BLUE_BRIGHT("\033[0;104m"),
    PURPLE_BRIGHT("\033[0;105m"),
    CYAN_BRIGHT("\033[0;106m"),
    WHITE_BRIGHT("\033[0;107m");

    private final String value;

    BackgroundColor(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
