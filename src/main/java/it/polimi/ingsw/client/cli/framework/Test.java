package it.polimi.ingsw.client.cli.framework;

import it.polimi.ingsw.client.cli.framework.elements.FixedTextBox;
import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.cli.framework.elements.GroupBox;
import it.polimi.ingsw.client.cli.framework.elements.ResourceBox;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Arrays;
import java.util.Collections;

public class Test {
    public static void main(String[] args) throws UnableToDrawElementException, InterruptedException {
        FixedTextBox blue = new FixedTextBox("blue", 5, 5, 5, "BLUE", ForegroundColor.BLUE, BackgroundColor.BLACK_BRIGHT);
        FixedTextBox red = new FixedTextBox("red", 6, 5, 5, "RED", ForegroundColor.RED, BackgroundColor.BLACK_BRIGHT);
        FixedTextBox green = new FixedTextBox("green", 7, 5, 5, "GREEN", ForegroundColor.GREEN, BackgroundColor.BLACK);

        GroupBox box = new GroupBox("box", 4, 3, 9, 14, "TEST", ForegroundColor.WHITE, BackgroundColor.BLACK_BRIGHT,
                Arrays.asList(new FixedTextBox[]{blue, red, green}));

        box.setDoubleBorder(true);
        box.setAlignLeft(true);

        Frame frame1 = new Frame("box", Collections.singletonList(box));
        frame1.setBackgroundColor(BackgroundColor.BLUE_BRIGHT);

        CliFramework framework = new CliFramework(true, Collections.singletonList(frame1));
        framework.setActiveFrame("box");

        for (int i = 0; i < 10; i++) {
            blue.setStartingColumn(blue.getStartingColumn() + 5);
            red.setStartingColumn(red.getStartingColumn() + 5);
            green.setStartingColumn(green.getStartingColumn() + 5);

            box.setEndingColumn(box.getEndingColumn() + 5);
            box.setStartingColumn(box.getStartingColumn() + 5);

            framework.renderActiveFrame();

            Thread.sleep(1000);
        }

        ResourceBox gold = new ResourceBox("gold", 5, 5, "gold");
        ResourceBox stone = new ResourceBox("stone", 6, 5, "stone");
        ResourceBox shield = new ResourceBox("shield", 7, 5, "shield");
        ResourceBox servant = new ResourceBox("servant", 8, 5, "servant");
        ResourceBox faith = new ResourceBox("faith", 9, 5, "faith");
        ResourceBox any = new ResourceBox("any", 10, 5, "any");

        Frame frame2 = new Frame("resources", Arrays.asList(gold, stone, shield, servant, faith, any));
        frame2.setBackgroundColor(BackgroundColor.BLACK);

        framework.addFrame(frame2);
        framework.setActiveFrame("resources");

        framework.renderActiveFrame();
    }
}
