package it.polimi.ingsw.client.cli.framework;

import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.common.exceptions.UnableToDrawElementException;

import java.util.*;

public class CliFramework {
    private final OutputHandler outputHandler;
    private final LinkedHashMap<String, Frame> frames;
    private String activeFrameName;

    public CliFramework(boolean printColors) {
        outputHandler = new OutputHandler(printColors);
        frames = new LinkedHashMap<>();
        activeFrameName = null;
    }

    public CliFramework(boolean printColors, List<Frame> frames) {
        if(frames == null)
            throw new NullPointerException();

        outputHandler = new OutputHandler(printColors);

        this.frames = new LinkedHashMap<>();
        for(Frame i : frames)
            addFrame(i);

        activeFrameName = null;
    }

    public Frame getFrame(String name) {
        if(name == null)
            throw new NullPointerException();

        if(!frames.containsKey(name))
            throw new NoSuchElementException("No frame named " + name + " in this group");

        return frames.get(name);
    }

    public List<Frame> getAllFrames() {
        return new ArrayList<>(frames.values());
    }

    public void addFrame(Frame frame) {
        if(frame == null)
            throw new NullPointerException();

        if(frames.containsKey(frame.getName()))
            throw new IllegalArgumentException("A frame with the same name already exists");

        frames.put(frame.getName(), frame);
    }

    public void removeFrame(String name) {
        if(name == null)
            throw new NullPointerException();

        if(!frames.containsKey(name))
            throw new NoSuchElementException("No frame named \" + name + \" in this group");

        frames.remove(name);

        if(name.equals(activeFrameName))
            activeFrameName = null;
    }

    public String getActiveFrameName() {
        return activeFrameName;
    }

    public void setActiveFrame(String activeFrameName) {
        if(activeFrameName == null)
            throw new NullPointerException();

        if(!frames.containsKey(activeFrameName))
            throw new NoSuchElementException("The selected frame doesn't exists");

        this.activeFrameName = activeFrameName;
    }

    public Frame getActiveFrame() {
        if(activeFrameName == null)
            throw new NoSuchElementException("No frame is currently active");

        return frames.get(activeFrameName);
    }

    public void renderActiveFrame() throws UnableToDrawElementException {
        getActiveFrame().draw(outputHandler);
    }
}
