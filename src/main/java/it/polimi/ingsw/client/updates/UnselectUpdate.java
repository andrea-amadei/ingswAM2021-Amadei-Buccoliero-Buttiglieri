package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ClientShelf;

import java.util.NoSuchElementException;

public class UnselectUpdate implements Update{

    private final String section;
    private final String player;

    public UnselectUpdate(String section, String player){
        this.section = section;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);

        switch(section){
            case "shop":
                client.getShop().unselect();
                break;
            case "storage":
                for(ClientShelf shelf : clientPlayer.getCupboard())
                    shelf.unselect();
                for(ClientShelf leaderShelf : clientPlayer.getLeaderShelves())
                    leaderShelf.unselect();
                clientPlayer.getChest().unselect();
                break;
            case "production":
                clientPlayer.getProduction().unselect();
                break;
            default:
                throw new NoSuchElementException("There is no section \"" + section + "\"");

        }
    }

    @Override
    public void checkFormat() {
        if(section == null)
            throw new NullPointerException("Pointer to section is null");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public String getSection() {
        return section;
    }

    public String getPlayer() {
        return player;
    }
}
