package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.parser.RawList;

import java.util.List;

public class RawFaithPathTileList implements RawList<RawFaithPathTile> {
    @SerializedName(value = "tiles", alternate = {"faith_path_tiles", "faithPathTiles"})
    private List<RawFaithPathTile> tiles;

    @Override
    public List<RawFaithPathTile> getList() {
        return tiles;
    }
}
