package it.polimi.ingsw.client.model;

import java.util.ArrayList;
import java.util.List;

public class ConversionOption {

    private final List<String> resources;
    private final int faithPoints;

    public ConversionOption(List<String> resources, int faithPoints) {
        this.resources = resources;
        this.faithPoints = faithPoints;
    }

    public List<String> getResources() {
        return new ArrayList<>(resources);
    }

    public int getFaithPoints() {
        return faithPoints;
    }
}
