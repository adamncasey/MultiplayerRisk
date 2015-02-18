package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;

import logic.*;
import player.*;

public class DecideAttackResultTest{

    private static Game game;

    @BeforeClass
    public static void setupGame(){
        ArrayList<IPlayer> players = new ArrayList<IPlayer>();
        game = new Game(players, 0, "resources/risk_map.json");
    }

//1 2 3
//vs
//1 2

//1 v 1
//1 v 2
//2 v 1
//2 v 2
//3 v 1
//3 v 2
}
