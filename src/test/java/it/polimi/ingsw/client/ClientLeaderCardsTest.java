package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientLeaderCards;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawLeaderCard;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ClientLeaderCardsTest {

    private static List<RawLeaderCard> leaders;


    @BeforeEach
    public void init(){
        try {
            leaders = JSONParser.parseLeaders(ResourceLoader.getPathFromResource("cfg/leaders.json")).stream().map(LeaderCard::toRaw).collect(Collectors.toList());
        } catch (ParserException | IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void creation(){
        ClientLeaderCards lCards = new ClientLeaderCards();
        assertEquals(0, lCards.getLeaderCards().size());
        assertEquals(0, lCards.getCoveredCards());
    }

    @Test
    public void addLeaderCard(){
        ClientLeaderCards lCards = new ClientLeaderCards();

        lCards.addLeaderCard(leaders.get(5));
        assertEquals(leaders.get(5), lCards.getLeaderCards().get(0));
    }

    @Test
    public void removeLeaderCard(){
        ClientLeaderCards lCards = new ClientLeaderCards();

        lCards.addLeaderCard(leaders.get(5));
        lCards.removeLeaderCard(0);

        assertEquals(0, lCards.getLeaderCards().size());
    }

    @Test
    public void removeLeaderCardById(){
        ClientLeaderCards lCards = new ClientLeaderCards();

        lCards.addLeaderCard(leaders.get(5));
        lCards.removeLeaderCardById(6);

        assertEquals(0, lCards.getLeaderCards().size());
    }

    @Test
    public void activateLeaderCard(){
        ClientLeaderCards lCards = new ClientLeaderCards();

        lCards.addLeaderCard(leaders.get(5));
        lCards.addLeaderCard(leaders.get(7));

        lCards.activateLeaderCard(1);
        assertTrue(lCards.getActivatedLeaderCardIndexes().contains(1));
    }

    @Test
    public void activateLeaderCardById(){
        ClientLeaderCards lCards = new ClientLeaderCards();

        lCards.addLeaderCard(leaders.get(5));
        lCards.addLeaderCard(leaders.get(7));

        lCards.activateLeaderCardsById(8);
        assertTrue(lCards.getActivatedLeaderCardIndexes().contains(1));
    }

    @Test
    public void changeCoveredCards(){
        ClientLeaderCards lCards = new ClientLeaderCards();

        lCards.changeCoveredCardsNumber(3);
        assertEquals(3, lCards.getCoveredCards());

        lCards.changeCoveredCardsNumber(-3);
        assertEquals(0, lCards.getCoveredCards());
    }
}