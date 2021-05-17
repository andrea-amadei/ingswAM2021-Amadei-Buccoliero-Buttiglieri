package it.polimi.ingsw.common.payload_components.groups.actions;

import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.model.actions.SelectPlayAction;

@SerializedType("select_play")
public class SelectPlayActionPayloadComponent extends ActionPayloadComponent{

    private final SelectPlayAction.Play play;

    public SelectPlayActionPayloadComponent(String player, SelectPlayAction.Play play) {
        super(player);
        this.play = play;
    }

    public SelectPlayAction.Play getPlay() {
        return play;
    }
}
