package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

public class OutputHandler {
    private static final int STANDARD_WIDTH = 180;
    private static final int STANDARD_HEIGHT = 48;

    private static final char BLANK = ' ';

    private final int width;
    private final int height;

    private final boolean printColors;

    private final char[][] chars;
    private final ForegroundColor[][] foreground;
    private final BackgroundColor[][] background;

    public OutputHandler(boolean printColors) {
        width = STANDARD_WIDTH;
        height = STANDARD_HEIGHT;

        this.printColors = printColors;

        chars = new char[height][width];
        foreground = new ForegroundColor[height][width];
        background = new BackgroundColor[height][width];

        resetAll();
    }

    public OutputHandler(boolean printColors, int width, int height) {
        if(width <= 0 || height <= 0)
            throw new IllegalArgumentException("Width and height must be positive");

        this.width = width;
        this.height = height;

        this.printColors = printColors;

        chars = new char[height][width];
        foreground = new ForegroundColor[height][width];
        background = new BackgroundColor[height][width];

        resetAll();
    }

    public void update() {
        StringBuilder str = new StringBuilder("\r");

        str.append("\n".repeat(30));

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if (printColors)
                    str.append(foreground[i][j]).append(background[i][j]);

                str.append(chars[i][j]);
            }

            str.append(BackgroundColor.RESET).append('\n');
        }

        str.append("\n> ");

        System.out.print(str);
    }

    public static int getStandardWidth() {
        return STANDARD_WIDTH;
    }

    public static int getStandardHeight() {
        return STANDARD_HEIGHT;
    }

    public char getBlank() {
        return BLANK;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char getChar(int row, int column) {
        if(row < 0 || row >= height)
            throw new IndexOutOfBoundsException("Invalid row");

        if(column < 0 || column >= width)
            throw new IndexOutOfBoundsException("Invalid column");

        return chars[row][column];
    }

    public ForegroundColor getForeground(int row, int column) {
        if(row < 0 || row >= height)
            throw new IndexOutOfBoundsException("Invalid row");

        if(column < 0 || column >= width)
            throw new IndexOutOfBoundsException("Invalid column");

        return foreground[row][column];
    }

    public BackgroundColor getBackground(int row, int column) {
        if(row < 0 || row >= height)
            throw new IndexOutOfBoundsException("Invalid row");

        if(column < 0 || column >= width)
            throw new IndexOutOfBoundsException("Invalid column");

        return background[row][column];
    }

    public void resetChars() {
        for(int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                chars[i][j] = BLANK;
    }

    public void resetForegroundColors() {
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                foreground[i][j] = ForegroundColor.RESET;
    }

    public void resetBackgroundColors() {
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                background[i][j] = BackgroundColor.RESET;
    }

    public void resetAll() {
        resetChars();
        resetForegroundColors();
        resetBackgroundColors();
    }

    public void setChar(int row, int column, char newChar) {
        if(row < 0 || row >= height)
            throw new IndexOutOfBoundsException("Invalid row");

        if(column < 0 || column >= width)
            throw new IndexOutOfBoundsException("Invalid column");

        chars[row][column] = newChar;
    }

    public void setForegroundColor(int row, int column, ForegroundColor newForegroundColor) {
        if(row < 0 || row >= height)
            throw new IndexOutOfBoundsException("Invalid row");

        if(column < 0 || column >= width)
            throw new IndexOutOfBoundsException("Invalid column");

        foreground[row][column] = newForegroundColor;
    }

    public void setForegroundBackground(int row, int column, ForegroundColor newForegroundColor) {
        if(row < 0 || row >= height)
            throw new IndexOutOfBoundsException("Invalid row");

        if(column < 0 || column >= width)
            throw new IndexOutOfBoundsException("Invalid column");

        foreground[row][column] = newForegroundColor;
    }

    public void setString(int startingRow, int startingColumn, String newString) {
        if(newString == null)
            throw new NullPointerException();

        if(newString.length() == 0)
            throw new IllegalArgumentException("Invalid empty string");

        if(startingRow < 0 || startingRow >= height)
            throw new IndexOutOfBoundsException("Invalid starting row");

        if(startingColumn < 0)
            throw new IndexOutOfBoundsException("Invalid starting column");

        if(startingColumn + newString.length() >= width)
            throw new IndexOutOfBoundsException("String too long to fit");

        for(int i = 0; i < newString.length(); i++)
            chars[startingRow][startingColumn + i] = newString.charAt(i);
    }

    public void setString(int startingRow, int startingColumn, String newString, ForegroundColor newForegroundColor) {
        if(newString == null || newForegroundColor == null)
            throw new NullPointerException();

        if(newString.length() == 0)
            throw new IllegalArgumentException("Invalid empty string");

        if(startingRow < 0 || startingRow >= height)
            throw new IndexOutOfBoundsException("Invalid starting row");

        if(startingColumn < 0)
            throw new IndexOutOfBoundsException("Invalid starting column");

        if(startingColumn + newString.length() >= width)
            throw new IndexOutOfBoundsException("String too long to fit");

        for(int i = 0; i < newString.length(); i++) {
            chars[startingRow][startingColumn + i] = newString.charAt(i);
            foreground[startingRow][startingColumn + i] = newForegroundColor;
        }
    }

    public void setString(int startingRow, int startingColumn, String newString, ForegroundColor newForegroundColor, BackgroundColor newBackgroundColor) {
        if(newString == null || newForegroundColor == null || newBackgroundColor == null)
            throw new NullPointerException();

        if(newString.length() == 0)
            throw new IllegalArgumentException("Invalid empty string");

        if(startingRow < 0 || startingRow >= height)
            throw new IndexOutOfBoundsException("Invalid starting row");

        if(startingColumn < 0)
            throw new IndexOutOfBoundsException("Invalid starting column");

        if(startingColumn + newString.length() >= width)
            throw new IndexOutOfBoundsException("String too long to fit");

        for(int i = 0; i < newString.length(); i++) {
            chars[startingRow][startingColumn + i] = newString.charAt(i);
            foreground[startingRow][startingColumn + i] = newForegroundColor;
            background[startingRow][startingColumn + i] = newBackgroundColor;
        }
    }

    public void setCharRectangle(int startingRow, int startingColumn, int endingRow, int endingColumn, char newChar) {
        if(startingRow < 0 || startingRow >= height)
            throw new IndexOutOfBoundsException("Invalid starting row");

        if(startingColumn < 0 || startingColumn >= width)
            throw new IndexOutOfBoundsException("Invalid starting width");

        if(endingRow < 0 || endingRow >= height)
            throw new IndexOutOfBoundsException("Invalid ending row");

        if(endingColumn < 0 || endingColumn >= width)
            throw new IndexOutOfBoundsException("Invalid ending width");

        if(startingRow > endingRow)
            throw new IllegalArgumentException("Starting row is bigger than ending row");

        if(startingColumn > endingColumn)
            throw new IllegalArgumentException("Starting column is bigger than ending row");

        for(int i = startingRow; i <= endingRow; i++)
            for(int j = startingColumn; j <= endingColumn; j++)
                chars[i][j] = newChar;
    }

    public void setForegroundColorRectangle(int startingRow, int startingColumn, int endingRow, int endingColumn, ForegroundColor newForegroundColor) {
        if(startingRow < 0 || startingRow >= height)
            throw new IndexOutOfBoundsException("Invalid starting row");

        if(startingColumn < 0 || startingColumn >= width)
            throw new IndexOutOfBoundsException("Invalid starting width");

        if(endingRow < 0 || endingRow >= height)
            throw new IndexOutOfBoundsException("Invalid ending row");

        if(endingColumn < 0 || endingColumn >= width)
            throw new IndexOutOfBoundsException("Invalid ending width");

        if(startingRow > endingRow)
            throw new IllegalArgumentException("Starting row is bigger than ending row");

        if(startingColumn > endingColumn)
            throw new IllegalArgumentException("Starting column is bigger than ending row");

        for(int i = startingRow; i <= endingRow; i++)
            for(int j = startingColumn; j <= endingColumn; j++)
                foreground[i][j] = newForegroundColor;
    }

    public void setBackgroundColorRectangle(int startingRow, int startingColumn, int endingRow, int endingColumn, BackgroundColor newBackgroundColor) {
        if(startingRow < 0 || startingRow >= height)
            throw new IndexOutOfBoundsException("Invalid starting row");

        if(startingColumn < 0 || startingColumn >= width)
            throw new IndexOutOfBoundsException("Invalid starting width");

        if(endingRow < 0 || endingRow >= height)
            throw new IndexOutOfBoundsException("Invalid ending row");

        if(endingColumn < 0 || endingColumn >= width)
            throw new IndexOutOfBoundsException("Invalid ending width");

        if(startingRow > endingRow)
            throw new IllegalArgumentException("Starting row is bigger than ending row");

        if(startingColumn > endingColumn)
            throw new IllegalArgumentException("Starting column is bigger than ending row");

        for(int i = startingRow; i <= endingRow; i++)
            for(int j = startingColumn; j <= endingColumn; j++)
                background[i][j] = newBackgroundColor;
    }


}
