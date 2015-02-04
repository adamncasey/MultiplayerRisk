package ui.commandline;

import lobby.LocalGameLobby;
import lobby.RemoteGameLobby;
import lobby.handler.HostLobbyEventHandler;
import lobby.handler.JoinLobbyEventHandler;
import networking.LobbyClient;
import player.IPlayer;
import settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Adam on 31/01/2015.
 */
public class CLIMain {

    public static void main(String[] args) throws IOException {
        System.out.println("'host' or 'join' a lobby");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String value = reader.readLine();
        if(value.equals("host")) {
            hostLobby();
        } else if(value.equals("join")) {
            joinLobby();
        }
    }

    public static void hostLobby() {
        LocalGameLobby lobby = new LocalGameLobby(handler, Settings.port);
        lobby.start();
        try {
            synchronized (lobby) {
                lobby.wait();
            }
        } catch(InterruptedException e) {
            System.out.println("Interrupted whilst waiting");
        }
    }

    public static HostLobbyEventHandler handler = new HostLobbyEventHandler() {
        @Override
        public String onPlayerJoinRequest(LobbyClient client) {
            System.out.println("onPLayerJoinRequest " + client.supportedVersions[0]);
            return null; // Accept the player (rejecting requires a string for reject message)
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

    public static void joinLobby() {
        try {
            RemoteGameLobby lobby = new RemoteGameLobby(InetAddress.getByName("127.0.0.1"), Settings.port);

            lobby.start();

            synchronized (lobby) {
                lobby.wait();
            }
        } catch(UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch(InterruptedException e) {
            System.out.printf("Interrupted Exception: " + e.getMessage());
        }
    }

//    public static JoinLobbyEventHandler joinHandler = new JoinLobbyEventHandler() {
//
//        @Override
//        public void onTCPConnect() {
//            System.out.println("onTCPConnect ");
//        }
//
//        @Override
//        public void onJoinAccepted(int playerid) {
//            System.out.println("onJoinAccepted " + playerid);
//        }
//
//        @Override
//        public void onJoinRejected(String message) {
//            System.out.println("onJoinRejected " + message);
//        }
//
//        @Override
//        public void onPlayerJoin(int playerid) {
//            System.out.println("onPlayerJoin " + playerid);
//        }
//
//        @Override
//        public void onPlayerLeave(int playerid) {
//            System.out.println("onPlayerLeave " + playerid);
//        }
//
//        @Override
//        public void onPingStart() {
//
//            System.out.println("onPingStart ");
//        }
//
//        @Override
//        public void onPingReceive(int playerid) {
//            System.out.println("onPingReceive " + playerid);
//
//        }
//
//        @Override
//        public void onReady() {
//            System.out.println("onReady ");
//
//        }
//
//        @Override
//        public void onReadyAcknowledge(int playerid) {
//            System.out.println("onReadyAcknowledge " + playerid);
//
//        }
//
//        @Override
//        public void onDicePlayerOrder() {
//            System.out.println("onDicePlayerOrder ");
//
//        }
//
//        @Override
//        public void onDiceHash(int playerid) {
//            System.out.println("onDiceHash " + playerid);
//
//        }
//
//        @Override
//        public void onDiceNumber(int playerid) {
//            System.out.println("onDiceNumber " + playerid);
//
//        }
//
//        @Override
//        public void onDiceCardShuffle() {
//            System.out.println("onDiceCardShuffle ");
//
//        }
//
//        @Override
//        public void onLobbyComplete(List<IPlayer> players, List<Object> cards, Object board) {
//            System.out.println("onLobbyComplete: ");
//            System.out.println("\tplayers: " + players.toString());
//            System.out.println("\tcards: " + cards.toString());
//            System.out.println("\tboard: " + board.toString());
//        }
//
//        @Override
//        public void onFailure(Throwable e) {
//            System.out.println("onFailure: " + e.getMessage());
//
//            e.printStackTrace();
//        }
//    };
}
