package it.polimi.ingsw.server.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.leader.Requirement;
import it.polimi.ingsw.model.leader.SpecialAbility;
import it.polimi.ingsw.server.Console;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class JSONParser {
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

    public static List<LeaderCard> parseLeaders(Path path) throws ParserException, IOException {
        // test for null or non existing file
        if(path == null)
            throw new NullPointerException();

        // test if file exists
        if(!Files.exists(path))
            throw new FileNotFoundException("File does not exists");

        // get the JSON as string and parse it the normal way
        return parseLeaders(Files.readString(path));
    }

    public static List<LeaderCard> parseLeaders(String json) throws ParserException {
        RawLeaderCardList cards;
        LeaderCard newCard;
        Set<Integer> ids = new HashSet<>();
        List<LeaderCard> list = new ArrayList<>();

        int successful = 0, skipped = 0;

        Console.log("PARSING LEADERS - PHASE 1: Reading string...");

        // STEP 1: GET RAW CARDS
        try {
            cards = gson.fromJson(json, RawLeaderCardList.class);
        } catch (JsonSyntaxException e) {
            throw new ParserException(e.getMessage());
        }

        // test if the list is working
        if(cards.getList() == null)
            throw new ParserException("Unable to parse any card");

        Console.log("PARSING LEADERS - PHASE 2: Consolidating data...");

        //STEP 2: CONSOLIDATE DATA
        for(RawLeaderCard card : cards.getList()) {
            // check if id is present
            if(card.getId() == 0) {
                errorHandler("Missing mandatory card id!");
                skipped++;
                continue;
            }

            // check if id is unique
            if (ids.contains(card.getId())) {
                errorHandler("Duplicate card property \"id\" " + card.getId());
                skipped++;
                continue;
            } else
                ids.add(card.getId());

            // convert card
            try {
                newCard = card.toLeaderCard();
            } catch (IllegalRawConversionException e) {
                errorHandler(e.getMessage());
                skipped++;
                continue;
            }

            // add to list
            list.add(newCard);

            if(DEBUG_MODE) {
                Console.log("----- CARD " + (cards.getList().indexOf(card) + 1) + " -----");

                Console.log("Id: " + newCard.getId());
                Console.log("Name: " + newCard.getName());
                Console.log("Points: " + newCard.getPoints());

                Console.log("Requirements: " + newCard.getRequirements().size());
                for(Requirement i : newCard.getRequirements())
                    Console.log("  - " + i.toString());

                Console.log("Special abilities: " + newCard.getAbilities().size());
                for(SpecialAbility i : newCard.getAbilities())
                    Console.log("  - " + i.toString());
            }

            successful++;
        }

        Console.log("PARSING DONE! Successful = " + successful + ", Skipped = " + skipped);

        return list;
    }
}
