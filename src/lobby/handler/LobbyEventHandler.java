package lobby.handler;

import player.IPlayer;

import java.util.List;


public interface LobbyEventHandler {
    /*
        TODO: Only called on Lobby Host. Protocol may change to allow these to be called for lobby joiners as well.
        onPlayerJoin to be called when a player has been accepted by the Host into the lobby.
    */
    public void onPlayerJoin(int playerid);
    public void onPlayerLeave(int playerid);

    /*
        Indicate progress of ping messages, could be used to show in UI who is holding up the game.
     */
    public void onPingStart(); /* indicates host has started game */
    public void onPingReceive(int playerid);

    /*
        Indicate progress of ready message/acknowledgements. Again, could be used to show who's holding it up.

        TODO: Could have a more generic system for acknowledgment callbacks.
     */
    public void onReady();
    public void onReadyAcknowledge(int playerid);

    /*
        Indicate start of player order dice roll.
     */
    public void onDicePlayerOrder();

    /*
        Indicate progress of a dice roll. Used for both player order and card shuffle dice roll.
     */
    public void onDiceHash(int playerid);
    public void onDiceNumber(int playerid);

    /*
        Indicate start of card shuffle dice roll.
     */
    public void onDiceCardShuffle();

    /**
     * Called when the lobby is complete.
     * The next step should be the "setup" phase of the Risk game.
     * @param players - List of players in play order.
     * @param cards - List of cards used in game, shuffled. TODO: Card format etc needs thinking about. These will be encrypted in future too..
     * @param board - Some board format. TODO: Look at logic.Board
     */
    public void onLobbyComplete(List<IPlayer> players, List<Object> cards, Object board);

    /**
     * Called on Error / Exception which causes joining or hosting the lobby to fail.
     * @param e - Exception / Error / general throwable
     *
     * @note thread will exit after this has occured.
     */
    public void onFailure(Throwable e);
}
