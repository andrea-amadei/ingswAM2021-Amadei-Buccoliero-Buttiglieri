package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.parser.raw.RawRequirement;

public interface RequirementSelector {
    RawRequirement getRawRequirement();
    void setRawRequirement(RawRequirement rawRequirement);
}
