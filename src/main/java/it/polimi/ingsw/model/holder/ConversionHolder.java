package it.polimi.ingsw.model.holder;

import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.market.ConversionActuator;

import java.util.*;

/**
 * A conversion holder. Every time the player activates a leader card that adds behavior to a marble color, a new actuator
 * is added to the list of actuators associated to that color.
 */
public class ConversionHolder {
    private final Map<MarbleColor, List<ConversionActuator>> conversions;

    /**
     * Creates a new ConversionHolder. Each color has no associated actuator
     */
    public ConversionHolder(){
        conversions = new HashMap<>();
    }

    /**
     * Adds an actuator to the list of the actuators associated to the specified color
     * @param color the marble color to which the new actuator is associated
     * @param actuator the actuator to associate to the specified color
     * @throws NullPointerException if either color or actuator are null
     */
    public void addConversionActuator(MarbleColor color, ConversionActuator actuator){
        if(color == null || actuator == null)
            throw new NullPointerException();

        List<ConversionActuator> colorConversions = conversions.computeIfAbsent(color, k -> new ArrayList<>());
        colorConversions.add(actuator);
    }

    /**
     * Returns a copy of the list of actuators associated to the specified color
     * @param color the specified color
     * @return a copy of the list of actuators associated to the specified color. If no actuators
     *         are associated with the specified color, an empty list is returned
     * @throws NullPointerException if color is null
     */
    public List<ConversionActuator> getActuatorsFromColor(MarbleColor color){
        if(color == null)
            throw new NullPointerException();

        Optional<List<ConversionActuator>> conversionsFromColor = Optional.ofNullable(conversions.get(color));
        conversionsFromColor.map(ArrayList::new);
        return conversionsFromColor.orElse(new ArrayList<>());
    }
}
