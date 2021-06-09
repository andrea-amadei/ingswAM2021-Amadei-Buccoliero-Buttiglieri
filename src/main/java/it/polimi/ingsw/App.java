package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientGameBuilder;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.cli.CliBuilder;
import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.client.cli.framework.OutputHandler;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.cli.updaters.*;
import it.polimi.ingsw.client.model.*;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.FaithPathGroup;
import it.polimi.ingsw.model.FaithPathTile;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.*;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.io.IOException;
import java.nio.file.Paths;

import java.util.*;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) throws UnableToDrawElementException {
        if(args.length == 0)
            ClientMain.main(args);
        else if(args[0].equalsIgnoreCase("server"))
            ServerMain.main(Arrays.copyOfRange(args, 1, args.length));
        else if(args[0].equalsIgnoreCase("client"))
            ClientMain.main(Arrays.copyOfRange(args, 1, args.length));
        else
            System.out.println("Unexpected argument " + args[0] + ". Please use 'client' or 'server'");
    }
}
