package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.parser.raw.RawLevelFlag;

public class AddFlagUpdate implements Update{

    private final RawLevelFlag flag;
    private final String player;

    public AddFlagUpdate(RawLevelFlag flag, String player){
        this.flag = flag;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        clientPlayer.getFlagHolder().addFlag(flag);
    }

    @Override
    public void checkFormat() {
        if(flag == null)
            throw new NullPointerException("pointer to flag is null");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public RawLevelFlag getFlag() {
        return flag;
    }

    public String getPlayer() {
        return player;
    }
}
