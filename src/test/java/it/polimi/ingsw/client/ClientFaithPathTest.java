package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientmodel.ClientFaithPath;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.faithpath.FaithPathGroup;
import it.polimi.ingsw.server.model.faithpath.FaithPathTile;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.common.parser.raw.RawFaithPathTile;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ClientFaithPathTest {

    private static List<RawFaithPathTile> tiles;
    private static List<RawFaithPathGroup> groups;

    @BeforeEach
    public void init() throws IOException, ParserException {
        JSONParser.setShowLogs(false);
        tiles = JSONParser.parseFaithPath(ResourceLoader.getPathFromResource("cfg/faith.json")).getTiles().stream().map(FaithPathTile::toRaw).collect(Collectors.toList());
        groups = JSONParser.parseFaithPath(ResourceLoader.getPathFromResource("cfg/faith.json")).getFaithGroupList().stream().map(FaithPathGroup::toRaw).collect(Collectors.toList());
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
