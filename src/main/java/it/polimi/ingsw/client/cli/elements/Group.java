package it.polimi.ingsw.client.cli.elements;

import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Group implements MutableElementGroup {
    private final String name;
    private boolean visible;
    private int zIndex;
    private final LinkedHashMap<String, VisibleElement> elements;

    public Group(String name) {
        if(name == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        setVisible(true);
        setZIndex(0);

        elements = new LinkedHashMap<>();
    }

    public Group(String name, List<VisibleElement> elements) {
        if(name == null || elements == null)
            throw new NullPointerException();

        if(name.length() == 0)
            throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;

        this.elements = new LinkedHashMap<>();
        for(VisibleElement i : elements)
            addElement(i);

        setVisible(true);
        setZIndex(0);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public VisibleElement getElement(String name) {
        if(name == null)
            throw new NullPointerException();

        if(!elements.containsKey(name))
            throw new NoSuchElementException("No element named " + name + " in this group");

        return elements.get(name);
    }

    @Override
    public List<VisibleElement> getAllElements() {
        return elements.values()
                .stream()
                .sorted(Comparator.comparingInt(VisibleElement::getZIndex))
                .collect(Collectors.toList());
    }

    @Override
    public void addElement(VisibleElement element) {
        if(element == null)
            throw new NullPointerException();

        if(elements.containsKey(element.getName()))
            throw new IllegalArgumentException("An element with the same name already exists");

        elements.put(element.getName(), element);
    }

    @Override
    public void removeElement(String name) {
        if(name == null)
            throw new NullPointerException();

        if(!elements.containsKey(name))
            throw new NoSuchElementException("No element named \" + name + \" in this group");

        elements.remove(name);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public void setZIndex(int ZIndex) {
        this.zIndex = ZIndex;
    }

    @Override
    public void draw(OutputHandler outputHandler) throws UnableToDrawElementException {
        if(visible)
            CliRenderer.renderWithoutUpdating(outputHandler, getAllElements());
    }
}
