package it.polimi.ingsw.client.cli;

import java.util.List;

public interface ElementGroup extends VisibleElement {
    VisibleElement getElement(String name);
    List<VisibleElement> getAllElements();
}
