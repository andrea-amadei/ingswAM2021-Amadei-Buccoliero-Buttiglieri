package it.polimi.ingsw.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.common.GameConfig;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.parser.adapters.*;
import it.polimi.ingsw.client.updates.Update;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.InvalidFaithPathException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.parser.raw.list.*;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.ForegroundColor;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import it.polimi.ingsw.server.clienthandling.setupactions.SetupAction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class JSONParser {
    private static boolean SHOW_LOGS = true;
    private static boolean DEBUG_MODE = false;
    private static boolean BEST_EFFORT_MODE = true;

    private static final Gson gson = new Gson();

    private JSONParser() { }

    public static void setShowLogs(boolean showLogs) {
        SHOW_LOGS = showLogs;
    }

    public static void setDebugMode(boolean debugMode) {
        DEBUG_MODE = debugMode;
    }

    public static void setBestEffortMode(boolean bestEffortMode) {
        BEST_EFFORT_MODE = bestEffortMode;
    }

    private static void errorHandler(String message) throws ParserException {
        if(BEST_EFFORT_MODE)
            Logger.log(message + ". Skipped...", Logger.Severity.WARNING, ForegroundColor.YELLOW);
        else
            throw new ParserException(message);
    }

    public static <O extends UniqueSerializableObject<?>, R extends UniqueRawObject<O>, L extends OrderedRawList<R>> List<O> parseOrderedList(String json, String description, Class<L> rawListClass) throws ParserException {
        L rawList;
        O object;
        List<O> list = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        int successful = 0, skipped = 0;

        // PHASE 1: GET RAW OBJECTS
        if(SHOW_LOGS)
            Logger.log("JSON PARSER - Parsing " + description.toLowerCase() + " (phase 1/2) ...");

        try {
            rawList = gson.fromJson(json, rawListClass);
        } catch (JsonSyntaxException e) {
            throw new ParserException(e.getMessage());
        }

        // test if the list is working
        if(rawList.getList() == null || rawList.getList().size() == 0)
            throw new ParserException("Unable to parse any card");

        // PHASE 2: CONVERT OBJECTS
        if(SHOW_LOGS)
            Logger.log("JSON PARSER - Converting raw data from " + description.toLowerCase() + " (phase 2/2) ...");

        // for each raw object
        for(R rawObject : rawList.getList()) {
            // check if id is unique
            if (ids.contains(rawObject.getStringId())) {
                errorHandler("Duplicate object property \"id\" " + rawObject.getStringId());
                skipped++;
                continue;
            } else
                ids.add(rawObject.getStringId());

            // convert object
            try {
                object = rawObject.toObject();
            } catch (IllegalRawConversionException e) {
                errorHandler(e.getMessage() + " (id: " + rawObject.getStringId() + ")");
                skipped++;
                continue;
            }

            // add to list
            list.add(object);

            // print debug messages
            if(SHOW_LOGS && DEBUG_MODE) {
                Logger.log("----- " + description + " " + (rawList.getList().indexOf(rawObject) + 1) + " -----");
                object.printDebugInfo();
            }

            successful++;
        }

        // Sort the list based on string ids
        list.sort(Comparator.comparing(a -> a.getStringId()));

        // DONE!
        if(SHOW_LOGS)
            Logger.log("JSON PARSER - Parsing of " + description + " done! Successful = " + successful + ", Skipped = " + skipped);

        return list;
    }

    public static <O extends UniqueSerializableObject<?>, R extends UniqueRawObject<O>, L extends OrderedRawList<R>> List<O> parseOrderedList(Path path, String description, Class<L> rawListClass) throws ParserException, IOException {
        // test for null or non existing file
        if(path == null)
            throw new NullPointerException();

        // test if file exists
        if(!Files.exists(path))
            throw new FileNotFoundException("File does not exists");

        return parseOrderedList(Files.readString(path), description, rawListClass);
    }

    public static <O extends SerializableObject<?>, R extends RawObject<O>, L extends RawList<R>> List<O> parseList(String json, String description, Class<L> rawListClass) throws ParserException {
        L rawList;
        O object;
        List<O> list = new ArrayList<>();

        int successful = 0, skipped = 0;

        // PHASE 1: GET RAW OBJECTS
        if(SHOW_LOGS)
            Logger.log("JSON PARSER - Parsing " + description.toLowerCase() + " (phase 1/2) ...");

        try {
            rawList = gson.fromJson(json, rawListClass);
        } catch (JsonSyntaxException e) {
            throw new ParserException(e.getMessage());
        }

        // test if the list is working
        if(rawList.getList() == null || rawList.getList().size() == 0)
            throw new ParserException("Unable to parse any card");

        // PHASE 2: CONVERT OBJECTS
        if(SHOW_LOGS)
            Logger.log("JSON PARSER - Converting raw data from " + description.toLowerCase() + " (phase 2/2) ...");

        // for each raw object
        for(R rawObject : rawList.getList()) {
            // convert object
            try {
                object = rawObject.toObject();
            } catch (IllegalRawConversionException e) {
                errorHandler(e.getMessage());
                skipped++;
                continue;
            }

            // add to list
            list.add(object);

            // print debug messages
            if(SHOW_LOGS && DEBUG_MODE) {
                Logger.log("----- " + description + " " + (rawList.getList().indexOf(rawObject) + 1) + " -----");
                object.printDebugInfo();
            }

            successful++;
        }

        // DONE!
        if(SHOW_LOGS)
            Logger.log("JSON PARSER - Parsing of " + description + " done! Successful = " + successful + ", Skipped = " + skipped);

        return list;
    }

    public static <O extends SerializableObject<?>, R extends RawObject<O>, L extends RawList<R>> List<O> parseList(Path path, String description, Class<L> rawListClass) throws ParserException, IOException {
        // test for null or non existing file
        if(path == null)
            throw new NullPointerException();

        // test if file exists
        if(!Files.exists(path))
            throw new FileNotFoundException("File does not exists");

        return parseList(Files.readString(path), description, rawListClass);
    }

    public static <O extends SerializableObject<?>, R extends RawObject<O>> O parse(String json, Class<R> rawObjectClass) throws ParserException, IllegalRawConversionException {
        if(json == null)
            throw new NullPointerException();

        try {
            return gson.fromJson(json, rawObjectClass).toObject();
        } catch (JsonSyntaxException e) {
            throw new ParserException(e.getMessage());
        }
    }

    public static List<LeaderCard> parseLeaders(Path path) throws ParserException, IOException {
        return parseOrderedList(path, "Leaders", RawLeaderCardList.class);
    }

    public static List<LeaderCard> parseLeaders(String json) throws ParserException {
        return parseOrderedList(json, "Leaders", RawLeaderCardList.class);
    }

    public static List<CraftingCard> parseCraftingCards(Path path) throws ParserException, IOException {
        return parseOrderedList(path, "Crafting Cards", RawCraftingCardList.class);
    }

    public static List<CraftingCard> parseCraftingCards(String json) throws ParserException {
        return parseOrderedList(json, "Crafting Cards", RawCraftingCardList.class);
    }

    public static List<Crafting> parseBaseCrafting(Path path) throws ParserException, IOException {
        return parseList(path, "Base Crafting", RawCraftingList.class);
    }

    public static List<Crafting> parseBaseCrafting(String json) throws ParserException {
        return parseList(json, "Base Crafting", RawCraftingList.class);
    }

    public static FaithPath parseFaithPath(Path path) throws ParserException, IOException {
        List<FaithPathTile> tiles = parseOrderedList(path, "Faith Path Tiles", RawFaithPathTileList.class);
        List<FaithPathGroup> groups = parseOrderedList(path, "Faith Path Group", RawFaithPathGroupList.class);

        try {
            return new FaithPath(groups, tiles);
        } catch (IllegalArgumentException | InvalidFaithPathException e) {
            throw new ParserException("Faith Path: " + e.getMessage());
        }
    }

    public static FaithPath parseFaithPath(String json) throws ParserException {
        List<FaithPathTile> tiles = parseOrderedList(json, "Faith Path Tiles", RawFaithPathTileList.class);
        List<FaithPathGroup> groups = parseOrderedList(json, "Faith Path Group", RawFaithPathGroupList.class);

        try {
            return new FaithPath(groups, tiles);
        } catch (IllegalArgumentException | InvalidFaithPathException e) {
            throw new ParserException("Faith Path: " + e.getMessage());
        }
    }

    public static ClientNetworkObject getClientNetworkObject(String json){
        Gson clientNetworkObjGson = new GsonBuilder()
                .registerTypeAdapter(ClientNetworkObject.class, new ClientNetworkObjectAdapter())
                .registerTypeAdapter(SetupAction.class, new SetupActionAdapter())
                .registerTypeAdapter(Action.class, new ActionAdapter())
                .registerTypeAdapter(ResourceSingle.class, new ResourceSingleAdapter())
                .registerTypeAdapter(ResourceGroup.class, new ResourceGroupAdapter())
                .registerTypeAdapter(ResourceType.class, new ResourceTypeDeserializer())
                .create();

        return clientNetworkObjGson.fromJson(json, ClientNetworkObject.class);
    }

    public static ServerNetworkObject getServerNetworkObject(String json){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ServerNetworkObject.class, new ServerNetworkObjectAdapter())
                .registerTypeAdapter(Update.class, new UpdateAdapter())
                .registerTypeAdapter(ResourceSingle.class, new ResourceSingleAdapter())
                .registerTypeAdapter(ResourceGroup.class, new ResourceGroupAdapter())
                .registerTypeAdapter(ResourceType.class, new ResourceTypeDeserializer())
                .create();

        return gson.fromJson(json, ServerNetworkObject.class);
    }

    public static List<ServerNetworkObject> getServerNetworkObjects(String json){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ServerNetworkObject.class, new ServerNetworkObjectAdapter())
                .registerTypeAdapter(Update.class, new UpdateAdapter())
                .registerTypeAdapter(ResourceSingle.class, new ResourceSingleAdapter())
                .registerTypeAdapter(ResourceGroup.class, new ResourceGroupAdapter())
                .registerTypeAdapter(ResourceType.class, new ResourceTypeDeserializer())
                .create();

        Type serverNetworkObjListType = new TypeToken<List<ServerNetworkObject>>(){}.getType();
        return gson.fromJson(json, serverNetworkObjListType);
    }

    public static GameConfig getGameConfig(String json){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ResourceSingle.class, new ResourceSingleAdapter())
                .registerTypeAdapter(ResourceGroup.class, new ResourceGroupAdapter())
                .registerTypeAdapter(ResourceType.class, new ResourceTypeDeserializer())
                .create();

        return gson.fromJson(json, GameConfig.class);
    }

    public static GameConfig getGameConfig(Path path) throws IOException {
        return getGameConfig(Files.readString(path));
    }
}
