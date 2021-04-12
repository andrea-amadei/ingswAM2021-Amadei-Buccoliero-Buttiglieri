package it.polimi.ingsw.server.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.server.parser.raw.RawCraftingCardList;
import it.polimi.ingsw.server.parser.raw.RawLeaderCardList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class JSONParser {
    private static final boolean SHOW_LOGS = true;
    private static final boolean DEBUG_MODE = true;
    private static final boolean BEST_EFFORT_MODE = true;

    private static final Gson gson = new Gson();

    private JSONParser() { }

    private static void errorHandler(String message) throws ParserException {
        if(BEST_EFFORT_MODE)
            Console.log(message + ". Skipped...", Console.Severity.WARNING, Console.Format.YELLOW);
        else
            throw new ParserException(message);
    }

    public static <O extends SerializedObject, R extends UniqueRawObject<O>, L extends RawList<R>> List<O> parse(String json, String description, Class<L> rawListClass) throws ParserException {
        L rawList;
        O object;
        List<O> list = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();

        int successful = 0, skipped = 0;

        // PHASE 1: GET RAW OBJECTS
        if(SHOW_LOGS)
            Console.log("JSON PARSER - Parsing " + description.toLowerCase() + " (phase 1/2) ...");

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
            Console.log("JSON PARSER - Converting raw data from " + description.toLowerCase() + " (phase 2/2) ...");

        // for each raw object
        for(R rawObject : rawList.getList()) {
            // check if id is present
            if(rawObject.getId() == 0) {
                errorHandler("Missing mandatory object id!");
                skipped++;
                continue;
            }

            // check if id is unique
            if (ids.contains(rawObject.getId())) {
                errorHandler("Duplicate object property \"id\" " + rawObject.getId());
                skipped++;
                continue;
            } else
                ids.add(rawObject.getId());

            // convert object
            try {
                object = rawObject.convert();
            } catch (IllegalRawConversionException e) {
                errorHandler(e.getMessage());
                skipped++;
                continue;
            }

            // add to list
            list.add(object);

            // print debug messages
            if(SHOW_LOGS && DEBUG_MODE) {
                Console.log("----- " + description + " " + (rawList.getList().indexOf(rawObject) + 1) + " -----");
                object.printDebugInfo();
            }

            successful++;
        }

        // DONE!
        if(SHOW_LOGS)
            Console.log("JSON PARSER - Parsing of " + description + " done! Successful = " + successful + ", Skipped = " + skipped);

        return list;
    }

    public static <O extends SerializedObject, R extends UniqueRawObject<O>, L extends RawList<R>> List<O> parse(Path path, String description, Class<L> rawListClass) throws ParserException, IOException {
        // test for null or non existing file
        if(path == null)
            throw new NullPointerException();

        // test if file exists
        if(!Files.exists(path))
            throw new FileNotFoundException("File does not exists");

        return parse(Files.readString(path), description, rawListClass);
    }

    public static List<LeaderCard> parseLeaders(Path path) throws ParserException, IOException {
        return parse(path, "Leaders", RawLeaderCardList.class);
    }

    public static List<LeaderCard> parseLeaders(String json) throws ParserException {
        return parse(json, "Leaders", RawLeaderCardList.class);
    }

    public static List<CraftingCard> parseCraftingCards(Path path) throws ParserException, IOException {
        return parse(path, "Crafting Cards", RawCraftingCardList.class);
    }

    public static List<CraftingCard> parseCraftingCards(String json) throws ParserException {
        return parse(json, "Crafting Cards", RawCraftingCardList.class);
    }
}
