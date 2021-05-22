package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.cli.updaters.*;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;

public final class CliBuilder {
    public static void createStartFrame(CliFramework framework, ClientModel model) {
        Frame frame = new Frame("start");
        new PersonalDataCliUpdater(model.getPersonalData(), frame);

        framework.addFrame(frame);
        framework.setActiveFrame("start");
    }

    public static void createGameFrames(CliFramework framework, ClientModel model) {
        Frame frame;
        ClientPlayer player;

        for(int i = 0; i < model.getPlayers().size(); i++) {
            frame = new Frame("player_" + (i + 1));
            player = model.getPlayers().get(i);

            new FaithPathCliUpdater(player.getFaithPath(), frame);

            new DiscountHolderCliUpdater(player.getDiscountHolder(), frame);
            new FlagHolderCliUpdater(player.getFlagHolder(), frame);

            new ProductionCliUpdater(player.getProduction(), frame);

            new BaseStorageCliUpdater(player.getHand(), frame, 19, 85, "Hand", false);
            new BaseStorageCliUpdater(player.getChest(), frame, 19, 108, "Chest", false);
            new BaseStorageCliUpdater(player.getMarketBasket(), frame, 19, 62, "Market Basket", true);

            new LeaderCardsCliUpdater(player.getLeaderCards(), frame);

            new PlayerCliUpdater(player, frame);

            framework.addFrame(frame);
        }

        frame = new Frame("global");
        new MarketCliUpdater(model.getMarket(), frame);
        new ShopCliUpdater(model.getShop(), frame);
        framework.addFrame(frame);
    }
}
