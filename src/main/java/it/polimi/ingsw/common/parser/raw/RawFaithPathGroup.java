package it.polimi.ingsw.common.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.server.model.faithpath.FaithPathGroup;
import it.polimi.ingsw.common.parser.UniqueRawObject;

public class RawFaithPathGroup implements UniqueRawObject<FaithPathGroup> {

    @SerializedName(value = "group", alternate = "id")
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

    public RawFaithPathGroup(int group, int points) {
        this.group = group;
        this.points = points;
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
        if(group == null)
            throw new IllegalRawConversionException("Missing mandatory field \"group\" in faith path group");

        if(points == null)
            throw new IllegalRawConversionException("Missing mandatory field \"points\" in faith path group");

        try {
            return new FaithPathGroup(group, points);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }

    }
}
