package it.polimi.ingsw.client.cli.framework;

import java.util.List;

public interface ElementGroup extends VisibleElement {
    VisibleElement getElement(String name);
    List<VisibleElement> getAllElements();
}
