package ui.commandline;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

import ai.agents.Agent;
import ai.agents.RandomAgent;
import logic.Card;
import logic.move.Move;
import logic.move.Move.Stage;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;

/**
 * CommandLineController --- Allows the user to control a player from the command line.
 */
public class CommandLineController implements PlayerController {
    private static Random random = new Random();

    private Scanner reader;
    private PrintWriter writer;

    private Player player;
    private Board board;

    private Agent testingAI; // Will fill in the blanks when I want to test a particular move stage.
    boolean testing;
    Stage testingStage;

    public CommandLineController(Scanner reader, PrintWriter writer){
        this.reader = reader;
        this.writer = writer;
        this.testing = false;
        this.testingStage = null;
    }

    public void setup(Player player, Board board){
        this.player = player;
        this.board = board;
        if(testing){
            this.testingAI = new RandomAgent();
            this.testingAI.setup(player, board);
        }
    }

    public void getMove(Move move){
        if(testing && move.getStage() != testingStage){
            testingAI.getMove(move);
            return;
        }
        switch(move.getStage()){
            case CLAIM_TERRITORY:
                claimTerritory(move);
                return;
            case REINFORCE_TERRITORY:
                reinforceTerritory(move);
                return;
            case TRADE_IN_CARDS:
                tradeInCards(move);
                return;
            case PLACE_ARMIES:
                placeArmies(move);
                return;
            case DECIDE_ATTACK:
                decideAttack(move);
                return;
            case START_ATTACK:
                startAttack(move);
                return;
            case CHOOSE_ATTACK_DICE:
                chooseAttackingDice(move);
                return;
            case CHOOSE_DEFEND_DICE:
                chooseDefendingDice(move);
                return;
            case OCCUPY_TERRITORY:
                occupyTerritory(move);
                return;
            case DECIDE_FORTIFY:
                decideFortify(move);
                return;
            case START_FORTIFY:
                startFortify(move);
                return;
            case FORTIFY_TERRITORY:
                chooseFortifyArmies(move);
                return;
            default:
                return;
        }
    }

    private void claimTerritory(Move move){
        writer.println("Which territory do you want to claim?");
        board.printBoard(writer);
        writer.print("> ");
        int territory = chooseUnclaimedTerritory();
        move.setTerritory(territory);
    }

    private void reinforceTerritory(Move move){
        int uid = move.getUID();
        writer.println("Which territory do you want to reinforce?");
        board.printBoard(writer);
        writer.print("> ");
        int territory = chooseAllyTerritory(uid);
        move.setTerritory(territory);
    }

    private void tradeInCards(Move move){
        boolean tradingInCards = false;
        List<Card> hand = player.getHand();
        if(Card.containsSet(hand)){
            if(hand.size() >= 5){
                writer.println("You must trade in cards.");
                tradingInCards = true;
            }else{
                writer.println("Do you want to trade in cards? (y or n)");
                Card.printHand(writer, hand);
                writer.print("> ");
                writer.flush();
                tradingInCards = chooseYesNo();
            }
        } 

        List<Card> toTradeIn = new ArrayList<Card>(); 
        if(tradingInCards){
            writer.println("Which cards do you want to trade in? Enter 3 cards.");
            Card.printHand(writer, hand);
            writer.print("> ");
            writer.flush();
            boolean correct = false;
            while(!correct){
                toTradeIn = pickCards();
                if(Card.containsSet(toTradeIn)){
                    correct = true;
                }else{
                    writer.println("Invalid selection, try again.");
                    Card.printHand(writer, hand);
                    writer.print("> ");
                    writer.flush();
                }
            }
        }

        move.setToTradeIn(toTradeIn);
    }

    private List<Card> pickCards(){
        boolean correct = false;
        List<Card> hand = player.getHand();
        List<Integer> picked = new ArrayList<Integer>();
        while(!correct){
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            int cardIndex = reader.nextInt();
            if(cardIndex > 0 && cardIndex <= hand.size()){
                boolean found = false;
                for(Integer i : picked){
                    if(i == cardIndex){
                        found = true;
                    }
                }
                if(!found){
                    picked.add(cardIndex);
                    writer.print("> ");
                    writer.flush();
                }else{
                    writer.print("You have already picked that card\n> ");
                    writer.flush();
                }
            }else{
                writer.print("Invalid card index\n> ");
                writer.flush();
            }
            if(picked.size() == 3){
                correct = true;
            }
        }
        List<Card> toTradeIn = new ArrayList<Card>();
        for(Integer i : picked){
            toTradeIn.add(hand.get(i-1));
        }
        return toTradeIn;
    }

    private void placeArmies(Move move){
        int uid = move.getUID();
        int armiesToPlace = move.getCurrentArmies();
        int extraArmies = move.getExtraArmies();
        List<Integer> matches = move.getMatches();
        writer.format("You have %d armies to place.\n", armiesToPlace + extraArmies);
        if(matches.size() == 1){
            writer.format("This turn, you must place %d armies in %s.\n", extraArmies, board.getName(matches.get(0)));
        }else if(matches.size() > 1){
            String matchesString = String.format("This turn, you must placed %d armies in ", extraArmies);
            for(int i = 0; i != matches.size()-1; ++i){
                matchesString += String.format("%s, ", board.getName(matches.get(i)));
            }
            matchesString += String.format("or %s.\n", board.getName(matches.get(matches.size()-1)));
            writer.print(matchesString);
        }
        writer.println("In which territory would you like to place some armies?");
        board.printBoard(writer);
        writer.print("> ");
        int territory = chooseAllyTerritory(uid);
        writer.format("How many armies would you like to place in %s?\n", board.getName(territory));
        board.printBoard(writer);
        writer.print("> ");
        int numArmies = 0; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            numArmies = reader.nextInt();
            if(numArmies >= 1){
                if(numArmies <= armiesToPlace){
                    correct = true;
                }else{
                    writer.print("You can't place that many armies.\n> ");
                }
            }else{
                writer.print("You must place at least 1 army.\n> ");
            }
        }

        move.setTerritory(territory);
        move.setArmies(numArmies);
    }

    private void decideAttack(Move move){
        writer.println("Do you want to attack? (y or n)");
        board.printBoard(writer);
        writer.print("> ");
        boolean attack = chooseYesNo();
        move.setDecision(attack);
    }

    private void startAttack(Move move){
        int uid = move.getUID();
        writer.println("Choose the territory to attack from.");
        board.printBoard(writer);
        writer.print("> ");
        int ally = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            ally = chooseAllyTerritory(uid);
            if(board.getArmies(ally) >= 2){
                boolean found = false;
                for(Integer i : board.getLinks(ally)){
                    if(board.getOwner(i) != uid){
                        found = true;
                    }
                }
                if(found){
                    correct = true;
                }else{
                    writer.format("There are no enemies adjacent to %s.\n> ", board.getName(ally));
                }
            }else{
                writer.format("%s does not have enough armies to attack.\n> ", board.getName(ally));
            }
        }
        writer.println("Choose the territory to attack.");
        board.printBoard(writer);
        writer.print("> ");
        List<Integer> adjacents = board.getLinks(ally);
        int enemy = -1; correct = false;
        while(!correct){
            writer.flush();
            enemy = chooseEnemyTerritory(uid);
            for(Integer i : adjacents){
                if(enemy == i){
                    correct = true;
                }
            }
            if(!correct){
                writer.format("%s is not adjacent to %s.\n> ", board.getName(enemy), board.getName(ally));
            }
        }

        move.setFrom(ally);
        move.setTo(enemy);
    }

    private void chooseAttackingDice(Move move){
        int numArmies = board.getArmies(move.getFrom());
        String attackingName = board.getName(move.getFrom());
        String defendingName = board.getName(move.getTo());
        int defendingArmies = board.getArmies(move.getTo());
        writer.format("Choose how many dice to roll. You are attacking from %s which has %d armies, to %s which has %d armies.\n> ", attackingName, numArmies, defendingName, defendingArmies);
        int numDice = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            numDice = reader.nextInt();
            if(numDice >= 1 && numDice <= 3){
                if(numArmies > numDice){
                    correct = true;
                }else{
                    writer.format("You can't roll that many dice when attacking from %s.\n> ", attackingName);
                }
            }else{
                writer.print("Invalid number of dice.\n> ");
            }
        }

        move.setAttackDice(numDice);
    }

    private void chooseDefendingDice(Move move){
        int numArmies = board.getArmies(move.getTo());
        String defendingName = board.getName(move.getTo());
        String attackingName = board.getName(move.getFrom());
        int attackingArmies = board.getArmies(move.getFrom());
        writer.format("Choose how many dice to roll. You are defending %s which has %d armies, from an attack from %s which has %d armies.\n> ", defendingName, numArmies, attackingName, attackingArmies);
        int numDice = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            numDice = reader.nextInt();
            if(numDice >= 1 && numDice <= 2){
                if(numArmies >= numDice){
                    correct = true;
                }else{
                    writer.format("You can't roll that many dice when defending %s.\n> ", defendingName);
                }
            }else{
                writer.print("Invalid number of dice.\n> ");
            }
        }

        move.setDefendDice(numDice);
    }

    private void occupyTerritory(Move move){
        int currentArmies = move.getCurrentArmies();
        int numDice = move.getAttackDice();
        writer.format("Choose how many armies to occupy with. The attacking territory has %d armies remaining.\n> ", currentArmies);
        int numArmies = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            numArmies = reader.nextInt();
            if(numArmies >= numDice){
                if((currentArmies - numArmies) >= 1){
                    correct = true;
                }else{
                    writer.print("You must leave at least 1 army behind.\n> ");
                }
            }else{
                writer.format("You must occupy with at least %d armies.\n> ", numDice);
            }
        }
        move.setArmies(numArmies);
    }

    private void decideFortify(Move move){
        writer.println("Do you want to fortify? (y or n)");
        board.printBoard(writer);
        writer.print("> ");
        boolean fortifying = chooseYesNo();
        move.setDecision(fortifying);
    }

    private void startFortify(Move move){
        int uid = move.getUID();
        writer.println("Choose the territory to fortify from.");
        board.printBoard(writer);
        writer.print("> ");
        int ally = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            ally = chooseAllyTerritory(uid);
            if(board.getArmies(ally) >= 2){
                boolean found = false;
                for(Integer i : board.getLinks(ally)){
                    if(board.getOwner(i) == uid){
                        found = true;
                    }
                }
                if(found){
                    correct = true;
                }else{
                    writer.format("There are no allies adjacent to %s.\n> ", board.getName(ally));
                }
            }else{
                writer.format("%s does not have enough armies to fortify.\n> ", board.getName(ally));;
            }
        }
        writer.println("Choose the territory to fortify.");
        board.printBoard(writer);
        writer.print("> ");
        List<Integer> adjacents = board.getLinks(ally);
        int fortify = -1; correct = false;
        while(!correct){
            writer.flush();
            fortify = chooseAllyTerritory(uid);
            for(Integer i : adjacents){
                if(fortify == i){
                    correct = true;
                }
            }
            if(!correct){
                writer.format("%s is not adjacent to %s.\n> ", board.getName(fortify), board.getName(ally));
            }
        }

        move.setFrom(ally);
        move.setTo(fortify);
    }

    private void chooseFortifyArmies(Move move){
        int currentArmies = move.getCurrentArmies();
        writer.format("Choose how many armies to fortify with. The fortifying territory has %d armies remaining.\n> ", currentArmies);
        int numArmies = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            numArmies = reader.nextInt();
            if(numArmies >= 1){
                if((currentArmies - numArmies) >= 1){
                    correct = true;
                }else{
                    writer.print("You must leave at least 1 army behind.\n> ");
                }
            }else{
                writer.print("You must fortify with at least 1 army.\n> ");
            }
        }
        move.setArmies(numArmies);
    }

    private boolean chooseYesNo(){
        boolean decision = false;
        String answer = ""; boolean correct = false;
        while(!correct){
            writer.flush();
            answer = reader.next();
            answer.toLowerCase();
            if(answer.equals("y") || answer.equals("yes")){
                decision = true;
                correct = true;
            }else if(answer.equals("n") || answer.equals("no")){
                correct = true;
            }else{
                writer.print("Invalid input\n> ");
                writer.flush();
            }         
        } 
        return decision;

    }

    private int chooseUnclaimedTerritory(){
        int territory = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            territory = reader.nextInt();
            if(territory >= 0 && territory < board.getNumTerritories()){
                if(board.getOwner(territory) == -1){
                    correct = true;
                }else{
                    writer.print("That territory has already been claimed.\n> ");
                }
            }else{
                writer.print("That territory doesn't exist.\n> ");
            }
        }
        return territory;
    }

    private int chooseAllyTerritory(int uid){
        int territory = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            territory = reader.nextInt();
            if(territory >= 0 && territory < board.getNumTerritories()){
                if(board.getOwner(territory) == uid){
                    correct = true;
                }else{
                    writer.print("You do not own that territory.\n> ");
                }
            }else{
                writer.print("That territory doesn't exist.\n> ");
            }
        }
        return territory;
    }

    private int chooseEnemyTerritory(int uid){
        int territory = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            territory = reader.nextInt();
            if(territory >= 0 && territory < board.getNumTerritories()){
                if(board.getOwner(territory) != uid){
                    correct = true;
                }else{
                    writer.print("You own that territory.\n> ");
                }
            }else{
                writer.print("That territory doesn't exist.\n> ");
            }
        }
        return territory;
    }
}
