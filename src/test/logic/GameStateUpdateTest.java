package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

import logic.*;
import player.*;

public class GameStateUpdateTest{

    @Test
    public void testSetCounter(){
        GameState game = new GameState(0, 0); 
        int expectedValues[] = {4, 6, 8, 10, 12, 15, 20, 25};
        for(int i : expectedValues){
            assertEquals(i, game.incrementSetCounter());
        }
    }

    // Test check Attack possible
    //
    // Test check Fortify possible
}
