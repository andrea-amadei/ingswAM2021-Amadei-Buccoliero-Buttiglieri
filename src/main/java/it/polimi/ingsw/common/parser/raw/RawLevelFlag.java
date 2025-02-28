package it.polimi.ingsw.common.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.basetypes.LevelFlag;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.RawObject;

public class RawLevelFlag implements RawObject<LevelFlag> {

    @SerializedName(value = "color", alternate = "flag")
    private FlagColor color;

    @SerializedName("level")
    private Integer level;

    public RawLevelFlag() { }

    public RawLevelFlag(LevelFlag levelFlag) {
        if(levelFlag == null)
            throw new NullPointerException();

        color = levelFlag.getColor();
        level = levelFlag.getLevel();
    }

    public FlagColor getColor() {
        return color;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public LevelFlag toObject() throws IllegalRawConversionException {
        if(color == null)
            throw new IllegalRawConversionException("Illegal or absent field \"color\" in storage");

        if(level == null)
            throw new IllegalRawConversionException("Missing mandatory field \"level\" in storage");

        try {
            return new LevelFlag(color, level);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return JSONSerializer.toJson(this);
    }
}
