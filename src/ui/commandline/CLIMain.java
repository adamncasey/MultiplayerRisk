package ui.commandline;

import ai.AgentFactory;
import ai.AgentTypes;
import ai.agents.Agent;
import lobby.LocalGameLobby;
import lobby.RemoteGameLobby;
import lobby.handler.HostLobbyEventHandler;
import lobby.handler.JoinLobbyEventHandler;
import logic.Game;
import networking.LobbyClient;
import networking.LocalPlayerHandler;
import player.IPlayer;
import settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adam on 31/01/2015.
 */
public class CLIMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("'host' or 'join' a lobby");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        LocalGameLobby lobby;

        String value = reader.readLine();
        if(value.equals("host")) {
            lobby = hostLobby();

            value = reader.readLine();
            if(value.equals("start")) {
                System.out.println("Start game requested");
                lobby.startGame();
            }
            lobby.join();

        } else if(value.equals("join")) {
            joinLobby();
        }

        playGame();
    }

    static List<IPlayer> playersBefore = null;
    static List<IPlayer> playersAfter = null;

    public static void setPlayers(List<IPlayer> before, List<IPlayer> after) {
        playersBefore = before;
        playersAfter = after;
    }

    private static void playGame() {
        if(playersBefore == null || playersAfter == null) {
            System.out.println("Error setting up game.");
            return;
        }
        System.out.println("Starting game");
        Agent agent = AgentFactory.buildAgent(AgentTypes.Type.GREEDY);
        IPlayer localPlayer = new CommandLinePlayer(agent, new Scanner(System.in), new PrintWriter(System.out));

        List<IPlayer> players = new LinkedList<>();
        players.addAll(playersBefore);
        players.add(localPlayer);
        players.addAll(playersAfter);

        List<String> names = namePlayers(playersBefore, playersAfter);

        Game game = new Game(players, names, new LocalPlayerHandler());

        System.out.println("Players: ");
        for(String name : names) {
            System.out.println(name);
        }

        game.run();
    }

    private static List<String> namePlayers(List<IPlayer> playersBefore, List<IPlayer> playersAfter) {
        int i=0;
        List<String> names = new LinkedList<>();

        for(;i<playersBefore.size(); i++) {
            names.add("NetworkPlayer " + i);
        }
        names.add("Local Player");

        for(int j=0;j<playersAfter.size(); j++, i++) {
            names.add("NetworkPlayer " + i);
        }

        return names;
    }

    public static LocalGameLobby hostLobby() {
        LocalGameLobby lobby = new LocalGameLobby(handler, Settings.port);
        lobby.start();
        return lobby;
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
        public void onInitialiseGame(double protocolVersion, String[] extendedFeatures) {

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
        public void onLobbyComplete(List<IPlayer> playersBefore, List<IPlayer> playersAfter, List<Object> cards) {
            System.out.println("onLobbyComplete: ");
            System.out.println("\tplayers: " + playersBefore.toString());
            System.out.println("\tplayers: " + playersAfter.toString());

            System.out.println("At this point, we should pass this data off to the Game Loop");

            setPlayers(playersBefore, playersAfter);
        }

        @Override
        public void onFailure(Throwable e) {
            System.out.println("onFailure: " + e.getMessage());

            e.printStackTrace();
        }
    };

    public static void joinLobby() {
        try {
            RemoteGameLobby lobby = new RemoteGameLobby(InetAddress.getByName("127.0.0.1"), Settings.port, joinHandler);

            lobby.start();

            lobby.join();
        } catch(UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch(InterruptedException e) {
            System.out.printf("Interrupted Exception: " + e.getMessage());
        }
    }

    public static JoinLobbyEventHandler joinHandler = new JoinLobbyEventHandler() {

        @Override
        public void onTCPConnect() {
            System.out.println("onTCPConnect ");
        }

        @Override
        public void onJoinAccepted(int playerid) {
            System.out.println("onJoinAccepted " + playerid);
        }

        @Override
        public void onJoinRejected(String message) {
            System.out.println("onJoinRejected " + message);
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
        public void onInitialiseGame(double protocolVersion, String[] extendedFeatures) {
            System.out.println("onInitialiseGame " + protocolVersion + ". " + extendedFeatures.toString());
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
        public void onLobbyComplete(List<IPlayer> playersBefore, List<IPlayer> playersAfter, List<Object> cards) {
            System.out.println("onLobbyComplete: ");
            System.out.println("\tplayers: " + playersBefore.toString());
            System.out.println("\tplayers: " + playersAfter.toString());

            System.out.println("At this point, we should pass this data off to the Game Loop");

            setPlayers(playersBefore, playersAfter);
        }

        @Override
        public void onFailure(Throwable e) {
            System.out.println("onFailure: " + e.getMessage());

            e.printStackTrace();
        }
    };
}
