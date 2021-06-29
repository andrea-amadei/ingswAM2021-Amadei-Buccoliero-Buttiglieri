package it.polimi.ingsw.common.parser.raw.list;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.parser.OrderedRawList;
import it.polimi.ingsw.common.parser.raw.RawFaithPathTile;

import java.util.List;

public class RawFaithPathTileList implements OrderedRawList<RawFaithPathTile> {
    @SerializedName(value = "tiles", alternate = {"faith_path_tiles", "faithPathTiles"})
    private List<RawFaithPathTile> tiles;

    @Override
    public List<RawFaithPathTile> getList() {
        return tiles;
    }
}
