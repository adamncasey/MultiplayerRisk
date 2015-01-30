package lobby.handler;


import networking.Message;

public interface HostLobbyEventHandler extends LobbyEventHandler {

    /*
        TODO Integrate this into
        Called when a player connects and sends a join_game message.
        @return null to accept the player
        @return String - "Human readable reason sent to the rejected player" - to reject the player
     */
    public String onPlayerJoinRequest(Message joinGameMessage);

}
