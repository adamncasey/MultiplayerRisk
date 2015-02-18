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

    @Test
    public void attack1v1(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(1);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void even1v1(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defend1v1(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attack1v2(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(2);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(1);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defend1v2(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(5);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attack2v1(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        attack.add(2);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(1);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defend2v1(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(2);
        attack.add(3);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attackAttack2v2(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        attack.add(2);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(1);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(2);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attackDefend2v2(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        attack.add(1);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(2);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(1);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defendAttack2v2(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        attack.add(2);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        defend.add(1);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(1);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defendDefend2v2(){
        ArrayList<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        attack.add(1);
        ArrayList<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        defend.add(2);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(2);
        expected.add(0);
        ArrayList<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }
}
