package lobby.handler;

import player.IPlayer;

import java.util.List;


public interface LobbyEventHandler {
    /**
        TODO: Only called on Lobby Host. Protocol may change to allow these to be called for lobby joiners as well.
        onPlayerJoin to be called when a player has been accepted by the Host into the lobby.
    */
    void onPlayerJoin(int playerid);
    void onPlayerLeave(int playerid);

    /**
        Indicate progress of ping messages, could be used to show in UI who is holding up the game.
     */
    void onPingStart(); /* indicates host has started game */
    void onPingReceive(int playerid);

    /**
        Indicate progress of ready message/acknowledgements. Again, could be used to show who's holding it up.

        TODO: Could have a more generic system for acknowledgment callbacks.
     */
    void onReady();
    void onReadyAcknowledge(int playerid);

    /**
     * Called when initialise_game message is sent (if we are host player) or received (if we are joining)
     */
    void onInitialiseGame(double protocolVersion, String[] extendedFeatures);
    /**
        Indicate start of player order dice roll.
     */
    void onDicePlayerOrder();

    /**
        Indicate progress of a dice roll. Used for both player order and card shuffle dice roll.
     */
    void onDiceHash(int playerid);
    void onDiceNumber(int playerid);

    /**
        Indicate start of card shuffle dice roll.
     */
    void onDiceCardShuffle();

    /**
     * Called when the lobby is complete.
     * The next step should be the "setup" phase of the Risk game.
     * @param cards - List of cards used in game, shuffled. TODO: Card format etc needs thinking about. These will be encrypted in future too..
     *              TODO It seems unlikely that encrypted cards will happen anymore
     * @param playersBefore - List of players whose turn comes before the local turn
     * @param playersAfter - List of players whose turn comes after the local turn
     * @param cardIDs
     */
    void onLobbyComplete(List<IPlayer> playersBefore, List<IPlayer> playersAfter, List<Integer> cardIDs);

    /**
     * Called on Error / Exception which causes joining or hosting the lobby to fail.
     * @param e - Exception / Error / general throwable
     *
     * @note thread will exit after this has occured.
     */
    void onFailure(Throwable e);

}
