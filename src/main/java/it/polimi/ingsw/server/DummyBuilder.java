package it.polimi.ingsw.server;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.fsm.states.MenuState;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleFactory;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.model.storage.*;
import it.polimi.ingsw.parser.JSONParser;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public final class DummyBuilder {
    private static final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private static final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
    private static final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private static final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
    private static final ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

    public static List<LeaderCard> buildLeaderCards(){
        JSONParser.setShowLogs(false);
        try {
            return JSONParser.parseLeaders(Paths.get("src/main/leaders.json"));
        }catch(IOException | ParserException e1){
            Logger.log(e1.getMessage(), Logger.Severity.ERROR);
            return null;
        }
    }

    public static List<CraftingCard> buildCraftingCards(){
        JSONParser.setShowLogs(false);
        Map<ResourceType, Integer> map1 = new HashMap<>(){{
            put(gold, 2);
            put(servant, 1);
        }};
        Map<ResourceSingle, Integer> map2 = new HashMap<>(){{
            put(shield, 2);
            put(stone, 1);
        }};

        List<CraftingCard> craftingCards = new ArrayList<>();
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(map1, map1, 0, 1);
        for(int i = 0; i < GameParameters.MARKET_ROWS; i++)
            for(int j = 0; j < GameParameters.MARKET_COLUMNS; j++){
                craftingCards.add(new CraftingCard(1, new LevelFlag(FlagColor.values()[j], i+1), map2, new UpgradableCrafting(map1, map1, 0, i+1), 10));
                craftingCards.add(new CraftingCard(2, new LevelFlag(FlagColor.values()[j], i+1), map2, new UpgradableCrafting(map1, map1, 0, i+1), 10));
                craftingCards.add(new CraftingCard(3, new LevelFlag(FlagColor.values()[j], i+1), map2, new UpgradableCrafting(map1, map1, 0, i+1), 10));
            }

        return craftingCards;
    }

    public static List<FaithPathTile> buildFaithPathTiles(){
        JSONParser.setShowLogs(false);
        List<FaithPathTile> faithTiles = new ArrayList<>();
        int i = 0;
        for(; i < 3; i++){
            faithTiles.add(new FaithPathTile(i+1, 1, i, 4, 0, false));
        }
        for(; i < 6; i++)
            faithTiles.add(new FaithPathTile(i+1, 1, i, 4, 1, false));
        faithTiles.add(new FaithPathTile(i+1, 1, i, 4, 1, true));
        i++;

        for(; i < 10; i++){
            faithTiles.add(new FaithPathTile(i+1, 1, i, 4, 0, false));
        }
        for(; i < 12; i++){
            faithTiles.add(new FaithPathTile(i+1, 1, i, 4, 2, false));
        }
        faithTiles.add(new FaithPathTile(i+1, 1, i, 4, 2, true));

        return faithTiles;

        //Order:  0 1 2 3 4 5 6 7 8 9 10 11 12
        //Points: 4 4 4 4 4 4 4 4 4 4 4  4  4
        //Checks:             X             X
    }

    public static GameModel buildDummyModel(List<String> usernames, Random seededRandom){

        if(usernames == null || seededRandom == null)
            throw new NullPointerException();


        //--------------------------------------------------------------------------------------------------------------
        //creating the players
        //--------------------------------------------------------------------------------------------------------------
        List<Player> players = new ArrayList<>();
        for(String username : usernames){

            //creating the base storages
            BaseStorage hand = new BaseStorage("Hand");
            BaseStorage marketBasket = new BaseStorage("MarketBasket");
            BaseStorage chest = new BaseStorage("Chest");

            //creating the base Shelves (in the real builder these are read from json)
            List<Shelf> baseShelves = new ArrayList<>();
            for(int i = 0; i < GameParameters.BASE_CUPBOARD_SHELF_NAMES.size(); i++){
                baseShelves.add(new Shelf(GameParameters.BASE_CUPBOARD_SHELF_NAMES.get(i),
                        GameParameters.BASE_CUPBOARD_SHELF_TYPES.get(i),
                        GameParameters.BASE_CUPBOARD_SHELF_SIZES.get(i)));
            }

            //creating the player storage using the previously created objects
            Storage storage = new Storage(chest, hand, marketBasket, baseShelves);


            //creating the player production (the parameter will be read from json)
            Production production = new Production(GameParameters.UPGRADABLE_CRAFTING_NUMBER);

            //creating the player faithHolder (the parameter will be read from json)
            FaithHolder faithHolder = new FaithHolder(GameParameters.FAITH_CHECKPOINT_NUMBER);

            //creating the player board
            Board board = new Board(storage, production, faithHolder);

            players.add(new Player(username, usernames.indexOf(username), board));
        }

        //--------------------------------------------------------------------------------------------------------------
        //creating the market
        //--------------------------------------------------------------------------------------------------------------

        //creating all marbles (read from json how how many marbles for each color need to be created)
        List<Marble> marbles = new ArrayList<>();
        for(MarbleColor color : MarbleColor.values()){
            for(int i = 0; i < GameParameters.MARBLE_PER_COLOR.get(color); i++)
                marbles.add(MarbleFactory.createMarble(color));
        }

        //shuffle all marbles
        Collections.shuffle(marbles, seededRandom);

        //all marbles except the last one are put in the grid. The last one is set as the odd one
        List<Marble> marblesInGrid = marbles.subList(0, marbles.size() - 1);
        Marble oddOne = marbles.get(marbles.size() - 1);

        //create the market instance (the two last parameters are read from json)
        Market market = new Market(marblesInGrid, oddOne, GameParameters.MARKET_ROWS, GameParameters.MARKET_COLUMNS);


        //--------------------------------------------------------------------------------------------------------------
        //creating the shop
        //--------------------------------------------------------------------------------------------------------------

        //creating the shop instance (the parameter is read from json)
        Shop shop = new Shop(GameParameters.MAX_CARD_LEVEL);

        //add crafting cards (use the real parser to read crafting cards)
        for(CraftingCard card : DummyBuilder.buildCraftingCards())
            shop.addCard(card);

        //--------------------------------------------------------------------------------------------------------------
        //creating the faith path
        //--------------------------------------------------------------------------------------------------------------

        //creating tiles (read from json with the real parser)
        List<FaithPathTile> tiles = DummyBuilder.buildFaithPathTiles();

        //creating groups (read from json with the real parser)
        List<FaithPathGroup> groupPoints = new ArrayList<>(){{
            add(new FaithPathGroup(1, 3));
            add(new FaithPathGroup(2, 6));
        }};

        //creating the faith path instance. The parameter "isSinglePlayer" needs to be read from json
        FaithPath faithPath = new FaithPath(groupPoints, tiles, false);


        //--------------------------------------------------------------------------------------------------------------
        //creating the leader cards
        //--------------------------------------------------------------------------------------------------------------

        //creating the list (use the real parser)
        List<LeaderCard> leaderCards = DummyBuilder.buildLeaderCards();

        return new GameModel(players, market, shop, faithPath, leaderCards, new ArrayDeque<>());

    }

    public static StateMachine buildController(List<String> usernames, Random seededRandom, boolean isSinglePlayer, ActionQueue actionQueue){
        GameModel model = buildDummyModel(usernames, seededRandom);

        GameContext context = new GameContext(model, isSinglePlayer);

        //TODO: initialize the state machine with the initial state once developed
        StateMachine stateMachine = new StateMachine(actionQueue, context, new MenuState(context));
        model.getFaithPath().setListener(stateMachine);

        return stateMachine;
    }

}
