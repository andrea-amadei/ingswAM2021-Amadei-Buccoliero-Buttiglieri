package it.polimi.ingsw.common.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.server.model.faithpath.FaithPathTile;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.UniqueRawObject;

public class RawFaithPathTile implements UniqueRawObject<FaithPathTile> {

    @SerializedName(value = "x", alternate = "coord_x")
    private Integer x;
    @SerializedName(value = "y", alternate = "coord_y")
    private Integer y;

    @SerializedName(value = "order", alternate = "id")
    private int order;

    @SerializedName(value = "points", alternate = {"victoryPoints", "victory_points"})
    private int victoryPoints;

    @SerializedName(value = "pope_group", alternate = "popeGroup")
    private int popeGroup;
    @SerializedName(value = "pope_check", alternate = "popeCheck")
    private boolean popeCheck;

    public RawFaithPathTile() { }

    public RawFaithPathTile(FaithPathTile faithPathTile) {
        if(faithPathTile == null)
            throw new NullPointerException();

        x = faithPathTile.getX();
        y = faithPathTile.getY();
        order = faithPathTile.getOrder();
        victoryPoints = faithPathTile.getVictoryPoints();
        popeGroup = faithPathTile.getPopeGroup();
        popeCheck = faithPathTile.isPopeCheck();
    }

    public RawFaithPathTile(int order, int x, int y, int victoryPoints, int popeGroup, boolean popeCheck) {
        this.x = x;
        this.y = y;
        this.order = order;
        this.victoryPoints = victoryPoints;
        this.popeGroup = popeGroup;
        this.popeCheck = popeCheck;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOrder() {
        return order;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getPopeGroup() {
        return popeGroup;
    }

    public boolean isPopeCheck() {
        return popeCheck;
    }

    @Override
    public String getStringId() {
        return String.format("%03d", order);
    }

    @Override
    public FaithPathTile toObject() throws IllegalRawConversionException {
        if(x == null)
            throw new IllegalRawConversionException("Missing mandatory field \"x\" in faith path tile");

        if(y == null)
            throw new IllegalRawConversionException("Missing mandatory field \"y\" in faith path tile");

        try {
            return new FaithPathTile(x, y, order, victoryPoints, popeGroup, popeCheck);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return JSONSerializer.toJson(this);
    }
}
