package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.*;
import it.polimi.ingsw.common.GameConfig;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ClientGameBuilder {
    public static void buildGame(ClientModel model, List<String> usernames, String configJson, String craftingJson, String faithJson, String leadersJson) throws ParserException {

        GameConfig gameConfig = JSONParser.getGameConfig(configJson);


        List<RawCraftingCard> craftingCards = JSONParser.parseCraftingCards(craftingJson)
                .stream()
                .map(CraftingCard::toRaw)
                .collect(Collectors.toList());

        FaithPath referenceFaithPath = JSONParser.parseFaithPath(faithJson);

        List<RawFaithPathTile> tiles = referenceFaithPath.getTiles()
                                                         .stream()
                                                         .map(FaithPathTile::toRaw)
                                                         .collect(Collectors.toList());

        List<RawFaithPathGroup> groups = referenceFaithPath.getFaithGroupList()
                                                           .stream()
                                                           .map(FaithPathGroup::toRaw)
                                                           .collect(Collectors.toList());

        List<RawLeaderCard> leaders = JSONParser.parseLeaders(leadersJson)
                                                .stream()
                                                .map(LeaderCard::toRaw)
                                                .collect(Collectors.toList());

        //--------------------------------------------------------------------------------------------------------------
        //set leader list and crafting card list
        //--------------------------------------------------------------------------------------------------------------

        model.setLeaderCards(leaders);
        model.setCraftingCards(craftingCards);

        //--------------------------------------------------------------------------------------------------------------
        //building players
        //--------------------------------------------------------------------------------------------------------------
        List<ClientPlayer> players = new ArrayList<>();
        for(String username : usernames){
            ClientBaseStorage chest = new ClientBaseStorage(new RawStorage(gameConfig.getChestId(), new HashMap<>()));
            ClientBaseStorage hand = new ClientBaseStorage(new RawStorage(gameConfig.getHandId(), new HashMap<>()));
            ClientBaseStorage basket = new ClientBaseStorage(new RawStorage(gameConfig.getBasketId(), new HashMap<>()));

            List<ClientShelf> cupboard = new ArrayList<>();
            for(int i = 0; i < gameConfig.getBaseCupboardShelfNames().size(); i++){
                cupboard.add(new ClientShelf(gameConfig.getBaseCupboardShelfNames().get(i),
                        gameConfig.getBaseCupboardShelfTypes().get(i).getId(),
                        gameConfig.getBaseCupboardShelfSizes().get(i)));
            }

            List<ClientShelf> leaderShelves = new ArrayList<>();

            ClientProduction production = new ClientProduction(gameConfig.getUpgradableCraftingNumber());

            ClientFlagHolder flagHolder = new ClientFlagHolder();

            ClientDiscountHolder discountHolder = new ClientDiscountHolder();

            ClientLeaderCards leaderCards = new ClientLeaderCards();

            ClientFaithPath faithPath = new ClientFaithPath(tiles, groups);

            players.add(new ClientPlayer(username, chest, hand, basket, cupboard, leaderShelves, production, flagHolder, discountHolder, leaderCards, faithPath));
        }

        model.setPlayers(players);

        //--------------------------------------------------------------------------------------------------------------
        //building shop and market
        //--------------------------------------------------------------------------------------------------------------
        ClientShop shop = new ClientShop(gameConfig.getMaxCardLevel(), FlagColor.values().length);

        ClientMarket market = new ClientMarket(gameConfig.getMarketRows(), gameConfig.getMarketColumns());

        model.setShop(shop);
        model.setMarket(market);
    }
}
