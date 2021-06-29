package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.common.parser.raw.RawStorage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;

public interface ResourceContainer {
    /* PROPERTIES */
    StringProperty containerJSONProperty();
    ObjectProperty<RawStorage> rawStorageProperty();
    BooleanProperty hideIfEmptyProperty();
    BooleanProperty showResourceIfZeroProperty();
    BooleanProperty showXProperty();
    BooleanProperty anyAcceptedProperty();
    MapProperty<String, Integer> selectedResourcesProperty();

    /* GETTERS */
    String getContainerJSON();
    RawStorage getRawStorage();
    boolean isHideIfEmpty();
    boolean isShowResourceIfZero();
    boolean isShowX();
    boolean isAnyAccepted();
    ObservableMap<String, Integer> getSelectedResources();

    /* SETTERS */
    void setContainerJSON(String containerJSON);
    void setRawStorage(RawStorage rawStorage);
    void setHideIfEmpty(boolean hideIfEmpty);
    void setShowResourceIfZero(boolean showResourceIfZero);
    void setShowX(boolean showX);
    void setAnyAccepted(boolean anyAccepted);
    void setSelectedResources(ObservableMap<String, Integer> selectedResources);
}
