package it.polimi.ingsw.server;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.GameConfig;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.faithpath.FaithPath;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.StateMachine;
import it.polimi.ingsw.server.model.fsm.states.SetupState;
import it.polimi.ingsw.server.model.global.Board;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.global.Shop;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.model.leader.LeaderCard;
import it.polimi.ingsw.server.model.lorenzo.AddFaithToken;
import it.polimi.ingsw.server.model.lorenzo.DiscardToken;
import it.polimi.ingsw.server.model.lorenzo.ShuffleToken;
import it.polimi.ingsw.server.model.lorenzo.Token;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleFactory;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.production.CraftingCard;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.BaseStorage;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import it.polimi.ingsw.common.parser.JSONParser;

import java.util.*;

public class ServerBuilder {

    public static List<LeaderCard> buildLeaderCards(String json) throws ParserException {
        JSONParser.setShowLogs(false);
        try {
            return JSONParser.parseLeaders(json);
        }catch(ParserException e1){
            Logger.log(e1.getMessage(), Logger.Severity.ERROR);
            throw e1;
        }
    }

    public static List<CraftingCard> buildCraftingCards(String json) throws ParserException {

        if(json == null)
            throw new NullPointerException();
        JSONParser.setShowLogs(false);
        try {
            return JSONParser.parseCraftingCards(json);
        }catch(ParserException e1){
            Logger.log(e1.getMessage(), Logger.Severity.ERROR);
            throw e1;
        }
    }

    public static FaithPath buildFaithPath(String json) throws ParserException {
        JSONParser.setShowLogs(false);
        try {
            return JSONParser.parseFaithPath(json);
        }catch(ParserException e1){
            Logger.log(e1.getMessage(), Logger.Severity.ERROR);
            throw e1;
        }
    }

    public static GameConfig buildGameConfig(String json){
        JSONParser.setShowLogs(false);
        return JSONParser.getGameConfig(json);
    }


    public static GameModel buildModel(String config, String crafting, String faith, String leaders, List<String> usernames, boolean isSinglePlayer, Random seededRandom) throws ParserException {
        if(usernames == null || seededRandom == null)
            throw new NullPointerException();

        //--------------------------------------------------------------------------------------------------------------
        //loading config
        //--------------------------------------------------------------------------------------------------------------

        GameConfig gameConfig = JSONParser.getGameConfig(config);

        //--------------------------------------------------------------------------------------------------------------
        //creating the players
        //--------------------------------------------------------------------------------------------------------------
        List<Player> players = new ArrayList<>();
        for(String username : usernames){

            //creating the base storages
            BaseStorage hand = new BaseStorage(gameConfig.getHandId());
            BaseStorage marketBasket = new BaseStorage(gameConfig.getBasketId());
            BaseStorage chest = new BaseStorage(gameConfig.getChestId());

            //creating the base Shelves
            List<Shelf> baseShelves = new ArrayList<>();
            for(int i = 0; i < gameConfig.getBaseCupboardShelfNames().size(); i++){
                baseShelves.add(new Shelf(gameConfig.getBaseCupboardShelfNames().get(i),
                        gameConfig.getBaseCupboardShelfTypes().get(i),
                        gameConfig.getBaseCupboardShelfSizes().get(i)));
            }

            //creating the player storage using the previously created objects
            Storage storage = new Storage(chest, hand, marketBasket, baseShelves);


            //creating the player production
            Production production = new Production(gameConfig.getUpgradableCraftingNumber());

            //creating the player faithHolder
            FaithHolder faithHolder = new FaithHolder(gameConfig.getFaithCheckpointNumber());

            //creating the player board
            Board board = new Board(storage, production, faithHolder);

            players.add(new Player(username, usernames.indexOf(username), board));
        }

        //--------------------------------------------------------------------------------------------------------------
        //creating the market
        //--------------------------------------------------------------------------------------------------------------

        //creating all marbles
        List<Marble> marbles = new ArrayList<>();
        for(MarbleColor color : MarbleColor.values()){
            for(int i = 0; i < gameConfig.getMarblePerColor().get(color); i++)
                marbles.add(MarbleFactory.createMarble(color));
        }

        //shuffle all marbles
        Collections.shuffle(marbles, seededRandom);

        //all marbles except the last one are put in the grid. The last one is set as the odd one
        List<Marble> marblesInGrid = marbles.subList(0, marbles.size() - 1);
        Marble oddOne = marbles.get(marbles.size() - 1);

        //create the market instance
        Market market = new Market(marblesInGrid, oddOne, gameConfig.getMarketRows(), gameConfig.getMarketColumns());

        //--------------------------------------------------------------------------------------------------------------
        //creating the shop
        //--------------------------------------------------------------------------------------------------------------

        //creating the shop instance
        Shop shop = new Shop(gameConfig.getMaxCardLevel());

        //add crafting cards
        for(CraftingCard card : buildCraftingCards(crafting))
            shop.addCard(card);


        //--------------------------------------------------------------------------------------------------------------
        //creating the faith path
        //--------------------------------------------------------------------------------------------------------------

        //creating the faith path instance. The parameter "isSinglePlayer" needs to be read from json
        FaithPath faithPath = buildFaithPath(faith);

        faithPath.setSinglePlayer(isSinglePlayer);


        //--------------------------------------------------------------------------------------------------------------
        //creating the leader cards
        //--------------------------------------------------------------------------------------------------------------

        //creating the list (use the real parser)
        List<LeaderCard> leaderCards = buildLeaderCards(leaders);

        //--------------------------------------------------------------------------------------------------------------
        //creating the tokens if the game is single player (otherwise tokens are an empty deque)
        //--------------------------------------------------------------------------------------------------------------
        Deque<Token> tokens = new ArrayDeque<>();

        if(isSinglePlayer){
            tokens.add(new ShuffleToken(1));
            tokens.add(new DiscardToken(FlagColor.YELLOW, 2));
            tokens.add(new DiscardToken(FlagColor.BLUE, 2));
            tokens.add(new DiscardToken(FlagColor.GREEN, 2));
            tokens.add(new DiscardToken(FlagColor.PURPLE, 2));
            tokens.add(new AddFaithToken(2));
        }
        return new GameModel(players, market, shop, faithPath, leaderCards, tokens);

    }

    public static StateMachine buildStateMachine(String config, String crafting, String faith, String leaders, List<String> usernames, Random seededRandom, boolean isSinglePlayer, ActionQueue actionQueue) throws ParserException {
        GameModel model = buildModel(config, crafting, faith, leaders, usernames, isSinglePlayer, seededRandom);

        GameContext gameContext = new GameContext(model, isSinglePlayer);

        StateMachine stateMachine = new StateMachine(actionQueue, gameContext, new SetupState(gameContext));
        model.getFaithPath().setListener(stateMachine);
        for(Token t : model.getLorenzoTokens())
            t.setListener(stateMachine);

        return stateMachine;
    }

    public static StateMachine buildCustomStateMachine(String config, String crafting, String faith, String leaders, List<String> usernames, Random seededRandom, boolean isSinglePlayer, ActionQueue actionQueue) throws ParserException{
        GameModel model = buildModel(config, crafting, faith, leaders, usernames, isSinglePlayer, seededRandom);
        GameContext gameContext = new GameContext(model, isSinglePlayer, config, crafting, faith, leaders);

        StateMachine stateMachine = new StateMachine(actionQueue, gameContext, new SetupState(gameContext));
        model.getFaithPath().setListener(stateMachine);
        for(Token t : model.getLorenzoTokens())
            t.setListener(stateMachine);

        return stateMachine;
    }
}
