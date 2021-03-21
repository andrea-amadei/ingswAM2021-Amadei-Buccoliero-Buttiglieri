package it.polimi.ingsw.gamematerials;

public interface ResourceType {

    //TODO:Refactor "isA"
    boolean isA(ResourceSingle other);
    boolean isA(ResourceGroup other);

    @Override
    String toString();
}
