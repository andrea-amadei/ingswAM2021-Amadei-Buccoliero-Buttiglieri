package it.polimi.ingsw.configurator;

import it.polimi.ingsw.client.gui.nodes.CraftingBox;
import it.polimi.ingsw.client.gui.nodes.CraftingCardBox;
import it.polimi.ingsw.client.gui.nodes.FaithPath;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.common.parser.raw.*;
import it.polimi.ingsw.configurator.nodes.CraftingSelector;
import it.polimi.ingsw.configurator.nodes.LeaderCardSelector;
import it.polimi.ingsw.configurator.nodes.ResourceSelector;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.faithpath.FaithPathGroup;
import it.polimi.ingsw.server.model.faithpath.FaithPathTile;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.utils.ResourceLoader;
import it.polimi.ingsw.server.model.leader.LeaderCard;
import it.polimi.ingsw.server.model.production.CraftingCard;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class ConfiguratorController {
    // COMMON
    @FXML
    public TabPane root;

    private int getIntFromTextField(TextField textField) {
        int n;
        try { n = Integer.parseInt(textField.getText()); } catch (NumberFormatException e) { n = 0; }

        return n;
    }

    public void initialize() {
        int i;

        groupAddButton = new Button("+");
        groupAddButton.setOnAction(actionEvent -> createFaithGroup());

        groupRemoveButton = new Button("X");
        groupRemoveButton.setOnAction(actionEvent -> removeFaithGroup());

        tileAddButton = new Button("+");
        tileAddButton.setOnAction(actionEvent -> createFaithTile());

        tileRemoveButton = new Button("X");
        tileRemoveButton.setOnAction(actionEvent -> removeFaithTile());

        List<RawFaithPathTile> defaultTiles;
        List<RawFaithPathGroup> defaultGroups;
        RawCrafting defaultBaseCrafting;
        List<RawCraftingCard> defaultCrafting;
        List<RawLeaderCard> defaultLeaders;

        try {
            it.polimi.ingsw.server.model.faithpath.FaithPath fp = JSONParser.parseFaithPath(ResourceLoader.loadFile("cfg/faith.json"));
            defaultTiles = fp.getTiles().stream().map(FaithPathTile::toRaw).collect(Collectors.toList());
            defaultGroups = fp.getFaithGroupList().stream().map(FaithPathGroup::toRaw).collect(Collectors.toList());
        } catch (ParserException e) {
            throw new IllegalArgumentException("Loading of standard faith path failed unexpectedly!");
        }

        try {
            defaultCrafting = JSONParser.parseCraftingCards(ResourceLoader.loadFile("cfg/crafting.json")).stream().map(CraftingCard::toRaw).collect(Collectors.toList());
            defaultBaseCrafting = JSONParser.parseBaseCrafting(ResourceLoader.loadFile("cfg/crafting.json")).get(0).toRaw();
        } catch (ParserException e) {
            throw new IllegalArgumentException("Loading of standard crafting failed unexpectedly!");
        }

        try {
            defaultLeaders = JSONParser.parseLeaders(ResourceLoader.loadFile("cfg/leaders.json")).stream().map(LeaderCard::toRaw).collect(Collectors.toList());
        } catch (ParserException e) {
            throw new IllegalArgumentException("Loading of standard crafting failed unexpectedly!");
        }

        for(i = 0; i < defaultGroups.size(); i++) {
            createFaithGroup();
            groupPoints.get(i).setText(String.valueOf(defaultGroups.get(i).getPoints()));
        }

        for(i = 0; i < defaultTiles.size(); i++) {
            createFaithTile();
            xs.get(i).setText(String.valueOf(defaultTiles.get(i).getX()));
            ys.get(i).setText(String.valueOf(defaultTiles.get(i).getY()));
            tilePoints.get(i).setText(String.valueOf(defaultTiles.get(i).getVictoryPoints()));
            tileGroups.get(i).setText(String.valueOf(defaultTiles.get(i).getPopeGroup()));
            tileChecks.get(i).setSelected(defaultTiles.get(i).isPopeCheck());
        }

        testFaithPath();

        baseCraftingSelector.setInputMap(defaultBaseCrafting.getInput());
        baseCraftingSelector.setOutputMap(defaultBaseCrafting.getOutput());
        baseCraftingSelector.setFaithOutput(defaultBaseCrafting.getFaithOutput());

        for(i = 0; i < defaultCrafting.size(); i++) {
            createShopCard();

            shopCardColor.get(i).getSelectionModel().select(defaultCrafting.get(i).getFlag().name().toLowerCase());
            shopCardLevel.get(i).getSelectionModel().select(defaultCrafting.get(i).getLevel() - 1);
            shopCardPoints.get(i).setText(String.valueOf(defaultCrafting.get(i).getPoints()));

            shopCardCosts.get(i).setResourceMap(defaultCrafting.get(i).getCost());

            shopCardCrafting.get(i).setInputMap(defaultCrafting.get(i).getCrafting().getInput());
            shopCardCrafting.get(i).setOutputMap(defaultCrafting.get(i).getCrafting().getOutput());
            shopCardCrafting.get(i).setFaithOutput(defaultCrafting.get(i).getCrafting().getFaithOutput());
        }

        testShop();

        for(i = 0; i < defaultLeaders.size(); i++) {
            createLeaderCard();
            leaders.get(i).setRawLeaderCard(defaultLeaders.get(i));
        }

        removeLeaderButton.setDisable(true);
        testLeaders();
    }

    // FAITH PATH
    @FXML
    public AnchorPane faithPathPane;

    @FXML
    public VBox faithGroupsVBox;
    private final List<HBox> groups = new ArrayList<>();
    private final List<TextField> groupPoints = new ArrayList<>();
    private Button groupAddButton, groupRemoveButton;

    @FXML
    public VBox faithTilesVBox;
    private final List<HBox> tiles = new ArrayList<>();
    private final List<TextField> xs = new ArrayList<>();
    private final List<TextField> ys = new ArrayList<>();
    private final List<TextField> tilePoints = new ArrayList<>();
    private final List<TextField> tileGroups = new ArrayList<>();
    private final List<CheckBox> tileChecks = new ArrayList<>();
    private final List<Label> tileErrors = new ArrayList<>();
    private Button tileAddButton, tileRemoveButton;

    private FaithPath faithPath;

    private void createFaithGroup() {
        TextField textField;

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPrefHeight(40d);
        hBox.setSpacing(20d);
        VBox.setMargin(hBox, new Insets(0, 0, 0, 20d));

        hBox.getChildren().add(new Label("Group:"));

        textField = new TextField(String.valueOf(groups.size() + 1));
        textField.setDisable(true);
        textField.setEditable(false);
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        hBox.getChildren().add(textField);

        hBox.getChildren().add(new Label("Points granted:"));

        textField = new TextField(String.valueOf(groups.size() + 2));
        textField.setPrefHeight(25d);
        textField.setPrefWidth(80d);
        TextField finalTextField = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        groupPoints.add(textField);

        if(groups.size() != 0) {
            groups.get(groups.size() - 1).getChildren().remove(groupAddButton);
            groups.get(groups.size() - 1).getChildren().remove(groupRemoveButton);
        }

        hBox.getChildren().add(groupAddButton);
        hBox.getChildren().add(groupRemoveButton);

        groupRemoveButton.setDisable(false);

        faithGroupsVBox.getChildren().add(hBox);
        groups.add(hBox);
    }

    private void removeFaithGroup() {
        if(groups.size() == 1)
            return;

        faithGroupsVBox.getChildren().remove(groups.get(groups.size() - 1));

        groups.remove(groups.size() - 1);
        groupPoints.remove(groupPoints.size() - 1);

        groups.get(groups.size() - 1).getChildren().add(groupAddButton);
        groups.get(groups.size() - 1).getChildren().add(groupRemoveButton);

        if(groups.size() == 1)
            groupRemoveButton.setDisable(true);
    }

    private void createFaithTile() {
        Label label;
        TextField textField;
        CheckBox checkBox;

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPrefHeight(40d);
        hBox.setSpacing(20d);
        VBox.setMargin(hBox, new Insets(0, 0, 0, 20d));

        // TILE
        hBox.getChildren().add(new Label("Tile:"));

        textField = new TextField(String.valueOf(tiles.size()));
        textField.setDisable(true);
        textField.setEditable(false);
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        hBox.getChildren().add(textField);

        // X
        hBox.getChildren().add(new Label("X:"));

        textField = new TextField(String.valueOf(tiles.size() + 1));
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        TextField finalTextField1 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField1.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        xs.add(textField);

        // Y
        hBox.getChildren().add(new Label("Y:"));

        textField = new TextField("1");
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        TextField finalTextField2 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        ys.add(textField);

        // POINTS
        hBox.getChildren().add(new Label("Points:"));

        textField = new TextField("0");
        textField.setPrefHeight(25d);
        textField.setPrefWidth(80d);
        TextField finalTextField3 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                finalTextField3.setText(newValue.replaceAll("[^\\d]", ""));
        });
        hBox.getChildren().add(textField);
        tilePoints.add(textField);

        // GROUP
        hBox.getChildren().add(new Label("Group:"));

        textField = new TextField("0");
        textField.setPrefHeight(25d);
        textField.setPrefWidth(80d);
        TextField finalTextField4 = textField;
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                finalTextField4.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hBox.getChildren().add(textField);
        tileGroups.add(textField);

        // CHECK
        hBox.getChildren().add(new Label("Check:"));

        checkBox = new CheckBox();
        hBox.getChildren().add(checkBox);
        tileChecks.add(checkBox);

        // ERROR LABEL
        label = new Label("");
        label.setPrefWidth(400d);
        label.setStyle("-fx-text-fill: red;");
        hBox.getChildren().add(label);
        tileErrors.add(label);

        if(tiles.size() != 0) {
            tiles.get(tiles.size() - 1).getChildren().remove(tileAddButton);
            tiles.get(tiles.size() - 1).getChildren().remove(tileRemoveButton);
        }

        hBox.getChildren().add(tileAddButton);
        hBox.getChildren().add(tileRemoveButton);

        tileRemoveButton.setDisable(false);

        faithTilesVBox.getChildren().add(hBox);
        tiles.add(hBox);
    }

    private void removeFaithTile() {
        if(tiles.size() == 1)
            return;

        faithTilesVBox.getChildren().remove(tiles.get(tiles.size() - 1));

        tiles.remove(tiles.size() - 1);
        xs.remove(xs.size() - 1);
        ys.remove(ys.size() - 1);
        tilePoints.remove(tilePoints.size() - 1);
        tileGroups.remove(tileGroups.size() - 1);
        tileChecks.remove(tileChecks.size() - 1);
        tileErrors.remove(tileErrors.size() - 1);

        tiles.get(tiles.size() - 1).getChildren().add(tileAddButton);
        tiles.get(tiles.size() - 1).getChildren().add(tileRemoveButton);

        if(tiles.size() == 1)
            tileRemoveButton.setDisable(true);
    }

    private List<RawFaithPathGroup> createFaithGroupList() {
        List<RawFaithPathGroup> list = new ArrayList<>();

        for(int i = 0; i < groupPoints.size(); i++) {
            list.add(new RawFaithPathGroup(i + 1, getIntFromTextField(groupPoints.get(i))));
        }

        return list;
    }

    private List<RawFaithPathTile> createFaithTileList() {
        List<RawFaithPathTile> list = new ArrayList<>();
        Map<Integer, Set<Integer>> coordinates = new HashMap<>();
        List<Boolean> checksPerGroup;
        boolean error = false;
        int i, j;

        checksPerGroup = new ArrayList<>();

        for(i = 0; i < groups.size(); i++) {
            checksPerGroup.add(false);
        }

        for(i = 0; i < tiles.size(); i++) {
            tileErrors.get(i).setText("");

            // check for duplicate tiles
            if (coordinates.containsKey(getIntFromTextField(xs.get(i)))) {
                if (!coordinates.get(getIntFromTextField(xs.get(i))).add(getIntFromTextField(ys.get(i)))) {
                    tileErrors.get(i).setText("Different tiles cannot share the same coordinates");
                    error = true;
                }
            } else {
                int finalI = i;
                coordinates.put(getIntFromTextField(xs.get(i)), new HashSet<>() {{
                    add(getIntFromTextField(ys.get(finalI)));
                }});
            }

            // check for groups
            if(getIntFromTextField(tileGroups.get(i)) > groups.size()) {
                tileErrors.get(i).setText("The selected group does not exists");
                error = true;
            }

            if(getIntFromTextField(tileGroups.get(i)) == 0 && tileChecks.get(i).isSelected()) {
                tileErrors.get(i).setText("Tiles without group cannot be a check");
                error = true;
            }

            if(getIntFromTextField(tileGroups.get(i)) != 0 && tileChecks.get(i).isSelected()) {
                if(checksPerGroup.get(getIntFromTextField(tileGroups.get(i)) - 1)) {
                    tileErrors.get(i).setText("More tiles from the same group cannot be both checks");
                    error = true;
                } else
                    checksPerGroup.set(getIntFromTextField(tileGroups.get(i)) - 1, true);
            }
        }

        for(i = 0; i < checksPerGroup.size(); i++) {
            if(!checksPerGroup.get(i))
                for(j = 0; j < tileGroups.size(); j++)
                    if(getIntFromTextField(tileGroups.get(j)) == i + 1) {
                        tileErrors.get(j).setText("One tile per group must be a check");
                        error = true;
                    }
        }

        if(error)
            throw new IllegalArgumentException();

        for(i = 0; i < tiles.size(); i++) {
            list.add(new RawFaithPathTile(
                    i,
                    getIntFromTextField(xs.get(i)),
                    getIntFromTextField(ys.get(i)),
                    getIntFromTextField(tilePoints.get(i)),
                    getIntFromTextField(tileGroups.get(i)),
                    tileChecks.get(i).isSelected())
            );
        }

        return list;
    }

    @FXML
    public boolean testFaithPath() {
        faithPathPane.getChildren().remove(faithPath);

        List<RawFaithPathGroup> groups;
        List<RawFaithPathTile> tiles;

        try {
            groups = createFaithGroupList();
        } catch (RuntimeException e) {
            return false;
        }

        try {
            tiles = createFaithTileList();
        } catch (RuntimeException e) {
            return false;
        }

        faithPath = new FaithPath(tiles, groups);
        faithPath.getTransforms().add(new Scale(0.5d, 0.5d));
        AnchorPane.setLeftAnchor(faithPath, 460d);
        AnchorPane.setTopAnchor(faithPath, 50d);

        faithPathPane.getChildren().add(faithPath);

        return true;
    }

    @FXML
    public void generateFaithPath() {
        if(!testFaithPath())
            return;

        String str = "{\"tiles\":" + JSONSerializer.toJson(createFaithTileList()) + ",\"groups\":" + JSONSerializer.toJson(createFaithGroupList()) + "}";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save faith.json");
        fileChooser.setInitialFileName("faith.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if(file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                writer.println(str);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Unable to save file");
            }
        }

        str = "{\"max_username_length\":30,\"min_username_length\":2,\"max_card_level\":3,\"min_card_level\":1,\"amount_of_leaders_per_player\":4,\"amount_of_leaders_to_discard\":2,\"first_player_amount_of_faith_points_on_start\":0,\"second_player_amount_of_faith_points_on_start\":0,\"third_player_amount_of_faith_points_on_start\":1,\"fourth_player_amount_of_faith_points_on_start\":1,\"first_player_amount_of_resources_on_start\":0,\"second_player_amount_of_resources_on_start\":1,\"third_player_amount_of_resources_on_start\":1,\"fourth_player_amount_of_resources_on_start\":2,\"base_cupboard_shelf_names\":[\"TopShelf\",\"MiddleShelf\",\"BottomShelf\"],\"base_cupboard_shelf_types\":[\"any\",\"any\",\"any\"],\"base_cupboard_shelf_sizes\":[1,2,3],\"hand_id\":\"Hand\",\"basket_id\":\"MarketBasket\",\"chest_id\":\"Chest\",\"market_rows\":3,\"market_columns\":4,\"marble_per_color\":{\"BLUE\":2,\"WHITE\":4,\"GREY\":2,\"RED\":1,\"PURPLE\":2,\"YELLOW\":2},\"upgradable_crafting_number\":3,\"faith_checkpoint_number\":" +
                groups.size() + "}";

        fileChooser = new FileChooser();
        fileChooser.setTitle("Save config.json");
        fileChooser.setInitialFileName("config.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if(file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                writer.println(str);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Unable to save file");
            }
        }
    }

    // SHOP
    @FXML
    public AnchorPane shopPane;

    @FXML
    public CraftingSelector baseCraftingSelector;
    @FXML
    public CraftingBox baseCraftingBox;

    @FXML
    public VBox shopVBox;
    public List<HBox> shopCards = new ArrayList<>();

    public List<ComboBox<String>> shopCardColor = new ArrayList<>();
    public List<ComboBox<Integer>> shopCardLevel = new ArrayList<>();
    public List<TextField> shopCardPoints = new ArrayList<>();
    public List<ResourceSelector> shopCardCosts = new ArrayList<>();
    public List<CraftingSelector> shopCardCrafting = new ArrayList<>();

    public List<CraftingCardBox> craftingCardBoxes = new ArrayList<>();

    public void createShopCard() {
        HBox hBox;
        HBox hBox1, hBox2, hBox3, hBox4;
        Label label;

        hBox = new HBox();
        hBox.setSpacing(80d);
        VBox.setMargin(hBox, new Insets(0, 0, 0, 20d));

        VBox vBox;
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20d);

        ComboBox<String> comboBox1;
        ComboBox<Integer> comboBox2;
        TextField textField1;
        TextField textField2;

        textField1 = new TextField(String.valueOf(shopCards.size() + 1));
        textField1.setEditable(false);
        textField1.setDisable(true);
        textField1.setPrefWidth(80d);

        comboBox1 = new ComboBox<>();
        comboBox1.setPrefWidth(80d);
        comboBox1.setItems(FXCollections.observableArrayList("blue", "green", "purple", "yellow"));
        comboBox1.getSelectionModel().selectFirst();

        comboBox2 = new ComboBox<>();
        comboBox2.setPrefWidth(80d);
        comboBox2.setItems(FXCollections.observableArrayList(1, 2, 3));
        comboBox2.getSelectionModel().selectFirst();

        textField2 = new TextField("1");
        textField2.setPrefWidth(80d);
        textField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                textField2.setText(newValue.replaceAll("[^\\d]", ""));
        });

        shopCardColor.add(comboBox1);
        shopCardLevel.add(comboBox2);
        shopCardPoints.add(textField2);

        hBox1 = new HBox();
        hBox1.setSpacing(20d);
        label = new Label("Id:");
        label.setPrefWidth(120d);
        hBox1.getChildren().addAll(label, textField1);

        hBox2 = new HBox();
        hBox2.setSpacing(20d);
        label = new Label("Flag color:");
        label.setPrefWidth(120d);
        hBox2.getChildren().addAll(label, comboBox1);

        hBox3 = new HBox();
        hBox3.setSpacing(20d);
        label = new Label("Flag level:");
        label.setPrefWidth(120d);
        hBox3.getChildren().addAll(label, comboBox2);

        hBox4 = new HBox();
        hBox4.setSpacing(20d);
        label = new Label("Points:");
        label.setPrefWidth(120d);
        hBox4.getChildren().addAll(label, textField2);

        vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4);

        ResourceSelector resourceSelector = new ResourceSelector("Cost:", false);
        resourceSelector.setAlignment(Pos.CENTER);

        CraftingSelector craftingSelector = new CraftingSelector();
        craftingSelector.setAlignment(Pos.CENTER);

        shopCardCosts.add(resourceSelector);
        shopCardCrafting.add(craftingSelector);

        hBox.getChildren().addAll(vBox, resourceSelector, craftingSelector);

        shopVBox.getChildren().add(hBox);
        shopCards.add(hBox);
    }

    private List<RawCraftingCard> createCraftingCardList() {
        List<RawCraftingCard> list = new ArrayList<>();

        for(int i = 0; i < shopCards.size(); i++) {
            list.add(new RawCraftingCard(
                    i + 1,
                    FlagColor.valueOf(shopCardColor.get(i).getSelectionModel().getSelectedItem().toUpperCase()),
                    shopCardLevel.get(i).getSelectionModel().getSelectedItem(),
                    shopCardCosts.get(i).getResourceMap(),
                    shopCardCrafting.get(i).getRawCrafting(),
                    Integer.parseInt(shopCardPoints.get(i).getText())
            ));
        }

        return list;
    }

    @FXML
    public void testShop() {
        CraftingCardBox craftingCardBox;

        baseCraftingBox.setRawCrafting(baseCraftingSelector.getRawCrafting());

        for(int i = 0; i < shopCards.size(); i++) {
            craftingCardBox = new CraftingCardBox(new RawCraftingCard(
                    i + 1,
                    FlagColor.valueOf(shopCardColor.get(i).getSelectionModel().getSelectedItem().toUpperCase()),
                    shopCardLevel.get(i).getSelectionModel().getSelectedItem(),
                    shopCardCosts.get(i).getResourceMap(),
                    shopCardCrafting.get(i).getRawCrafting(),
                    Integer.parseInt(shopCardPoints.get(i).getText())
            ));

            craftingCardBox.getTransforms().add(new Scale(0.7d, 0.7d));
            craftingCardBox.setTranslateY(70d);

            if(shopCards.get(i).getChildren().size() == 4)
                shopCards.get(i).getChildren().remove(shopCards.get(i).getChildren().size() - 1);
            shopCards.get(i).getChildren().add(craftingCardBox);
        }
    }

    @FXML
    public void generateShop() {
        testShop();

        String str = "{\"shop\":" + JSONSerializer.toJson(createCraftingCardList()) + ", \"base\":[" + baseCraftingSelector.getRawCrafting() + "]}";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save crafting.json");
        fileChooser.setInitialFileName("crafting.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if(file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                writer.println(str);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Unable to save file");
            }
        }
    }

    // LEADERS
    @FXML
    public AnchorPane leaderPane;
    @FXML
    public VBox leaderVBox;
    @FXML
    public LeaderCardSelector test;
    @FXML
    public Button addLeaderButton;
    @FXML
    public Button removeLeaderButton;

    private List<LeaderCardSelector> leaders = new ArrayList<>();

    public void createLeaderCard() {
        LeaderCardSelector leaderCardSelector = new LeaderCardSelector();
        leaderCardSelector.setId(leaders.size() + 1);
        leaders.add(leaderCardSelector);
        leaderVBox.getChildren().add(leaderCardSelector);

        removeLeaderButton.setDisable(false);
    }

    public void removeLeaderCard() {
        if(leaders.size() <= 16)
            return;

        leaderVBox.getChildren().remove(leaderVBox.getChildren().size() - 1);
        leaders.remove(leaders.size() - 1);

        if(leaders.size() == 16)
            removeLeaderButton.setDisable(true);
    }

    public List<RawLeaderCard> createLeaderCardsList() {
        List<RawLeaderCard> list = new ArrayList<>();

        for(LeaderCardSelector leader : leaders) {
            list.add(leader.getRawLeaderCard());
        }

        return list;
    }

    public void testLeaders() {
        for(LeaderCardSelector leader : leaders) {
            leader.updatePreview();
        }
    }

    public void generateLeaders() {
        testLeaders();

        String str = "{\"cards\":" + JSONSerializer.toJson(createLeaderCardsList()) + "}";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save leaders.json");
        fileChooser.setInitialFileName("leaders.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if(file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                writer.println(str);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Unable to save file");
            }
        }
    }
}
