package it.polimi.ingsw.deprecated;

import it.polimi.ingsw.client.ClientGameBuilder;
import it.polimi.ingsw.client.cli.CliBuilder;
import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.cli.updaters.*;
import it.polimi.ingsw.client.clientmodel.*;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.common.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.raw.*;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.model.basetypes.*;
import it.polimi.ingsw.server.model.faithpath.FaithPathGroup;
import it.polimi.ingsw.server.model.faithpath.FaithPathTile;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.model.leader.*;
import it.polimi.ingsw.server.model.market.ConversionActuator;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleFactory;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.production.Crafting;
import it.polimi.ingsw.server.model.production.CraftingCard;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import it.polimi.ingsw.server.model.storage.BaseStorage;
import it.polimi.ingsw.server.model.storage.Shelf;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class OldTests {
    public static void gameTest() throws ParserException, UnableToDrawElementException {
        ClientModel model = new ClientModel();
        CliFramework framework = new CliFramework(true);

        ClientGameBuilder.buildGame(model, Arrays.asList("Pippo", "Pluto", "Paperino"),
                "{\"max_username_length\":30,\"min_username_length\":2,\"max_card_level\":3,\"min_card_level\":1,\"amount_of_leaders_to_discard\":2,\"first_player_amount_of_faith_points_on_start\":0,\"second_player_amount_of_faith_points_on_start\":0,\"third_player_amount_of_faith_points_on_start\":1,\"fourth_player_amount_of_faith_points_on_start\":1,\"first_player_amount_of_resources_on_start\":0,\"second_player_amount_of_resources_on_start\":1,\"third_player_amount_of_resources_on_start\":1,\"fourth_player_amount_of_resources_on_start\":2,\"base_cupboard_shelf_names\":[\"TopShelf\",\"MiddleShelf\",\"BottomShelf\"],\"base_cupboard_shelf_types\":[\"any\",\"any\",\"any\"],\"base_cupboard_shelf_sizes\":[1,2,3],\"hand_id\":\"Hand\",\"basket_id\":\"MarketBasket\",\"chest_id\":\"Chest\",\"market_rows\":3,\"market_columns\":4,\"marble_per_color\":{\"BLUE\":2,\"WHITE\":4,\"GREY\":2,\"RED\":1,\"PURPLE\":2,\"YELLOW\":2},\"upgradable_crafting_number\":3,\"faith_checkpoint_number\":3}",
                "{\"shop\":[{\"id\":1,\"color\":\"GREEN\",\"level\":1,\"points\":1,\"cost\":{\"shield\":2},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":1}},{\"id\":2,\"color\":\"PURPLE\",\"level\":1,\"points\":1,\"cost\":{\"servant\":2},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":1}},{\"id\":3,\"color\":\"BLUE\",\"level\":1,\"points\":1,\"cost\":{\"gold\":2},\"crafting\":{\"input\":{\"shield\":1},\"output\":{},\"faith_output\":1}},{\"id\":4,\"color\":\"YELLOW\",\"level\":1,\"points\":1,\"cost\":{\"stone\":2},\"crafting\":{\"input\":{\"servant\":1},\"output\":{},\"faith_output\":1}},{\"id\":5,\"color\":\"GREEN\",\"level\":1,\"points\":2,\"cost\":{\"shield\":1,\"servant\":1,\"stone\":1},\"crafting\":{\"input\":{\"stone\":1},\"output\":{\"servant\":1},\"faith_output\":0}},{\"id\":6,\"color\":\"PURPLE\",\"level\":1,\"points\":2,\"cost\":{\"shield\":1,\"servant\":1,\"gold\":1},\"crafting\":{\"input\":{\"gold\":1},\"output\":{\"shield\":1},\"faith_output\":0}},{\"id\":7,\"color\":\"BLUE\",\"level\":1,\"points\":2,\"cost\":{\"gold\":1,\"servant\":1,\"stone\":1},\"crafting\":{\"input\":{\"servant\":1},\"output\":{\"stone\":1},\"faith_output\":0}},{\"id\":8,\"color\":\"YELLOW\",\"level\":1,\"points\":2,\"cost\":{\"shield\":1,\"stone\":1,\"gold\":1},\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"gold\":1},\"faith_output\":0}},{\"id\":9,\"color\":\"GREEN\",\"level\":1,\"points\":3,\"cost\":{\"shield\":3},\"crafting\":{\"input\":{\"servant\":2},\"output\":{\"gold\":1,\"shield\":1,\"stone\":1},\"faith_output\":0}},{\"id\":10,\"color\":\"PURPLE\",\"level\":1,\"points\":3,\"cost\":{\"servant\":3},\"crafting\":{\"input\":{\"gold\":2},\"output\":{\"servant\":1,\"shield\":1,\"stone\":1},\"faith_output\":0}},{\"id\":11,\"color\":\"BLUE\",\"level\":1,\"points\":3,\"cost\":{\"gold\":3},\"crafting\":{\"input\":{\"stone\":2},\"output\":{\"gold\":1,\"servant\":1,\"shield\":1},\"faith_output\":0}},{\"id\":12,\"color\":\"YELLOW\",\"level\":1,\"points\":3,\"cost\":{\"stone\":3},\"crafting\":{\"input\":{\"shield\":2},\"output\":{\"gold\":1,\"servant\":1,\"stone\":1},\"faith_output\":0}},{\"id\":13,\"color\":\"GREEN\",\"level\":1,\"points\":4,\"cost\":{\"shield\":2,\"gold\":2},\"crafting\":{\"input\":{\"stone\":1,\"servant\":1},\"output\":{\"gold\":2},\"faith_output\":1}},{\"id\":14,\"color\":\"PURPLE\",\"level\":1,\"points\":4,\"cost\":{\"servant\":2,\"stone\":2},\"crafting\":{\"input\":{\"gold\":1,\"shield\":1},\"output\":{\"stone\":2},\"faith_output\":1}},{\"id\":15,\"color\":\"PURPLE\",\"level\":1,\"points\":4,\"cost\":{\"gold\":2,\"servant\":2},\"crafting\":{\"input\":{\"shield\":1,\"stone\":1},\"output\":{\"servant\":2},\"faith_output\":1}},{\"id\":16,\"color\":\"YELLOW\",\"level\":1,\"points\":4,\"cost\":{\"stone\":2,\"shield\":2},\"crafting\":{\"input\":{\"gold\":1,\"servant\":1},\"output\":{\"shield\":2},\"faith_output\":1}},{\"id\":17,\"color\":\"GREEN\",\"level\":2,\"points\":5,\"cost\":{\"shield\":4},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":2}},{\"id\":18,\"color\":\"PURPLE\",\"level\":2,\"points\":5,\"cost\":{\"servant\":4},\"crafting\":{\"input\":{\"gold\":1},\"output\":{},\"faith_output\":2}},{\"id\":19,\"color\":\"BLUE\",\"level\":2,\"points\":5,\"cost\":{\"gold\":4},\"crafting\":{\"input\":{\"servant\":1},\"output\":{},\"faith_output\":2}},{\"id\":20,\"color\":\"YELLOW\",\"level\":2,\"points\":5,\"cost\":{\"stone\":4},\"crafting\":{\"input\":{\"shield\":1},\"output\":{},\"faith_output\":2}},{\"id\":21,\"color\":\"GREEN\",\"level\":2,\"points\":6,\"cost\":{\"shield\":3,\"servant\":2},\"crafting\":{\"input\":{\"shield\":1,\"servant\":1},\"output\":{\"stone\":3},\"faith_output\":0}},{\"id\":22,\"color\":\"PURPLE\",\"level\":2,\"points\":6,\"cost\":{\"servant\":3,\"gold\":2},\"crafting\":{\"input\":{\"gold\":1,\"servant\":1},\"output\":{\"shield\":3},\"faith_output\":0}},{\"id\":23,\"color\":\"BLUE\",\"level\":2,\"points\":6,\"cost\":{\"gold\":3,\"stone\":2},\"crafting\":{\"input\":{\"gold\":1,\"stone\":1},\"output\":{\"servant\":3},\"faith_output\":0}},{\"id\":24,\"color\":\"YELLOW\",\"level\":2,\"points\":6,\"cost\":{\"stone\":3,\"shield\":2},\"crafting\":{\"input\":{\"stone\":1,\"shield\":1},\"output\":{\"gold\":3},\"faith_output\":0}},{\"id\":25,\"color\":\"GREEN\",\"level\":2,\"points\":7,\"cost\":{\"shield\":5},\"crafting\":{\"input\":{\"gold\":2},\"output\":{\"stone\":2},\"faith_output\":2}},{\"id\":26,\"color\":\"PURPLE\",\"level\":2,\"points\":7,\"cost\":{\"servant\":5},\"crafting\":{\"input\":{\"stone\":2},\"output\":{\"gold\":2},\"faith_output\":2}},{\"id\":27,\"color\":\"BLUE\",\"level\":2,\"points\":7,\"cost\":{\"gold\":5},\"crafting\":{\"input\":{\"servant\":2},\"output\":{\"shield\":2},\"faith_output\":2}},{\"id\":28,\"color\":\"YELLOW\",\"level\":2,\"points\":7,\"cost\":{\"stone\":5},\"crafting\":{\"input\":{\"shield\":2},\"output\":{\"servant\":2},\"faith_output\":2}},{\"id\":29,\"color\":\"GREEN\",\"level\":2,\"points\":8,\"cost\":{\"shield\":3,\"gold\":3},\"crafting\":{\"input\":{\"gold\":1},\"output\":{\"shield\":2},\"faith_output\":1}},{\"id\":30,\"color\":\"PURPLE\",\"level\":2,\"points\":8,\"cost\":{\"servant\":3,\"shield\":3},\"crafting\":{\"input\":{\"stone\":1},\"output\":{\"servant\":2},\"faith_output\":1}},{\"id\":31,\"color\":\"BLUE\",\"level\":2,\"points\":8,\"cost\":{\"gold\":3,\"stone\":3},\"crafting\":{\"input\":{\"servant\":1},\"output\":{\"stone\":2},\"faith_output\":1}},{\"id\":32,\"color\":\"YELLOW\",\"level\":2,\"points\":8,\"cost\":{\"stone\":3,\"servant\":3},\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"gold\":2},\"faith_output\":1}},{\"id\":33,\"color\":\"GREEN\",\"level\":3,\"points\":9,\"cost\":{\"shield\":6},\"crafting\":{\"input\":{\"gold\":2},\"output\":{\"stone\":3},\"faith_output\":2}},{\"id\":34,\"color\":\"PURPLE\",\"level\":3,\"points\":9,\"cost\":{\"servant\":6},\"crafting\":{\"input\":{\"stone\":2},\"output\":{\"gold\":3},\"faith_output\":2}},{\"id\":35,\"color\":\"BLUE\",\"level\":3,\"points\":9,\"cost\":{\"gold\":6},\"crafting\":{\"input\":{\"servant\":2},\"output\":{\"shield\":3},\"faith_output\":2}},{\"id\":36,\"color\":\"YELLOW\",\"level\":3,\"points\":9,\"cost\":{\"stone\":6},\"crafting\":{\"input\":{\"shield\":2},\"output\":{\"servant\":3},\"faith_output\":2}},{\"id\":37,\"color\":\"GREEN\",\"level\":3,\"points\":10,\"cost\":{\"shield\":5,\"servant\":2},\"crafting\":{\"input\":{\"gold\":1,\"servant\":1},\"output\":{\"shield\":2,\"stone\":2},\"faith_output\":1}},{\"id\":38,\"color\":\"PURPLE\",\"level\":3,\"points\":10,\"cost\":{\"servant\":5,\"gold\":2},\"crafting\":{\"input\":{\"stone\":1,\"shield\":1},\"output\":{\"gold\":2,\"servant\":2},\"faith_output\":1}},{\"id\":39,\"color\":\"BLUE\",\"level\":3,\"points\":10,\"cost\":{\"gold\":5,\"stone\":2},\"crafting\":{\"input\":{\"gold\":1,\"shield\":1},\"output\":{\"servant\":2,\"stone\":2},\"faith_output\":1}},{\"id\":40,\"color\":\"YELLOW\",\"level\":3,\"points\":10,\"cost\":{\"stone\":5,\"servant\":2},\"crafting\":{\"input\":{\"stone\":1,\"servant\":1},\"output\":{\"gold\":2,\"shield\":2},\"faith_output\":1}},{\"id\":41,\"color\":\"GREEN\",\"level\":3,\"points\":11,\"cost\":{\"shield\":7},\"crafting\":{\"input\":{\"servant\":1},\"output\":{\"gold\":1},\"faith_output\":3}},{\"id\":42,\"color\":\"PURPLE\",\"level\":3,\"points\":11,\"cost\":{\"servant\":7},\"crafting\":{\"input\":{\"gold\":1},\"output\":{\"stone\":1},\"faith_output\":3}},{\"id\":43,\"color\":\"BLUE\",\"level\":3,\"points\":11,\"cost\":{\"gold\":7},\"crafting\":{\"input\":{\"stone\":1},\"output\":{\"shield\":1},\"faith_output\":3}},{\"id\":44,\"color\":\"YELLOW\",\"level\":3,\"points\":11,\"cost\":{\"stone\":7},\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"servant\":1},\"faith_output\":3}},{\"id\":45,\"color\":\"GREEN\",\"level\":3,\"points\":12,\"cost\":{\"shield\":4,\"gold\":4},\"crafting\":{\"input\":{\"stone\":1},\"output\":{\"gold\":3,\"shield\":1},\"faith_output\":0}},{\"id\":46,\"color\":\"PURPLE\",\"level\":3,\"points\":12,\"cost\":{\"servant\":4,\"shield\":4},\"crafting\":{\"input\":{\"gold\":1},\"output\":{\"stone\":3,\"servant\":1},\"faith_output\":0}},{\"id\":47,\"color\":\"BLUE\",\"level\":3,\"points\":12,\"cost\":{\"gold\":4,\"stone\":4},\"crafting\":{\"input\":{\"servant\":1},\"output\":{\"shield\":3,\"gold\":1},\"faith_output\":0}},{\"id\":48,\"color\":\"YELLOW\",\"level\":3,\"points\":12,\"cost\":{\"stone\":4,\"servant\":4},\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"servant\":3,\"stone\":1},\"faith_output\":0}}],\"base\":[{\"input\":{\"any\":2},\"output\":{\"any\":1},\"faith_output\":0}]}",
                "{\"tiles\":[{\"order\":0,\"x\":1,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":1,\"x\":2,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":2,\"x\":3,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":3,\"x\":3,\"y\":2,\"points\":1,\"pope_group\":0,\"pope_check\":false},{\"order\":4,\"x\":3,\"y\":3,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":5,\"x\":4,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":6,\"x\":5,\"y\":3,\"points\":1,\"pope_group\":1,\"pope_check\":false},{\"order\":7,\"x\":6,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":8,\"x\":7,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":true},{\"order\":9,\"x\":8,\"y\":3,\"points\":2,\"pope_group\":0,\"pope_check\":false},{\"order\":10,\"x\":8,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":11,\"x\":8,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":12,\"x\":9,\"y\":1,\"points\":2,\"pope_group\":2,\"pope_check\":false},{\"order\":13,\"x\":10,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":14,\"x\":11,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":15,\"x\":12,\"y\":1,\"points\":3,\"pope_group\":2,\"pope_check\":false},{\"order\":16,\"x\":13,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":true},{\"order\":17,\"x\":13,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":18,\"x\":13,\"y\":3,\"points\":3,\"pope_group\":0,\"pope_check\":false},{\"order\":19,\"x\":14,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":20,\"x\":15,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":21,\"x\":16,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":false},{\"order\":22,\"x\":17,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":23,\"x\":18,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":24,\"x\":19,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":true}],\"groups\":[{\"group\":1,\"points\":2},{\"group\":2,\"points\":3},{\"group\":3,\"points\":4}]}",
                "{\"cards\":[{\"id\":1,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":1},{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"servant\",\"amount\":1}]},{\"id\":2,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"shield\",\"amount\":1}]},{\"id\":3,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":1},{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"stone\",\"amount\":1}]},{\"id\":4,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":1},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"gold\",\"amount\":1}]},{\"id\":5,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"gold\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_5\",\"accepted_types\":\"stone\",\"amount\":2}]},{\"id\":6,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"stone\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_6\",\"accepted_types\":\"servant\",\"amount\":2}]},{\"id\":7,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"servant\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_7\",\"accepted_types\":\"shield\",\"amount\":2}]},{\"id\":8,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"shield\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_8\",\"accepted_types\":\"gold\",\"amount\":2}]},{\"id\":9,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":2},{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"servant\"],\"faith_output\":0}]},{\"id\":10,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":2},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"shield\"],\"faith_output\":0}]},{\"id\":11,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":2},{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"stone\"],\"faith_output\":0}]},{\"id\":12,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":2},{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"gold\"],\"faith_output\":0}]},{\"id\":13,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"YELLOW\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"any\":1},\"faith_output\":1}}]},{\"id\":14,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"BLUE\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"servant\":1},\"output\":{\"any\":1},\"faith_output\":1}}]},{\"id\":15,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"PURPLE\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"stone\":1},\"output\":{\"any\":1},\"faith_output\":1}}]},{\"id\":16,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"GREEN\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"gold\":1},\"output\":{\"any\":1},\"faith_output\":1}}]}]}");

        List<Marble> marbles = Arrays.asList(
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.PURPLE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.RED),
                MarbleFactory.createMarble(MarbleColor.WHITE)
        );
        Market myMarket = new Market(marbles, MarbleFactory.createMarble(MarbleColor.PURPLE), 3, 4);
        model.getMarket().changeMarket(new RawMarket(myMarket));

        CliBuilder.createGameFrames(framework, model);

        model.getPlayers().get(2).getCupboard().get(2).changeResources(
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                }},
                                "delta"
                        )
                )
        );
        model.getPlayers().get(2).getCupboard().get(2).selectResources("gold", 1);

        framework.setActiveFrame("player_1");
        framework.renderActiveFrame();

        framework.setActiveFrame("player_2");
        framework.renderActiveFrame();

        framework.setActiveFrame("player_3");
        framework.renderActiveFrame();

        framework.setActiveFrame("global");
        framework.renderActiveFrame();
    }

    public static void generalFrameworkTest() throws UnableToDrawElementException, ParserException {
        Frame frame = new Frame("board");
        Frame global = new Frame("global");

        // MODEL
        ClientPlayer player1 = new ClientPlayer("Player 1", null, null, null, null, null, null,
                null, null, null, null);
        ClientModel clientModel = new ClientModel();
        clientModel.setPlayers(new ArrayList<>() {{
            add(player1);
            add(new ClientPlayer("Player 2", null, null, null, null, null, null,
                    null, null, null, null));
            add(new ClientPlayer("Player 3", null, null, null, null, null, null,
                    null, null, null, null));
            add(new ClientPlayer("P".repeat(24), null, null, null, null, null, null,
                    null, null, null, null));
        }});
        clientModel.setCurrentPlayer(player1);
        ModelCliUpdater modelCliUpdater1 = new ModelCliUpdater(clientModel, frame, "Player 3");
        ModelCliUpdater modelCliUpdater2 = new ModelCliUpdater(clientModel, global, null);

        // faith path
        it.polimi.ingsw.server.model.faithpath.FaithPath path = JSONParser.parseFaithPath("{\"tiles\":[{\"order\":0,\"x\":1,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":1,\"x\":2,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":2,\"x\":3,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":3,\"x\":3,\"y\":2,\"points\":1,\"pope_group\":0,\"pope_check\":false},{\"order\":4,\"x\":3,\"y\":3,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":5,\"x\":4,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":6,\"x\":5,\"y\":3,\"points\":1,\"pope_group\":1,\"pope_check\":false},{\"order\":7,\"x\":6,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":8,\"x\":7,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":true},{\"order\":9,\"x\":8,\"y\":3,\"points\":2,\"pope_group\":0,\"pope_check\":false},{\"order\":10,\"x\":8,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":11,\"x\":8,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":12,\"x\":9,\"y\":1,\"points\":2,\"pope_group\":2,\"pope_check\":false},{\"order\":13,\"x\":10,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":14,\"x\":11,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":15,\"x\":12,\"y\":1,\"points\":3,\"pope_group\":2,\"pope_check\":false},{\"order\":16,\"x\":13,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":true},{\"order\":17,\"x\":13,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":18,\"x\":13,\"y\":3,\"points\":3,\"pope_group\":0,\"pope_check\":false},{\"order\":19,\"x\":14,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":20,\"x\":15,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":21,\"x\":16,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":false},{\"order\":22,\"x\":17,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":23,\"x\":18,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":24,\"x\":19,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":true}],\"groups\":[{\"group\":1,\"points\":2},{\"group\":2,\"points\":3},{\"group\":3,\"points\":4}]}");
        ClientFaithPath faithPath = new ClientFaithPath(
                path.getTiles().stream().map(FaithPathTile::toRaw).collect(Collectors.toList()),
                path.getFaithGroupList().stream().map(FaithPathGroup::toRaw).collect(Collectors.toList())
        );
        faithPath.getCheckpointStatus().set(0, FaithHolder.CheckpointStatus.ACTIVE);
        faithPath.getCheckpointStatus().set(1, FaithHolder.CheckpointStatus.INACTIVE);
        faithPath.addFaithPoints(3);
        FaithPathCliUpdater faithPathCliUpdater = new FaithPathCliUpdater(faithPath, frame, false);

        // discount holder
        ClientDiscountHolder discountHolder = new ClientDiscountHolder();
        discountHolder.addDiscount("gold", 1);
        discountHolder.addDiscount("stone", 2);
        DiscountHolderCliUpdater discountHolderCliUpdater = new DiscountHolderCliUpdater(discountHolder, frame);

        // flag holder
        ClientFlagHolder flagHolder = new ClientFlagHolder();
        flagHolder.addFlag(new RawLevelFlag(new LevelFlag(FlagColor.BLUE, 2)));
        flagHolder.addFlag(new RawLevelFlag(new LevelFlag(FlagColor.GREEN, 1)));
        flagHolder.addFlag(new RawLevelFlag(new LevelFlag(FlagColor.GREEN, 1)));
        FlagHolderCliUpdater flagHolderCliUpdater = new FlagHolderCliUpdater(flagHolder, frame);

        // production
        ClientProduction production = new ClientProduction(3);
        production.addUpgradableCrafting(
                new RawCrafting(
                        new Crafting(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
                                }},
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                }},
                                1)
                ), 2, 0
        );
        production.addUpgradableCrafting(
                new RawCrafting(
                        new Crafting(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 1);
                                }},
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 3);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 3);
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 3);
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 3);
                                }},
                                5)
                ), 3, 1
        );
        production.addBaseCrafting(
                new RawCrafting(
                        new Crafting(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getAnyResource(), 2);
                                }},
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);
                                }},
                                0)
                )
        );
        production.addLeaderCrafting(
                new RawCrafting(
                        new Crafting(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                }},
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                }},
                                0)
                )
        );
        ProductionCliUpdater productionCliUpdater = new ProductionCliUpdater(production, frame);

        // hand
        ClientBaseStorage hand = new ClientBaseStorage(
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                }},
                                "hand"
                        )
                )
        );
        BaseStorageCliUpdater handCliUpdater = new BaseStorageCliUpdater(hand, frame, 19, 85,
                "Hand", false);

        // chest
        ClientBaseStorage chest = new ClientBaseStorage(
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 3);
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
                                }},
                                "chest"
                        )
                )
        );
        BaseStorageCliUpdater chestCliUpdater = new BaseStorageCliUpdater(chest, frame, 19, 108,
                "Chest", false);

        // basket
        ClientBaseStorage basket = new ClientBaseStorage(
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    // put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
                                }},
                                "basket"
                        )
                )
        );
        BaseStorageCliUpdater basketCliUpdater = new BaseStorageCliUpdater(basket, frame, 19, 62,
                "Market Basket", true);

        // cupboard
        List<ClientShelf> cupboard = new ArrayList<>();
        List<ClientShelf> leaderShelves = new ArrayList<>();
        cupboard.add(new ClientShelf("top", "any", 1,
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
                                }},
                                "top"
                        )
                )));
        cupboard.add(new ClientShelf("middle", "any", 2,
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 1);
                                }},
                                "middle"
                        )
                )));
        cupboard.add(new ClientShelf("bottom", "any", 3,
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                }},
                                "bottom"
                        )
                )));
        leaderShelves.add(new ClientShelf("leader 1", "stone", 2,
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 1);
                                }},
                                "leader 1"
                        )
                )));
        leaderShelves.add(new ClientShelf("leader 2", "servant", 2,
                new RawStorage(
                        new BaseStorage(
                                new HashMap<>() {{
                                    // put(ResourceTypeSingleton.getInstance().getServantResource(), 0);
                                }},
                                "leader 2"
                        )
                )));
        ClientPlayer player = new ClientPlayer("Name", null, null, null, cupboard,
                leaderShelves, null, null, null, null, null);
        player.addVictoryPoints(12);
        PlayerCliUpdater playerCliUpdater = new PlayerCliUpdater(player, frame);

        // leader cards
        ClientLeaderCards clientLeaderCards = new ClientLeaderCards();
        clientLeaderCards.changeCoveredCardsNumber(2);
        clientLeaderCards.addLeaderCard(
                new RawLeaderCard(
                        new LeaderCard(1, "Lorenzo", 12,
                                new ArrayList<>() {{
                                    add(new ConversionAbility(MarbleColor.WHITE, new ConversionActuator(Arrays.asList(
                                            ResourceTypeSingleton.getInstance().getServantResource(),
                                            ResourceTypeSingleton.getInstance().getShieldResource()), 1)));
                                    add(new DiscountAbility(2, ResourceTypeSingleton.getInstance().getGoldResource()));
                                    add(new StorageAbility(new Shelf("leader", ResourceTypeSingleton.getInstance().getStoneResource(), 2)));
                                }},
                                new ArrayList<>() {{
                                    add(new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1));
                                    add(new FlagRequirement(new BaseFlag(FlagColor.GREEN), 1));
                                    add(new LevelFlagRequirement(new LevelFlag(FlagColor.YELLOW, 1), 1));
                                    add(new ResourceRequirement(ResourceTypeSingleton.getInstance().getServantResource(), 2));
                                }})
                )
        );
        clientLeaderCards.addLeaderCard(
                new RawLeaderCard(
                        new LeaderCard(2, "Paolo", 9,
                                new ArrayList<>() {{
                                    add(new CraftingAbility(new Crafting(
                                            new HashMap<>() {{
                                                put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                            }},
                                            new HashMap<>() {{
                                                put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                            }},
                                            2
                                    )));
                                    add(new DiscountAbility(2, ResourceTypeSingleton.getInstance().getGoldResource()));
                                }},
                                new ArrayList<>() {{
                                    add(new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1));
                                    add(new FlagRequirement(new BaseFlag(FlagColor.GREEN), 1));
                                }})
                )
        );
        LeaderCardsCliUpdater leaderCardsCliUpdater = new LeaderCardsCliUpdater(clientLeaderCards, frame);

        // personal data
        PersonalData personalData = new PersonalData();
        personalData.addServerMessage("Test message 1 is this one");
        personalData.addServerMessage("Test message 2 is that one instead a b c d e f g h");
        personalData.addError("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ut tempus lacus, accumsan pellentesque lacus. Suspendisse potenti.");
        PersonalDataCliUpdater personalDataCliUpdater = new PersonalDataCliUpdater(personalData, frame, 12, 126);
        PersonalDataCliUpdater personalDataCliUpdaterGlobal = new PersonalDataCliUpdater(personalData, global, 10, 2);

        // market
        ClientMarket clientMarket = new ClientMarket(3, 4);

        List<Marble> marbles = Arrays.asList(
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.PURPLE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.RED),
                MarbleFactory.createMarble(MarbleColor.WHITE)
        );
        Market myMarket2 = new Market(marbles, MarbleFactory.createMarble(MarbleColor.PURPLE), 3, 4);
        clientMarket.changeMarket(new RawMarket(myMarket2));

        // new ConversionOption(Arrays.asList("gold", "shield"), 3)
        clientMarket.changePossibleConversions(
                Arrays.asList(MarbleColor.WHITE, MarbleColor.RED, MarbleColor.GREY, MarbleColor.BLUE),
                Arrays.asList(
                        Arrays.asList(
                                new ConversionOption(Arrays.asList("gold", "stone"), 0),
                                new ConversionOption(Arrays.asList("servant"), 1)
                        ),
                        Arrays.asList(
                                new ConversionOption(Arrays.asList("gold", "servant"), 1)
                        ),
                        Arrays.asList(
                                new ConversionOption(Arrays.asList("gold", "servant"), 2)
                        ),
                        Collections.emptyList()
                )
        );
        MarketCliUpdater marketCliUpdater = new MarketCliUpdater(clientMarket, global);

        // shop
        ClientShop clientShop = new ClientShop(3, 4);
        clientShop.selectCard(0, 1);
        clientShop.changeCard(0, 0, new RawCraftingCard(
                new CraftingCard(
                        1,
                        new LevelFlag(FlagColor.BLUE, 1),
                        new HashMap<>() {{
                            put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                            put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                            put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                        }},
                        new UpgradableCrafting(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                }},
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                }},
                                2,
                                1
                        ),
                        12)
        ));
        clientShop.changeCard(0, 1, new RawCraftingCard(
                new CraftingCard(
                        2,
                        new LevelFlag(FlagColor.PURPLE, 3),
                        new HashMap<>() {{
                            put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                            put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                            put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                        }},
                        new UpgradableCrafting(
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                }},
                                new HashMap<>() {{
                                    put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                    put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                }},
                                2,
                                3
                        ),
                        8)
        ));
        ShopCliUpdater shopCliUpdater = new ShopCliUpdater(clientShop, global);

        CliFramework framework = new CliFramework(true, Arrays.asList(frame, global));

        framework.setActiveFrame("global");
        framework.renderActiveFrame();

        framework.setActiveFrame("board");
        framework.renderActiveFrame();
    }

    public static void faithPathTest() throws UnableToDrawElementException, InterruptedException, ParserException, IOException {
        it.polimi.ingsw.server.model.faithpath.FaithPath path= JSONParser.parseFaithPath("{\"tiles\":[{\"order\":0,\"x\":1,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":1,\"x\":2,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":2,\"x\":3,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":3,\"x\":3,\"y\":2,\"points\":1,\"pope_group\":0,\"pope_check\":false},{\"order\":4,\"x\":3,\"y\":3,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":5,\"x\":4,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":6,\"x\":5,\"y\":3,\"points\":1,\"pope_group\":1,\"pope_check\":false},{\"order\":7,\"x\":6,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":8,\"x\":7,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":true},{\"order\":9,\"x\":8,\"y\":3,\"points\":2,\"pope_group\":0,\"pope_check\":false},{\"order\":10,\"x\":8,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":11,\"x\":8,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":12,\"x\":9,\"y\":1,\"points\":2,\"pope_group\":2,\"pope_check\":false},{\"order\":13,\"x\":10,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":14,\"x\":11,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":15,\"x\":12,\"y\":1,\"points\":3,\"pope_group\":2,\"pope_check\":false},{\"order\":16,\"x\":13,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":true},{\"order\":17,\"x\":13,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":18,\"x\":13,\"y\":3,\"points\":3,\"pope_group\":0,\"pope_check\":false},{\"order\":19,\"x\":14,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":20,\"x\":15,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":21,\"x\":16,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":false},{\"order\":22,\"x\":17,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":23,\"x\":18,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":24,\"x\":19,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":true}],\"groups\":[{\"group\":1,\"points\":2},{\"group\":2,\"points\":3},{\"group\":3,\"points\":4}]}");

        FaithPath fp = new FaithPath("fp", 1, 30,
                path.getTiles().stream().map(FaithPathTile::toRaw).collect(Collectors.toList()),
                path.getFaithGroupList().stream().map(FaithPathGroup::toRaw).collect(Collectors.toList())
        );

        Frame frame = new Frame("frame", Collections.singletonList(fp));
        frame.setBackgroundColor(BackgroundColor.BLUE);

        CliFramework framework = new CliFramework(true, Collections.singletonList(frame));
        framework.setActiveFrame("frame");

        framework.renderActiveFrame();
    }

    public static void flagTest() throws UnableToDrawElementException {
        FlagBoxWithAmount blue = new FlagBoxWithAmount("blue", 5, 5, 3, "blue", 2);
        FlagBoxWithAmount purple = new FlagBoxWithAmount("purple", 5, 9, 2, "purple", 2);
        FlagBoxWithAmount yellow = new FlagBoxWithAmount("yellow", 5, 13, 1, "yellow", 2);

        Frame frame = new Frame("frame", Arrays.asList(blue, purple, yellow));
        frame.setBackgroundColor(BackgroundColor.BLACK);

        CliFramework framework = new CliFramework(true, Collections.singletonList(frame));
        framework.setActiveFrame("frame");

        framework.renderActiveFrame();
    }

    public static void resourceTest() throws UnableToDrawElementException {
        ResourceBox gold = new ResourceBox("gold", 5, 5, "gold");
        ResourceBox stone = new ResourceBox("stone", 6, 5, "stone");
        ResourceBox shield = new ResourceBox("shield", 7, 5, "shield");
        ResourceBox servant = new ResourceBox("servant", 8, 5, "servant");
        ResourceBox faith = new ResourceBox("faith", 9, 5, "faith");
        ResourceBox any = new ResourceBox("any", 10, 5, "any");

        Frame frame = new Frame("frame", Arrays.asList(gold, stone, shield, servant, faith, any));
        frame.setBackgroundColor(BackgroundColor.BLACK);

        CliFramework framework = new CliFramework(true, Collections.singletonList(frame));
        framework.setActiveFrame("frame");

        framework.renderActiveFrame();
    }

    public static void frameworkTest() throws UnableToDrawElementException, InterruptedException {
        FixedTextBox blue = new FixedTextBox("blue", 5, 5, 5, "BLUE", ForegroundColor.BLUE, BackgroundColor.BLACK);
        FixedTextBox red = new FixedTextBox("red", 6, 5, 5, "RED", ForegroundColor.RED, BackgroundColor.BLACK);
        FixedTextBox green = new FixedTextBox("green", 7, 5, 5, "GREEN", ForegroundColor.GREEN, BackgroundColor.BLACK);

        GroupBox box = new GroupBox("box", 4, 3, 9, 14, "TEST", ForegroundColor.WHITE, BackgroundColor.BLACK_BRIGHT,
                Arrays.asList(new FixedTextBox[]{blue, red, green}));

        box.setDoubleBorder(true);
        box.setAlignLeft(true);

        Frame frame = new Frame("frame", Collections.singletonList(box));
        frame.setBackgroundColor(BackgroundColor.BLUE_BRIGHT);

        CliFramework framework = new CliFramework(true, Collections.singletonList(frame));
        framework.setActiveFrame("frame");

        while (true) {
            blue.setStartingColumn(blue.getStartingColumn() + 5);
            red.setStartingColumn(red.getStartingColumn() + 5);
            green.setStartingColumn(green.getStartingColumn() + 5);

            box.setEndingColumn(box.getEndingColumn() + 5);
            box.setStartingColumn(box.getStartingColumn() + 5);

            framework.renderActiveFrame();

            Thread.sleep(1000);
        }
    }

    public static void consoleTest() {
        Thread thread = new Thread(() -> {
            OutputHandler out = new OutputHandler(true);

            out.setBackgroundColorRectangle(0, 0, out.getHeight()-1, out.getWidth()-1, BackgroundColor.BLUE);
            out.setForegroundColorRectangle(0, 0, out.getHeight()-1, out.getWidth()-1, ForegroundColor.WHITE_BRIGHT);

            out.setBackgroundColorRectangle(0, 0, out.getHeight()-1, 1, BackgroundColor.WHITE_BRIGHT);
            out.setBackgroundColorRectangle(0, 0, 0, out.getWidth()-1, BackgroundColor.WHITE_BRIGHT);
            out.setBackgroundColorRectangle(out.getHeight()-1, 0, out.getHeight()-1, out.getWidth()-1, BackgroundColor.WHITE_BRIGHT);
            out.setBackgroundColorRectangle(0, out.getWidth()-2, out.getHeight()-1, out.getWidth()-1, BackgroundColor.WHITE_BRIGHT);

            out.setString(7, 30, "ADJUST TERMINAL SIZE TO FIT WINDOW", ForegroundColor.WHITE_BRIGHT);
            out.setString(8, 30, "SEND ANY COMMAND TO CONFIRM", ForegroundColor.WHITE_BRIGHT);

            while(true) {
                out.setCharRectangle(5, 6, 9, 14, out.getBlank());
                out.setCharRectangle(5, 10, 9, 10, '#');

                out.update();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                out.setCharRectangle(5, 6, 9, 14, out.getBlank());
                out.setCharRectangle(7, 6, 7, 14, '#');

                out.update();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        Scanner in = new Scanner(System.in);
        in.nextLine();

        thread.stop();
    }

    public static void parseTest() {
        JSONParser.setDebugMode(false);

        try {
            JSONParser.parseLeaders(Paths.get("src/main/leaders.json"));
            JSONParser.parseCraftingCards(Paths.get("src/main/crafting.json"));
            JSONParser.parseBaseCrafting(Paths.get("src/main/crafting.json"));
            JSONParser.parseFaithPath(Paths.get("src/main/faith.json"));
        } catch (Exception e) {
            Logger.log(e.getClass().getSimpleName() + " - " + e.getMessage(),
                    Logger.Severity.ERROR, ForegroundColor.RED);
        }
    }
}
