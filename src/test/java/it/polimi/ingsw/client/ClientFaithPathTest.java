package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientFaithPath;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ClientFaithPathTest {

    private static List<RawFaithPathTile> tiles;
    private static List<RawFaithPathGroup> groups;

    @BeforeEach
    public void init() throws IOException, ParserException {
        JSONParser.setShowLogs(false);
        tiles = JSONParser.parseFaithPath(Path.of("src/main/faith.json")).getTiles().stream().map(FaithPathTile::toRaw).collect(Collectors.toList());
        groups = JSONParser.parseFaithPath(Path.of("src/main/faith.json")).getFaithGroupList().stream().map(FaithPathGroup::toRaw).collect(Collectors.toList());
    }

    @Test
    public void creation(){
        ClientFaithPath clientFaithPath = new ClientFaithPath(tiles, groups);
        assertTrue(clientFaithPath.getFaithGroups().size() > 0);
        assertTrue(clientFaithPath.getTiles().size() > 0);
        assertEquals(clientFaithPath.getFaithGroups().size(), clientFaithPath.getCheckpointStatus().size());
        assertEquals(0, clientFaithPath.getFaithPoints());

        for(FaithHolder.CheckpointStatus status : clientFaithPath.getCheckpointStatus())
            assertEquals(FaithHolder.CheckpointStatus.UNREACHED, status);
    }

    @Test
    public void addPoints(){
        ClientFaithPath clientFaithPath = new ClientFaithPath(tiles, groups);
        clientFaithPath.addFaithPoints(3);
        assertEquals(3, clientFaithPath.getFaithPoints());
    }

    @Test
    public void changeCardStatus(){
        ClientFaithPath clientFaithPath = new ClientFaithPath(tiles, groups);
        clientFaithPath.changeCardStatus(2, FaithHolder.CheckpointStatus.INACTIVE);

        for(int i = 0; i < clientFaithPath.getCheckpointStatus().size(); i++) {
            if(i != 2)
                assertEquals(FaithHolder.CheckpointStatus.UNREACHED, clientFaithPath.getCheckpointStatus().get(i));
        }

        assertEquals(FaithHolder.CheckpointStatus.INACTIVE, clientFaithPath.getCheckpointStatus().get(2));
    }

}
