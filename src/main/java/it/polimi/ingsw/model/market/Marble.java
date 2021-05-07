package it.polimi.ingsw.model.market;

import it.polimi.ingsw.gamematerials.MarbleColor;

/**
 * A marble of the market. It hase a color and a baseConversionActuator. The base actuator is used iff the player
 * doesn't have any conversions from the color of this marble to one or more ConversionActuator.
 * It is an immutable class
 */
public class Marble {
    private final MarbleColor color;
    private transient final ConversionActuator baseConversionActuator;

    /**
     * Creates a new marble with a color and a default actuator. Note: it should only be created by the factory class
     * @param color the color of this marble
     * @param baseConversionActuator the base conversion actuator of the marble
     * @throws NullPointerException if either color or baseConversionActuator is null
     */
    public Marble(MarbleColor color, ConversionActuator baseConversionActuator){
        if(color == null || baseConversionActuator == null)
            throw new NullPointerException();

        this.color = color;
        this.baseConversionActuator = baseConversionActuator;
    }

    /**
     * Returns the color of this marble
     * @return the color of this marble
     */
    public MarbleColor getColor() {return color;}

    /**
     * Returns the base ConversionActuator of this marble
     * @return the base ConversionActuator of this marble
     */
    public ConversionActuator getBaseConversionActuator() {return baseConversionActuator;}

    @Override
    public String toString(){
        return String.valueOf(this.color.name().charAt(0));
    }

}
