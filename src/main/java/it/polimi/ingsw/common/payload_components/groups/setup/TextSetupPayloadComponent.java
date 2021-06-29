package it.polimi.ingsw.common.payload_components.groups.setup;

import it.polimi.ingsw.common.annotations.SerializedType;

@SerializedType("text")
public class TextSetupPayloadComponent extends SetupPayloadComponent{
    
    private final String text;
    
    public TextSetupPayloadComponent(String text){
        if(text == null)
            throw new NullPointerException();
        this.text = text;
    }
    
    public String getText() {return text;}
}
