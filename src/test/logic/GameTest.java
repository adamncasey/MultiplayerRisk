package test.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.Game;

public class GameTest {

    @Test(expected=NullPointerException.class)
    public void initializeNullTest(){
        Game game = new Game(null);
    }
}
