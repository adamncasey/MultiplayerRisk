package test.lobby;

import java.util.ArrayList;
import java.util.List;

import lobby.handler.HostLobbyEventHandler;
import networking.LobbyClient;
import networking.PortInUseException;
import lobby.ILobby;
import lobby.Lobby;
import lobby.LocalGameLobby;
import player.IPlayer;

public class LobbyDevMain {

	public static void main(String[] args) {
        
		System.setProperty("java.net.preferIPv4Stack" , "true");
		
		ILobby lobby = new Lobby();
		LocalGameLobby localLobby;
		
		// Create local lobby.
		try {
			
			localLobby = lobby.createGameLobby("James' Lobby", 1, handler);
			
			ArrayList<LobbyClient> players;
			while(true) {
                
                try {
                    Thread.sleep(3000);
                } 
                catch (InterruptedException e) { }
            }
			
			
		} catch (PortInUseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

    public static HostLobbyEventHandler handler = new HostLobbyEventHandler() {
        @Override
        public String onPlayerJoinRequest(LobbyClient client) {
            System.out.println("onPLayerJoinRequest " + client.supportedVersions.toString());
            return null; // Accept the player
        }

        @Override
        public void onPlayerJoin(int playerid) {
            System.out.println("onPlayerJoin " + playerid);
        }

        @Override
        public void onPlayerLeave(int playerid) {
            System.out.println("onPlayerLeave " + playerid);
        }

        @Override
        public void onPingStart() {

            System.out.println("onPingStart ");
        }

        @Override
        public void onPingReceive(int playerid) {
            System.out.println("onPingReceive " + playerid);

        }

        @Override
        public void onReady() {
            System.out.println("onReady ");

        }

        @Override
        public void onReadyAcknowledge(int playerid) {
            System.out.println("onReadyAcknowledge " + playerid);

        }

        @Override
        public void onDicePlayerOrder() {
            System.out.println("onDicePlayerOrder ");

        }

        @Override
        public void onDiceHash(int playerid) {
            System.out.println("onDiceHash " + playerid);

        }

        @Override
        public void onDiceNumber(int playerid) {
            System.out.println("onDiceNumber " + playerid);

        }

        @Override
        public void onDiceCardShuffle() {
            System.out.println("onDiceCardShuffle ");

        }

        @Override
        public void onLobbyComplete(List<IPlayer> players, List<Object> cards, Object board) {
            System.out.println("onLobbyComplete: ");
            System.out.println("\tplayers: " + players.toString());
            System.out.println("\tcards: " + cards.toString());
            System.out.println("\tboard: " + board.toString());
        }

        @Override
        public void onFailure(Throwable e) {
            System.out.println("onFailure: " + e.getMessage());

            e.printStackTrace();
        }
    };
}
