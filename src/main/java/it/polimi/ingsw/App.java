package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.common.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.configurator.ConfiguratorMain;
import it.polimi.ingsw.server.ServerMain;

import java.util.Arrays;

public class App {

    public static void main(String[] args) throws UnableToDrawElementException {

        if(args.length == 0)
            ClientMain.main(args);
        else if(args[0].equalsIgnoreCase("server"))
            ServerMain.main(Arrays.copyOfRange(args, 1, args.length));
        else if(args[0].equalsIgnoreCase("client"))
            ClientMain.main(Arrays.copyOfRange(args, 1, args.length));
        else if(args[0].equalsIgnoreCase("configurator"))
            ConfiguratorMain.main(Arrays.copyOfRange(args, 1, args.length));
        else
            System.out.println("Unexpected argument " + args[0] + ". Please use 'client', 'server' or 'configurator'");

    }
}
