package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.parser.RawObject;
import it.polimi.ingsw.parser.UniqueRawObject;

public class RawFaithPathGroup implements UniqueRawObject<FaithPathGroup> {

    @SerializedName("group")
    private Integer group;

    @SerializedName("points")
    private Integer points;

    public RawFaithPathGroup() { }

    public RawFaithPathGroup(FaithPathGroup faithPathGroup) {
        if(faithPathGroup == null)
            throw new NullPointerException();

        group = faithPathGroup.getGroup();
        points = faithPathGroup.getPoints();
    }

    public int getGroup() {
        return group;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String getStringId() {
        return String.format("%03d", group);
    }

    @Override
    public FaithPathGroup toObject() throws IllegalRawConversionException {
        return null;
    }
}
