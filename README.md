# Polimi IngSFW final project 2021

## Group members
- ### Andrea Amadei
- ### Caterina Buccoliero
- ### Giorgio Buttiglieri

## Implemented functionalities

- Complete rules
- GUI
- CLI
- Socket

#### Advanced functionalities:

- Multiple matches
- Disconnection resilience
- Parameters editor

## Starting the application

If there are no parameters specified, the GUI starts at host localhost on port 6789.
If there are parameters, the first one must be “client”, “server” or “configurator”.
-	If the first parameter is “client”, there can either be no more parameters (default is GUI, localhost 6789) or the next parameter must be one between “cli” and “gui”. For both last cases the default option is localhost 6789, or if more parameters are specified they must be exactly two: host and port.
-	If the first parameter is “server”,  a single numeric parameter can follow (the port) or no parameters at all (default 6789).
-   If the first parameter is “configurator”, no other options are required

### All examples

| command | What starts |
|:-----------------------|:------------------------------------:|
| java -jar MastersOfRenaissance_1.0.jar | Starts client GUI, localhost 6789 |
| java -jar MastersOfRenaissance_1.0.jar client | Starts client GUI, localhost 6789 |
| java -jar MastersOfRenaissance_1.0.jar client gui | Starts client GUI, localhost 6789 |
| java -jar MastersOfRenaissance_1.0.jar client gui 132.5.4.3 1234 | Starts client GUI, at 132.5.4.3, port 1234 |
| java -jar MastersOfRenaissance_1.0.jar client cli | Starts client CLI, localhost 6789 |
| java -jar MastersOfRenaissance_1.0.jar client cli 123.4.3.2 1234 | Starts client CLI, at 123.4.3.2, port 1234 |
| java -jar MastersOfRenaissance_1.0.jar server | Starts server, port 6789 |
| java -jar MastersOfRenaissance_1.0.jar server 1234 | Starts server, port 1234 |
| java -jar MastersOfRenaissance_1.0.jar configurator | Starts configurator |

## All CLI commands

| CLI command | What it does |
|:-----------------------|:------------------------------------:|
| “set_username” + String | Sets String as the username of the player |
| “activate_leader” + int | Activates the leader card with ID int |
| “back” | The game goes back to its previous state |
| "buy_from_market” + bool + int | Buys the row (if bool = true) or column (if bool = false) indicated by int from the market grid |
| “collect_from_basket” + String1 + int + String2 | Moves int amount of resources String1 from the market basket to the container with ID String2 |
| “confirm” | Confirms the player’s choice |
| “confirm_tidy” | Confirms the player’s reorganization of their storage space |
| “create_match” + String + int + bool | Creates a match called String, for int players. Creates a single player game if bool = true |
| “discard_leader” + int | Discards the leader card with ID int |
| “join_match” + String | The player joins the match called String |
| “preliminary_pick” + int1 + int2+ (optional)String1 + (optional)int3 + (optional)String2 + (optional) int4 | The player discards the leader cards with indexes int1 and int2, and chooses int3 amount of resources String1 and int4 amount of resources String2 |
| “resources_move” + String1 + String2 + String3 + int | Moves int amount of resources String4 from the container with ID String1 to the container with ID String2 |
| “reconnect” + String | Reconnects the player with username String |
| “select_card” + int1 + int2 + int3 | Selects the card situated at row int1 and column int2 of the shop grid and puts it in the production slot number int3 |
| “select_conversions” + int1 + int2 + int3 + (optional) int4 | Selects the conversions of index int1, int2, int3 and eventually int4, among the available conversions for the marbles |
| “select_crafting” + String + int | Selects the crafting number int among the crafting of type String |
| “select_output” + String1 + int1 + (optional)String2 + (optional)int2 ecc… | Selects the output of a crafting of undecided output as intX amount of StringX type resources |
| “market” | Selects the player’s route as the market route |
| “shop” | Selects the player’s route as the shop route |
| “crafting” | Selects the player’s route as the crafting route |
| “select_resources” + String1 + String2 + int | Selects int amount of String2 type resources from the container with ID String1 |
| “start” | Shows the player's board and the player can start to play |
| “switch” + String | Shows the shop and market if String = “global”, shows the player X’s board if String = X |
| “okay” | Okay |
| “ok” | Okay |
| “k” | Okay |
| “exit” | Closes the game |

## Building the .jar
Use Maven to build the all-in-one .jar file. Issue, in this order, the following commands:
<br>
    ```
       > mvn clean    
    ```  
    ```
      > mvn package
    ```
<br>
The executable MastersOfRenaissance_1.0.jar file will be located in the target folder of the project

### Pre-compiled .jar
Inside the <i>deliverables</i> folder, it is possible to download pre-compiled all-in-one .jar files.
-   For <b>Windows</b> users: download the MastersOfRenaissance_1.0.jar file inside the <i>windows</i> folder
-   For <b>macOS</b> users: download the MastersOfRenaissance_1.0_MAC.jar file inside the <i>macos</i> folder

#### Warning!
Although both jar files are similar and should work on both OS, some macOS users may encounter problems while running the GUI application or the configurator. This might occur due to an incompatibility with some JavaFX runtimes on newer versions of macOS. If something goes wrong, please build the .jar yourself as described above.

## Tools used
- IntelliJ
- JavaFX's Scene Builder
- Maven
- StarUML

## Libraries used
- GSON
- JavaFX
- ControlFX (a JavaFX plugin)
- Shaded (a Maven plugin)