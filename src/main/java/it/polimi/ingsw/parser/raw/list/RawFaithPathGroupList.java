package it.polimi.ingsw.parser.raw.list;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.parser.OrderedRawList;
import it.polimi.ingsw.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.parser.raw.RawFaithPathTile;

import java.util.List;

public class RawFaithPathGroupList implements OrderedRawList<RawFaithPathGroup> {
    @SerializedName(value = "groups", alternate = {"faith_path_groups", "faithPathGroups"})
    private List<RawFaithPathGroup> groups;

    @Override
    public List<RawFaithPathGroup> getList() {
        return groups;
    }
}
