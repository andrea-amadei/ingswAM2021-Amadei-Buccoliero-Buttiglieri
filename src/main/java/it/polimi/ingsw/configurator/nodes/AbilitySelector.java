package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;

public interface AbilitySelector {
    RawSpecialAbility getRawSpecialAbility();
    void setRawSpecialAbility(RawSpecialAbility rawSpecialAbility);
}
