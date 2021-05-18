package it.polimi.ingsw.client.cli.framework;

public interface MutableElementGroup extends ElementGroup {
    void addElement(VisibleElement element);
    void removeElement(String name);
}
