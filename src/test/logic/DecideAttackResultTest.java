package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

import logic.*;
import player.*;

public class DecideAttackResultTest{

    private static Game game;

    @BeforeClass
    public static void setupGame(){
        List<IPlayer> players = new ArrayList<IPlayer>();
        game = new Game(players, 0, 0, "resources/risk_map.json");
    }

    @Test
    public void attack1v1(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(1);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void even1v1(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defend1v1(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attack1v2(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(2);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(1);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defend1v2(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(5);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attack2v1(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        attack.add(2);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(1);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defend2v1(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(2);
        attack.add(3);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(0);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attackAttack2v2(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        attack.add(2);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(1);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(2);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void attackDefend2v2(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(4);
        attack.add(1);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(3);
        defend.add(2);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(1);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defendAttack2v2(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        attack.add(2);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        defend.add(1);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(1);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }

    @Test
    public void defendDefend2v2(){
        List<Integer> attack = new ArrayList<Integer>();
        attack.add(3);
        attack.add(1);
        List<Integer> defend = new ArrayList<Integer>();
        defend.add(4);
        defend.add(2);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(2);
        expected.add(0);
        List<Integer> result = game.decideAttackResult(attack, defend);
        assertEquals(expected, result);
    }
}
