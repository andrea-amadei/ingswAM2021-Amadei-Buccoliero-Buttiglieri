package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.holder.ConversionHolder;
import it.polimi.ingsw.server.model.market.ConversionActuator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversionHolderTest {

    @Test
    public void validCreation(){
        assertDoesNotThrow(ConversionHolder::new);
    }

    @Test
    public void emptyConversionForEachColorOnCreation(){
        ConversionHolder conversionHolder = new ConversionHolder();

        assertEquals(new ArrayList<ConversionActuator>(), conversionHolder.getActuatorsFromColor(MarbleColor.BLUE));
        assertEquals(new ArrayList<ConversionActuator>(), conversionHolder.getActuatorsFromColor(MarbleColor.WHITE));
        assertEquals(new ArrayList<ConversionActuator>(), conversionHolder.getActuatorsFromColor(MarbleColor.GREY));
        assertEquals(new ArrayList<ConversionActuator>(), conversionHolder.getActuatorsFromColor(MarbleColor.RED));
        assertEquals(new ArrayList<ConversionActuator>(), conversionHolder.getActuatorsFromColor(MarbleColor.YELLOW));
        assertEquals(new ArrayList<ConversionActuator>(), conversionHolder.getActuatorsFromColor(MarbleColor.PURPLE));
    }

    @Test
    public void addTwoConversionsAndRetrieveThem(){
        ConversionHolder conversionHolder = new ConversionHolder();
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ConversionActuator actuator1 = new ConversionActuator(Collections.singletonList(gold), 0);
        ConversionActuator actuator2 = new ConversionActuator(Collections.singletonList(gold), 1);

        conversionHolder.addConversionActuator(MarbleColor.BLUE, actuator1);
        conversionHolder.addConversionActuator(MarbleColor.BLUE, actuator2);

        List<ConversionActuator> expectedActuators = new ArrayList<>();
        expectedActuators.add(actuator1);
        expectedActuators.add(actuator2);

        assertEquals(expectedActuators, conversionHolder.getActuatorsFromColor(MarbleColor.BLUE));
    }

    @Test
    public void addConversionsToDifferentColorsAndRetrieveThem(){
        ConversionHolder conversionHolder = new ConversionHolder();
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ConversionActuator actuator1 = new ConversionActuator(Collections.singletonList(gold), 0);
        ConversionActuator actuator2 = new ConversionActuator(Collections.singletonList(gold), 1);

        conversionHolder.addConversionActuator(MarbleColor.BLUE, actuator1);
        conversionHolder.addConversionActuator(MarbleColor.RED, actuator2);

        List<ConversionActuator> expectedBlueActuators = new ArrayList<>();
        List<ConversionActuator> expectedRedActuators = new ArrayList<>();
        expectedBlueActuators.add(actuator1);
        expectedRedActuators.add(actuator2);

        assertEquals(expectedBlueActuators, conversionHolder.getActuatorsFromColor(MarbleColor.BLUE));
        assertEquals(expectedRedActuators, conversionHolder.getActuatorsFromColor(MarbleColor.RED));
    }

    @Test
    public void nullPointerAddConversions(){
        ConversionHolder conversionHolder = new ConversionHolder();
        assertThrows(NullPointerException.class, () -> conversionHolder.addConversionActuator(null, null));
    }

    @Test
    public void nullPointerGetConversions(){
        ConversionHolder conversionHolder = new ConversionHolder();
        assertThrows(NullPointerException.class, () -> conversionHolder.getActuatorsFromColor(null));
    }
}
