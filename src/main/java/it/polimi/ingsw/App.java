package it.polimi.ingsw;

import it.polimi.ingsw.server.Console;

public class App {
    public static void main(String[] args) {
        Console.log("Hello World! by gio e cate", Console.Severity.WARNING, Console.Format.RED);
        Console.log("Hello World! by gio e cate", Console.Severity.WARNING, Console.Format.YELLOW);
        Console.log("Hello World! by gio e cate", Console.Severity.WARNING, Console.Format.GREEN);
        Console.log("Hello World! by gio e cate", Console.Severity.WARNING, Console.Format.CYAN);
        Console.log("Hello World! by gio e cate", Console.Severity.WARNING, Console.Format.BLUE);
        Console.log("Hello World! by gio e cate", Console.Severity.WARNING, Console.Format.PURPLE);
    }
}
