package lobby.handler;


import networking.LobbyClient;

public interface HostLobbyEventHandler extends LobbyEventHandler {

    /*
        Called when a player connects and sends a join_game message.
        @return null to accept the player
        @return String - "Human readable reason sent to the rejected player" - to reject the player
     */
    public String onPlayerJoinRequest(LobbyClient client);

}
