package lobby.handler;

import player.IPlayer;

import java.util.List;

/**
 * Event Handler for Joining a remote game.
 *
 * These callbacks can be used to display appropriate message/UI to user during the Lobby phase
 */
public interface JoinLobbyEventHandler extends LobbyEventHandler {
    /*
        Sent after successful TCP connect to host
     */
    public void onTCPConnect();

    /*
        Sent after receiving accept_join_game or reject_join_game
     */
    public void onJoinAccepted(int playerid);
    public void onJoinRejected(String message);
}
