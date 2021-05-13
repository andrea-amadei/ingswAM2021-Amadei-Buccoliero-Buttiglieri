package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

public interface TextElement extends CliElement {
    String getText();

    ForegroundColor getForegroundColor();
    boolean isForegroundColorVisible();
    BackgroundColor getBackgroundColor();
    boolean isBackgroundColorVisible();

    void setText(String text);

    void setForegroundColor(ForegroundColor foregroundColor);
    void setForegroundColorVisible(boolean foregroundColorVisible);
    void setBackgroundColor(BackgroundColor backgroundColor);
    void setBackgroundColorVisible(boolean backgroundColorVisible);
}
