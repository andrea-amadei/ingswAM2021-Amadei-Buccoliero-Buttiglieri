package it.polimi.ingsw.gamematerials;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourceGroup implements ResourceType{

    private final String id;
    private final List<ResourceSingle> group;

    ResourceGroup(String id, List<ResourceSingle> group) {
        this.id = id;
        this.group = group;
    }

    public List<ResourceSingle> getGroup() {
        return new ArrayList<>(group);
    }

    @Override
    public boolean isA(ResourceSingle other) {
        return false;
    }

    @Override
    public boolean isA(ResourceGroup other) {
        return other.toString().equals(id);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceGroup that = (ResourceGroup) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
