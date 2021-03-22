package it.polimi.ingsw.gamematerials;

import java.util.Objects;

public class ResourceSingle extends ResourceType {


     ResourceSingle(String id) {
         super(id);
         this.associatedResources.add(this);
    }

    @Override
    public boolean isA(ResourceType other){
         if(other == null)
             throw new NullPointerException("Can't compare with null");
         return other.toString().equals(id) || other.associatedResources.contains(this);
    }

    @Override
    public String toString() {
        return this.id;
    }
}
