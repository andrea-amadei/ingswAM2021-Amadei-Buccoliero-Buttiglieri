package it.polimi.ingsw.gamematerials;

import java.util.Objects;

public class ResourceSingle implements ResourceType {

    private final String id;

     ResourceSingle(String id) {
        this.id = id;
    }

    @Override
    public boolean isA(ResourceSingle other) {
        return other.toString().equals(id);
    }

    @Override
    public boolean isA(ResourceGroup other) {
        return other.getGroup().contains(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceSingle that = (ResourceSingle) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }

}
