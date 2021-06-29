package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.cli.updaters.*;
import it.polimi.ingsw.client.clientmodel.ClientModel;
import it.polimi.ingsw.client.clientmodel.ClientPlayer;

public final class CliBuilder {
    public static void createStartFrame(CliFramework framework, ClientModel model) {
        Frame frame = new Frame("start");
        new LobbyPersonalDataCliUpdater(model.getPersonalData(), frame);

        framework.addFrame(frame);
        framework.setActiveFrame("start");
    }

    public static void createGameFrames(CliFramework framework, ClientModel model) {
        Frame frame;
        ClientPlayer player;

        for(int i = 0; i < model.getPlayers().size(); i++) {
            frame = new Frame("player_" + (i + 1));
            player = model.getPlayers().get(i);

            new FaithPathCliUpdater(player.getFaithPath(), frame, false);

            new DiscountHolderCliUpdater(player.getDiscountHolder(), frame);
            new FlagHolderCliUpdater(player.getFlagHolder(), frame);

            new ProductionCliUpdater(player.getProduction(), frame);

            new BaseStorageCliUpdater(player.getHand(), frame, 19, 85, "Hand", false);
            new BaseStorageCliUpdater(player.getChest(), frame, 19, 108, "Chest", false);
            new BaseStorageCliUpdater(player.getMarketBasket(), frame, 19, 62, "Market Basket", true);

            new LeaderCardsCliUpdater(player.getLeaderCards(), frame);

            new PlayerCliUpdater(player, frame);

            new ModelCliUpdater(model, frame, model.getPlayers().get(i).getUsername());

            new PersonalDataCliUpdater(model.getPersonalData(), frame, 12, 126);

            new EndGameCliUpdater(model.getEndGameResults(), frame);

            framework.addFrame(frame);
        }

        if(model.getPlayers().size() == 1)
            new FaithPathCliUpdater(model.getPlayers().get(0).getFaithPath(), framework.getFrame("player_1"), true);

        frame = new Frame("global");
        new MarketCliUpdater(model.getMarket(), frame);
        new ShopCliUpdater(model.getShop(), frame);
        new ModelCliUpdater(model, frame, null);
        new PersonalDataCliUpdater(model.getPersonalData(), frame, 10, 2);
        new EndGameCliUpdater(model.getEndGameResults(), frame);
        framework.addFrame(frame);
    }
}
