package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.PreliminaryPickActionPayloadComponent;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.actions.PreliminaryPickAction;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PreliminaryPickActionPayloadComponentTest {

    List<Integer> leadersToDiscard;
    Map<String, Integer> chosenResources;

    @BeforeEach
    public void init(){
        leadersToDiscard = new ArrayList<>();
        chosenResources = new HashMap<>();

        leadersToDiscard.add(0);
        leadersToDiscard.add(2);
        chosenResources.put("stone", 1);
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new PreliminaryPickActionPayloadComponent("Ernestino", leadersToDiscard,
                chosenResources);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"preliminary_pick\",\"group\":\"action\",\"leadersToDiscard\":[0,2]," +
                "\"chosenResources\":{\"stone\":1},\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"preliminary_pick\",\"group\":\"action\",\"leadersToDiscard\":[0,2]," +
                "\"chosenResources\":{\"stone\":1},\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof PreliminaryPickAction);

        PreliminaryPickAction action = ((PreliminaryPickAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals(leadersToDiscard, action.getLeadersToDiscard());
        assertEquals(1,action.getChosenResources().get(ResourceTypeSingleton.getInstance().getStoneResource()));
    }

}
