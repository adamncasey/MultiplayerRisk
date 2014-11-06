package main;

import java.util.ArrayList;
import java.util.Scanner;

import logic.Game;
import logic.GameState;
import player.IPlayer;
import player.SimplePlayer;

/**
 * RunGame --- A simple main that tests Game / GameState / IPlayer 
 * @author Nathan Blades
 */
public class RunGame {

    public static void main(String[] args){
        System.out.println("Creating the game board");
        GameState state = new GameState();

        System.out.println("Creating player list");
        ArrayList<IPlayer> players = new ArrayList<IPlayer>(); 

        System.out.println("Adding player 1");
        SimplePlayer player1 = new SimplePlayer("Player1", state);
        players.add(player1);

        System.out.println("Adding player 2");
        SimplePlayer player2 = new SimplePlayer("Player2", state);
        players.add(player2);

        System.out.println("Setting up the game");
        Game game = new Game(players, state);
        game.setupGame();

        System.out.println("Running Game - Control transferred to Game");
        game.playGame();
    
        System.out.println("Game ended");
    }
}
