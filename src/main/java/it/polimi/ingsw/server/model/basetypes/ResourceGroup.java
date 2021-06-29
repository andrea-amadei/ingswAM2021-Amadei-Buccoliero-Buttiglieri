package it.polimi.ingsw.server.model.basetypes;


import java.util.Set;

public class ResourceGroup extends ResourceType{


    public ResourceGroup(String id, Set<ResourceSingle> associatedTypes) {
        super(id);
        associatedResources.addAll(associatedTypes);
    }

    @Override
    public boolean isA(ResourceType other) {
        if(other == null)
            throw new NullPointerException("Can't compare with null");
        return other.id.equals(this.id);
    }

    @Override
    public boolean isGroup() {
        return true;
    }
}
