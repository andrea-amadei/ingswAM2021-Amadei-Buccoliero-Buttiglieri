package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DummyBuilder {
    private static final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private static final ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
    private static final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
    private static final ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
    private static final ResourceGroup any = ResourceTypeSingleton.getInstance().getAnyResource();

    public static List<LeaderCard> buildLeaderCards(){
        try {
            return JSONParser.parseLeaders(Paths.get("src/main/leaders.json"));
        }catch(IOException | ParserException e1){
            Console.log(e1.getMessage(), Console.Severity.ERROR);
            return null;
        }
    }

    public static List<CraftingCard> buildCraftingCards(){
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
    }
}
