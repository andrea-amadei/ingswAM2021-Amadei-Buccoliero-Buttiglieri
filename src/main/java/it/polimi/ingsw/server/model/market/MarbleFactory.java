package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;

import java.util.ArrayList;
import java.util.Collections;

public class MarbleFactory {

    /**
     * Creates a new marble of the specified color
     * @param color the color of this marble
     * @return a new Marble with standard values
     * @throws NullPointerException if color is null
     */
    public static Marble createMarble(MarbleColor color){
        if(color == null)
            throw new NullPointerException();

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

        ConversionActuator actuator = null;

        switch(color){
            case BLUE:
                actuator = new ConversionActuator(Collections.singletonList(shield), 0);
                break;
            case WHITE:
                actuator = new ConversionActuator(new ArrayList<>(), 0);
                break;
            case GREY:
                actuator = new ConversionActuator(Collections.singletonList(stone), 0);
                break;
            case RED:
                actuator = new ConversionActuator(new ArrayList<>(), 1);
                break;
            case PURPLE:
                actuator = new ConversionActuator(Collections.singletonList(servant), 0);
                break;
            case YELLOW:
                actuator = new ConversionActuator(Collections.singletonList(gold), 0);
                break;
        }

        return new Marble(color, actuator);
    }
}
