package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.parser.UniqueRawObject;

public class RawFaithPathTile implements UniqueRawObject<FaithPathTile> {

    @SerializedName(value = "x", alternate = "coord_x")
    private int x;
    @SerializedName(value = "y", alternate = "coord_y")
    private int y;

    @SerializedName(value = "order", alternate = "id")
    private int order;

    @SerializedName(value = "victory_points", alternate = "victoryPoints")
    private int victoryPoints;

    @SerializedName(value = "pope_group", alternate = "popeGroup")
    private int popeGroup;
    @SerializedName(value = "pope_check", alternate = "popeCheck")
    private boolean popeCheck;

    @Override
    public FaithPathTile convert() throws IllegalRawConversionException {
        if(x <= 0)
            throw new IllegalRawConversionException("Missing or illegal mandatory field \"x\" in faith path tile");

        if(y <= 0)
            throw new IllegalRawConversionException("Missing or illegal mandatory field \"y\" in faith path tile");

        try {
            return new FaithPathTile(x, y, order, victoryPoints, popeGroup, popeCheck);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }
    }

    @Override
    public String getId() {
        return String.format("%03d", order);
    }
}
