package player;

import ai.*;
import logic.*;
import player.*;

import java.util.*;
import java.io.*;

/**
 * CommandLineController --- Allows the user to control a player from the command line.
 */
public class CommandLineController implements PlayerController {
    private static Random random = new Random();

    private int uid;
    private ArrayList<Card> hand;
    private Board board;

    private Scanner reader;
    private PrintWriter writer;

    private PlayerController testingAI = new DumbAI(); // Will fill in the blanks when I want to test a particular move stage.
    boolean testing = false;
    int testingStage = -1;

    public CommandLineController(Scanner reader, PrintWriter writer){
        this.reader = reader;
        this.writer = writer;
    }

    public void setUID(int uid){
        this.uid = uid;
        if(testing){
            testingAI.setUID(uid);
        }
    }

    public void updateAI(ArrayList<Card> hand, Board board, int currentPlayer, Move previousMove){
        this.hand = new ArrayList<Card>(hand);
        this.board = board;
        if(testing){
            testingAI.updateAI(hand, board, currentPlayer, previousMove);
        }
    }

    public Move getMove(Move move){
        try{
            if(testing && move.getStage() != testingStage){
                return testingAI.getMove(move);
            }
            switch(move.getStage()){
                case 0:
                    return claimTerritory(move);
                case 1:
                    return reinforceTerritory(move);
                case 2:
                    return tradeInCards(move);
                case 3:
                    return placeArmies(move);
                case 4:
                    return decideAttack(move);
                case 5:
                    return startAttack(move);
                case 6:
                    return chooseAttackingDice(move);
                case 7:
                    return chooseDefendingDice(move);
                case 8:
                    return occupyTerritory(move);
                case 9:
                    return decideFortify(move);
                case 10:
                    return startFortify(move);
                case 11:
                    return chooseFortifyArmies(move);
                default:
                     return move;
            }
        }catch(WrongMoveException e){
            System.out.println("CommandLineController is not choosing a move correctly");
            return new Move(-1);
        }
    }

    private Move claimTerritory(Move move) throws WrongMoveException{
        writer.println("Which territory do you want to claim?");
        board.printBoard(writer);
        writer.print("> ");
        int territory = chooseUnclaimedTerritory();
        move.setTerritoryToClaim(territory);
        return move;
    }

    private Move reinforceTerritory(Move move) throws WrongMoveException{
        writer.println("Which territory do you want to reinforce?");
        board.printBoard(writer);
        writer.print("> ");
        int territory = chooseAllyTerritory();
        move.setTerritoryToReinforce(territory);
        return move;
    }

    private Move tradeInCards(Move move) throws WrongMoveException{ 
        boolean tradingInCards = false;
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

        ArrayList<Card> toTradeIn = new ArrayList<Card>(); 
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
        return move;
    }

    private ArrayList<Card> pickCards(){
        boolean correct = false;
        ArrayList<Integer> picked = new ArrayList<Integer>();
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
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        for(Integer i : picked){
            toTradeIn.add(hand.get(i-1));
        }
        return toTradeIn;
    }

    private Move placeArmies(Move move) throws WrongMoveException{
        int armiesToPlace = move.getArmiesToPlace();
        writer.format("You have %d armies to place.\n", armiesToPlace);
        writer.println("In which territory would you like to place some armies?");
        board.printBoard(writer);
        writer.print("> ");
        int territory = chooseAllyTerritory();
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

        move.setPlaceArmiesTerritory(territory);
        move.setPlaceArmiesNum(numArmies);
        return move;
    }

    private Move decideAttack(Move move) throws WrongMoveException{
        writer.println("Do you want to attack? (y or n)");
        board.printBoard(writer);
        writer.print("> ");
        boolean attack = chooseYesNo();
        move.setDecideAttack(attack);
        return move;
    }

    private Move startAttack(Move move) throws WrongMoveException{
        writer.println("Choose the territory to attack from.");
        board.printBoard(writer);
        writer.print("> ");
        int ally = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            ally = chooseAllyTerritory();
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
        ArrayList<Integer> adjacents = board.getLinks(ally);
        int enemy = -1; correct = false;
        while(!correct){
            writer.flush();
            enemy = chooseEnemyTerritory();
            for(Integer i : adjacents){
                if(enemy == i){
                    correct = true;
                }
            }
            if(!correct){
                writer.format("%s is not adjacent to %s.\n> ", board.getName(enemy), board.getName(ally));
            }
        }

        move.setAttackFrom(ally);
        move.setAttackTo(enemy);
        return move;
    }

    private Move chooseAttackingDice(Move move) throws WrongMoveException{
        int numArmies = board.getArmies(move.getAttackingFrom());
        String attackingName = board.getName(move.getAttackingFrom());
        String defendingName = board.getName(move.getAttackingTo());
        int defendingArmies = board.getArmies(move.getAttackingTo());
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

        move.setAttackingDice(numDice);
        return move;
    }

    private Move chooseDefendingDice(Move move) throws WrongMoveException{
        int numArmies = board.getArmies(move.getDefendingFrom());
        String defendingName = board.getName(move.getDefendingFrom());
        String attackingName = board.getName(move.getDefendingTo());
        int attackingArmies = board.getArmies(move.getDefendingTo());
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

        move.setDefendingDice(numDice);
        return move;
    }

    private Move occupyTerritory(Move move) throws WrongMoveException{
        int currentArmies = move.getOccupyCurrentArmies();
        int numDice = move.getOccupyDice();
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
        move.setOccupyArmies(numArmies);
        return move;
    }

    private Move decideFortify(Move move) throws WrongMoveException{
        writer.println("Do you want to fortify? (y or n)");
        board.printBoard(writer);
        writer.print("> ");
        boolean fortifying = chooseYesNo();
        move.setDecideFortify(fortifying);
        return move;
    }

    private Move startFortify(Move move) throws WrongMoveException{
        writer.println("Choose the territory to fortify from.");
        board.printBoard(writer);
        writer.print("> ");
        int ally = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            ally = chooseAllyTerritory();
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
        ArrayList<Integer> adjacents = board.getLinks(ally);
        int fortify = -1; correct = false;
        while(!correct){
            writer.flush();
            fortify = chooseAllyTerritory();
            for(Integer i : adjacents){
                if(fortify == i){
                    correct = true;
                }
            }
            if(!correct){
                writer.format("%s is not adjacent to %s.\n> ", board.getName(fortify), board.getName(ally));
            }
        }

        move.setFortifyFrom(ally);
        move.setFortifyTo(fortify);
        return move;
    }

    private Move chooseFortifyArmies(Move move) throws WrongMoveException{
        int currentArmies = move.getFortifyCurrentArmies();
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
        move.setFortifyArmies(numArmies);
        return move;
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

    private int chooseAllyTerritory(){
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

    private int chooseEnemyTerritory(){
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

