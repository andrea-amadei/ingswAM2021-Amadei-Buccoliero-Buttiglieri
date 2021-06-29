package it.polimi.ingsw.gamematerials;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ResourceType is an abstract class
 * It represents a type of resource
 */
public abstract class ResourceType {

    protected final Set<ResourceSingle> associatedResources;
    protected final String id;

    /**
     * ResourceType constructor
     * @param id the ID of the type
     */
    public ResourceType(String id){
        this.id = id;
        associatedResources = new HashSet<>();
    }

    public abstract boolean isA(ResourceType other);

    public abstract boolean isGroup();

    /**
     * @return ID
     */
    public String getId(){
        return id;
    }

    /**
     * @return the type representation formatted as a String
     * example: Any {Stone, Gold, Shield, Servant}
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(" {");
        List<ResourceSingle> orderedResourceGroup = associatedResources.stream()
                .sorted(Comparator.comparing(ResourceSingle::getId))
                .collect(Collectors.toList());

        for(int i = 0; i < orderedResourceGroup.size(); i++){
            sb.append(orderedResourceGroup.get(i));
            if(i < orderedResourceGroup.size() - 1)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * equals method assesses if two objects have the same ID
     * @param o the object to compare
     * @return the outcome of the comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceSingle that = (ResourceSingle) o;
        return id.equals(that.id);
    }

    /**
     * @return the hash code based off the ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
