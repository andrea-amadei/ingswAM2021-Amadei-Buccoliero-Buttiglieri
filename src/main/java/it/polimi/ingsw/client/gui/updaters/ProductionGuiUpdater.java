package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.ProductionBox;
import it.polimi.ingsw.client.clientmodel.ClientProduction;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

public class ProductionGuiUpdater implements Listener<ClientProduction> {

    private final ProductionBox productionBox;

    public ProductionGuiUpdater(ProductionBox productionBox, ClientProduction clientProduction){
        this.productionBox = productionBox;
        clientProduction.addListener(this);
        update(clientProduction);
    }

    @Override
    public void update(ClientProduction clientProduction) {
        Platform.runLater(() -> productionBox.setProduction(clientProduction));

    }
}
