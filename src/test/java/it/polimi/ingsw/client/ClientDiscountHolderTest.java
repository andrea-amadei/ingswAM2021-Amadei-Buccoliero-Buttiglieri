package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientDiscountHolder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ClientDiscountHolderTest {

    @Test
    public void creation(){
        ClientDiscountHolder clientDiscountHolder = new ClientDiscountHolder();
        assertEquals(0, clientDiscountHolder.getDiscounts().size());
    }

    @Test
    public void addDiscount(){
        ClientDiscountHolder clientDiscountHolder = new ClientDiscountHolder();
        clientDiscountHolder.addDiscount("gold", 2);
        assertEquals(new HashMap<String, Integer>(){{put("gold", 2);}}, clientDiscountHolder.getDiscounts());
    }
}
