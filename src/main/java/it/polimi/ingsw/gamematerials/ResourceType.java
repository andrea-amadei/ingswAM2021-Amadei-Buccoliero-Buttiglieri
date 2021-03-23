package it.polimi.ingsw.gamematerials;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ResourceType {

    protected final Set<ResourceSingle> associatedResources;
    protected final String id;

    public ResourceType(String id){
        this.id = id;
        associatedResources = new HashSet<>();
    }

    //TODO:Refactor "isA"
    public abstract boolean isA(ResourceType other);

    public String getId(){
        return id;
    }

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
}
