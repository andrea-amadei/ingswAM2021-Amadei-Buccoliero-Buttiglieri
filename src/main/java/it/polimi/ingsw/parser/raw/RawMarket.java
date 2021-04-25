package it.polimi.ingsw.parser.raw;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.RawObject;

import java.util.ArrayList;
import java.util.List;

public class RawMarket implements RawObject<Market> {

    @SerializedName("marbles")
    private List<MarbleColor> marbles;

    @SerializedName("odd")
    private MarbleColor odd;

    public RawMarket() { }

    public RawMarket(Market market) {
        int row, col;
        row = market.getRowSize();
        col = market.getColSize();

        marbles = new ArrayList<>(row * col);

        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++)
                marbles.add(market.getMarble(i, j).getColor());

        odd = market.getOddOne().getColor();
    }

    public List<MarbleColor> getMarbles() {
        return marbles;
    }

    public MarbleColor getOdd() {
        return odd;
    }

    @Override
    public Market toObject() throws IllegalRawConversionException {
        if(marbles == null)
            throw new IllegalRawConversionException("Missing mandatory field \"marbles\" in market");

        if(marbles.size() == 0)
            throw new IllegalRawConversionException("Field \"marbles\" is empty or invalid in market");

        if(odd == null)
            throw new IllegalRawConversionException("Absent or illegal field \"odd\" in storage");

        try {
            return new Market(marbles, odd);
        } catch (IllegalArgumentException e) {
            throw new IllegalRawConversionException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return JSONSerializer.toJson(this);
    }
}
