package it.polimi.ingsw.common.parser.raw.list;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.parser.OrderedRawList;
import it.polimi.ingsw.common.parser.raw.RawFaithPathGroup;

import java.util.List;

public class RawFaithPathGroupList implements OrderedRawList<RawFaithPathGroup> {
    @SerializedName(value = "groups", alternate = {"faith_path_groups", "faithPathGroups"})
    private List<RawFaithPathGroup> groups;

    @Override
    public List<RawFaithPathGroup> getList() {
        return groups;
    }
}
