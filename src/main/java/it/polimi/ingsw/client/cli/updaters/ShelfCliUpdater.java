package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.clientmodel.ClientShelf;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShelfCliUpdater implements Listener<ClientShelf> {
    private int startingRow;
    private int startingColumn;

    private final Frame frame;
    private ClientShelf shelf;

    private List<ResourceBox> elements;
    private List<TextBox> dividers;
    private FixedTextBox name;
    private TextBox selected;
    private Group group;

    public ShelfCliUpdater(ClientShelf shelf, Frame frame, int startingRow, int startingColumn) {
        if(frame == null || shelf == null)
            throw new NullPointerException();

        this.startingRow = startingRow;
        this.startingColumn = startingColumn;
        this.frame = frame;
        this.shelf = shelf;

        shelf.addListener(this);

        setup(shelf);
        update(shelf);
    }

    private ResourceBox getElem(int n, int row, int column, ClientShelf shelf) {
        String resource;
        boolean faded;
        List<String> resources = List.copyOf(shelf.getStorage().getResources().keySet());
        String trueResource;

        if(resources.size() != 0)
            trueResource = resources.get(0);
        else
            trueResource = "null";

        if(!shelf.getStorage().getResources().containsKey(trueResource) || shelf.getStorage().getResources().get(trueResource) <= n - 1) {
            resource = shelf.getAcceptedType();
            faded = true;
        }
        else {
            resource = trueResource;
            faded = false;
        }

        ResourceBox elem = new ResourceBox(
                "elem_" + n,
                row,
                column,
                resource
        );

        elem.setFaded(faded);

        return elem;
    }

    private TextBox getDivider(int n, int row, int column) {
        return new TextBox(
                "divider_" + n,
                row,
                column,
                "──",
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.BLACK
        );
    }

    public void setup(ClientShelf shelf) {
        elements = new ArrayList<>();
        dividers = new ArrayList<>();

        switch (shelf.getSize()) {
            case 1:
                elements.add(getElem(1, startingRow, startingColumn + 15, shelf));

                break;

            case 2:
                elements.add(getElem(1, startingRow, startingColumn + 13, shelf));
                elements.add(getElem(2, startingRow, startingColumn + 17, shelf));

                dividers.add(getDivider(1, startingRow, startingColumn + 15));

                break;

            case 3:
                elements.add(getElem(1, startingRow, startingColumn + 11, shelf));
                elements.add(getElem(2, startingRow, startingColumn + 15, shelf));
                elements.add(getElem(3, startingRow, startingColumn + 19, shelf));

                dividers.add(getDivider(1, startingRow, startingColumn + 13));
                dividers.add(getDivider(2, startingRow, startingColumn + 17));

                break;
        }

        name = new FixedTextBox(
                "name",
                startingRow,
                startingColumn + 2,
                8,
                shelf.getStorage().getId().replace("Shelf", ""),
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.BLACK
        );

        selected = new TextBox(
                "selected",
                startingRow,
                startingColumn + 22,
                " ",
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.GREEN
        );

        List<VisibleElement> list = new ArrayList<>();
        list.add(name);
        list.add(selected);
        list.addAll(elements.stream().map(x -> (VisibleElement) x).collect(Collectors.toList()));
        list.addAll(dividers.stream().map(x -> (VisibleElement) x).collect(Collectors.toList()));

        group = new Group("shelf_" + shelf.getStorage().getId(), list);
        frame.addElement(group);
    }

    @Override
    public void update(ClientShelf shelf) {
        for(VisibleElement elem : group.getAllElements())
            if(elem.getName().startsWith("elem_"))
                group.removeElement(elem.getName());

        switch (shelf.getSize()) {
            case 1:
                elements.set(0, getElem(1, startingRow, startingColumn + 15, shelf));

                break;

            case 2:
                elements.set(0, getElem(1, startingRow, startingColumn + 13, shelf));
                elements.set(1, getElem(2, startingRow, startingColumn + 17, shelf));

                break;

            case 3:
                elements.set(0, getElem(1, startingRow, startingColumn + 11, shelf));
                elements.set(1, getElem(2, startingRow, startingColumn + 15, shelf));
                elements.set(2, getElem(3, startingRow, startingColumn + 19, shelf));

                break;
        }

        for(VisibleElement elem : elements)
            group.addElement(elem);

        if(shelf.getSelectedResources().size() == 0) {
            selected.setBackgroundColor(BackgroundColor.BLACK);
            selected.setText(" ");
        }
        else {
            selected.setBackgroundColor(BackgroundColor.GREEN);
            selected.setText("Sel: " + shelf.getSelectedResources().get(List.copyOf(shelf.getSelectedResources().keySet()).get(0)));
        }
    }
}
