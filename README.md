CS3099 - Risk
===============================

Contributors
------------

- Nathan Blades, nb48
- Victor Andrei, va9
- James Russel, jr79
- Adam Casey, ac248

Third Party Contributions
-------------------------
We should find licences for these really.
Apache Commons codec
Apache Commons lang
Apache Commons math
JSON-simple
hamcrest-core
JUnit

We should also try to credit other resources used in our game
 * * * * * * * *  * ** * Do this

System Requirements
-------------------
- The JVM uses around `300MB` of RAM when playing the game.
- `Java 8` is required

Running the Game
----------------
You can run the game by executing the `Risk.jar` file.

### Running Other Aspects of the Project
The following extra components can be built using `ant all`

1. Play a Risk game locally on the command line: `PlayCLI.jar`
2. Spectate an AI match on the command line: `WatchCLI.jar`
3. Play a network Risk game on the command line: `NetworkCLI.jar`

Hosting a Game
--------------
### 1. Start the Lobby
To host a game, load the game and press the `Create Game` button.

Fill out your `Player Name`.
Decide who will take your turns: `Yourself`, or an `AI`.
Set the maximum lobby size, the listen `Port` and `Interface IP`.

Once you have filled these out, hit `Start Lobby`

#### Listen IP Address
If you are hosting a game for other computers on your network to join, the listen IP address must be set to either your `Assigned IP` on that network, or to `0.0.0.0`.

`0.0.0.0` will attempt to bind to all available interfaces.

### 2. Start the Game
Once at least 3 players are in the lobby, you can start a a game by pressing `Start Game`

Joining a Game
--------------
To join a game, load the game and press `Direct Connect`.

- Fill out your `Player Name`.
- Decide who will take your turns, `Yourself`, or one of the `AIs`.
- Set the `IP Address` and `Port` of the lobby you wish to connect to.
- Press `Connect` to join the lobby.

The game will begin when the Host decides to play.

Building the Game
-----------------
Our project uses the `ant` build system.

Simply running `ant` in the folder containing build.xml will build the project.

`ant all`

Running the Test Suite
----------------------
All tests are run with the `ant test`.
