package it.polimi.ingsw.common.utils;

public enum BackgroundColor {
    RESET("\u001B[0m"),

    BLACK("\u001B[40m"),
    RED("\u001B[41m"),
    GREEN("\u001B[42m"),
    YELLOW("\u001B[43m"),
    BLUE("\u001B[44m"),
    PURPLE("\u001B[45m"),
    CYAN("\u001B[46m"),
    WHITE("\u001B[47m"),

    BLACK_BRIGHT("\033[100m"),
    RED_BRIGHT("\033[101m"),
    GREEN_BRIGHT("\033[102m"),
    YELLOW_BRIGHT("\033[103m"),
    BLUE_BRIGHT("\033[104m"),
    PURPLE_BRIGHT("\033[105m"),
    CYAN_BRIGHT("\033[106m"),
    WHITE_BRIGHT("\033[107m");

    private final String value;

    BackgroundColor(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
