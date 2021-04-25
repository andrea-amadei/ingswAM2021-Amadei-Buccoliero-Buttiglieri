package it.polimi.ingsw.parser.raw.list;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.parser.OrderedRawList;
import it.polimi.ingsw.parser.RawList;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;

import java.util.List;

public class RawFaithPathTileList implements OrderedRawList<RawFaithPathTile> {
    @SerializedName(value = "tiles", alternate = {"faith_path_tiles", "faithPathTiles"})
    private List<RawFaithPathTile> tiles;

    @Override
    public List<RawFaithPathTile> getList() {
        return tiles;
    }
}
