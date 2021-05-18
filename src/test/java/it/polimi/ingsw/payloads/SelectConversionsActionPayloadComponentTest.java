package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.SelectConversionsActionPayloadComponent;
import it.polimi.ingsw.model.actions.SelectConversionsAction;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectConversionsActionPayloadComponentTest {

    private List<Integer> actuatorsChoice;

    @BeforeEach
    public void init(){
        actuatorsChoice = new ArrayList<>();
        actuatorsChoice.add(1);
        actuatorsChoice.add(2);
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new SelectConversionsActionPayloadComponent("Ernestino", actuatorsChoice);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"select_conversions\",\"group\":\"action\",\"actuatorsChoice\":[1,2]," +
                "\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"select_conversions\",\"group\":\"action\",\"actuatorsChoice\":[1,2],\"player\"" +
                ":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof SelectConversionsAction);

        SelectConversionsAction action = ((SelectConversionsAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals(actuatorsChoice, action.getActuatorsChoice());
    }

}
