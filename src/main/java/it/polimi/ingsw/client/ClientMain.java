package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.ClientCliMain;
import it.polimi.ingsw.client.gui.ClientGuiMain;
import it.polimi.ingsw.common.exceptions.UnableToDrawElementException;

import java.util.Arrays;

public class ClientMain {

    public static void main(String[] args) throws UnableToDrawElementException {
        if(args.length == 0)
            ClientGuiMain.main(args);
        else if(args[0].equalsIgnoreCase("gui"))
            ClientGuiMain.main(Arrays.copyOfRange(args, 1, args.length));
        else if(args[0].equalsIgnoreCase("cli"))
            ClientCliMain.main(Arrays.copyOfRange(args, 1, args.length));
        else
            System.out.println("Unexpected argument " + args[0] + ". Please use 'gui' or 'cli'");
    }
}
