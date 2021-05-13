package it.polimi.ingsw.client.cli;

public interface MutableElementGroup extends ElementGroup {
    void addElement(VisibleElement element);
    void removeElement(String name);
}
