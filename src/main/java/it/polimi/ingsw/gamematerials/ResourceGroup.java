package it.polimi.ingsw.gamematerials;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ResourceGroup extends ResourceType{


    public ResourceGroup(String id, Set<ResourceSingle> associatedTypes) {
        super(id);
        associatedResources.addAll(associatedTypes);
    }

    @Override
    public boolean isA(ResourceType other) {
        return other.id.equals(this.id);
    }
}
