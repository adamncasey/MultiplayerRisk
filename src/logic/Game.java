package logic;

import logic.Move.Stage;
import static logic.Move.Stage.*;

import java.util.List;
import java.util.ArrayList;

import player.IPlayer;

/**
 * Game --- The main game loop that lets each player take their turn, updating every player whenever anything happens.
 */
public class Game {

    private List<IPlayer> playerInterfaces;

    private MoveChecker checker;

    private int totalPlayerCount = 0;

    private GameState state;

    private static int setupValues[] = {35, 30, 25, 20};

    public Game(List<IPlayer> playerInterfaces, int seed, String boardFilename){
        this.playerInterfaces = new ArrayList<IPlayer>(playerInterfaces);

        this.totalPlayerCount = playerInterfaces.size();

        this.state = new GameState(totalPlayerCount, seed, boardFilename);

        this.checker = new MoveChecker();
        this.checker.update(state);

        for(int i = 0; i != this.totalPlayerCount; ++i){
            this.playerInterfaces.get(i).setup(state.getPlayer(i), state.getBoard());
        }
    }

    private void updatePlayers(Move move){
        move.setReadOnly();
        for(int i = 0; i != totalPlayerCount; ++i){
            IPlayer pi = playerInterfaces.get(i);
            pi.updatePlayer(move);
        }
        checker.update(state);
    }

    public boolean stillPlaying(int uid){
       Player p = state.getPlayer(uid);
       return !p.isEliminated();
    }

    public void setupGame() throws WrongMoveException{
        if(totalPlayerCount < 3 || totalPlayerCount > 6){
            return;
        }
        updatePlayers(new Move(-1, SETUP_BEGIN));
        int initialArmyValue = setupValues[state.getActivePlayerCount()-3];
        int totalArmies = totalPlayerCount * initialArmyValue;
        int territoriesToClaim = state.getBoard().getNumTerritories();

        int currentPlayer = 0;
        while(totalArmies != 0){
            IPlayer player = playerInterfaces.get(currentPlayer);
            if(territoriesToClaim != 0){
                Move move = new Move(currentPlayer, CLAIM_TERRITORY);
                move = getMove(move);
                int territoryToClaim = move.getTerritory();
                state.getBoard().claimTerritory(territoryToClaim, currentPlayer);
                state.getBoard().placeArmies(territoryToClaim, 1);
                territoriesToClaim--;
                updatePlayers(move);
            } else {
                Move move = new Move(currentPlayer, REINFORCE_TERRITORY);
                move = getMove(move);
                int territoryToReinforce = move.getTerritory();
                state.getBoard().placeArmies(territoryToReinforce, 1);
                updatePlayers(move);
            }
            totalArmies--;
            currentPlayer++;
            if(currentPlayer == totalPlayerCount){
                currentPlayer = 0;
            }
        }
        updatePlayers(new Move(-1, SETUP_END));
    }

    public void playGame() throws WrongMoveException{
        if(totalPlayerCount < 3 || totalPlayerCount > 6){
            return;
        }
        int turnCounter = 0;
        int currentPlayer = 0;
        updatePlayers(new Move(-1, GAME_BEGIN));
        while(state.getActivePlayerCount() != 1){
            if(stillPlaying(currentPlayer)){
                playerTurn(currentPlayer);
                turnCounter++;
            }
            currentPlayer++;
            if(currentPlayer == totalPlayerCount){
                currentPlayer = 0;
            }
        }
        Move gameEnded = new Move(-1, GAME_END);
        gameEnded.setTurns(turnCounter);
        gameEnded.setPlayer(currentPlayer);
        updatePlayers(gameEnded);
        return;
    }

    private void playerTurn(int uid) throws WrongMoveException{
        Move move = new Move(uid, TRADE_IN_CARDS);
        move = getMove(move);
        List<Card> toTradeIn = move.getToTradeIn();
        boolean traded = state.tradeInCards(uid, toTradeIn); 
        updatePlayers(move);

        int armies = state.calculatePlayerArmies(uid, traded, toTradeIn);
        while(armies != 0){
            move = new Move(uid, PLACE_ARMIES);
            move.setCurrentArmies(armies);
            move = getMove(move);
            state.getBoard().placeArmies(move.getTerritory(), move.getArmies());
            armies -= move.getArmies();
            updatePlayers(move);
        }

        boolean territoryCaptured = false;
        while(state.checkAttackPossible(uid)){
            move = new Move(uid, DECIDE_ATTACK);
            move = getMove(move);
            updatePlayers(move);
            boolean attacking = move.getDecision();
            if(!attacking){
                break;
            }

            move = new Move(uid, START_ATTACK);
            move = getMove(move);
            int attackFrom = move.getFrom();
            int attackTo = move.getTo();
            updatePlayers(move);

            move = new Move(uid, CHOOSE_ATTACK_DICE);
            move.setFrom(attackFrom);
            move.setTo(attackTo);
            move = getMove(move);
            int attackingDice = move.getAttackDice();
            updatePlayers(move);

            int enemyUID = state.getBoard().getOwner(attackTo);
            move = new Move(enemyUID, CHOOSE_DEFEND_DICE);
            move.setFrom(attackFrom);
            move.setTo(attackTo);
            move = getMove(move);
            int defendingDice = move.getDefendDice();
            updatePlayers(move);
 
            List<Integer> attackDiceRolls = state.rollDice(attackingDice);
            List<Integer> defendDiceRolls = state.rollDice(defendingDice);
            List<Integer> attackResult = state.decideAttackResult(attackDiceRolls, defendDiceRolls);
            state.getBoard().placeArmies(attackFrom, -attackResult.get(0));
            state.getBoard().placeArmies(attackTo, -attackResult.get(1));
            boolean willCaptureTerritory = state.getBoard().getArmies(attackTo) == 0;

            move = new Move(uid, END_ATTACK);
            move.setAttackerLosses(attackResult.get(0));
            move.setDefenderLosses(attackResult.get(1));
            move.setAttackDiceRolls(attackDiceRolls);
            move.setDefendDiceRolls(defendDiceRolls);
            updatePlayers(move);

            if(willCaptureTerritory){ 
                territoryCaptured = true;
                move = new Move(uid, OCCUPY_TERRITORY);
                move.setCurrentArmies(state.getBoard().getArmies(attackFrom));
                move.setAttackDice(attackingDice);
                move = getMove(move);
                int occupyArmies = move.getArmies();
                state.getBoard().placeArmies(attackFrom, -occupyArmies);
                state.getBoard().placeArmies(attackTo, occupyArmies);
                state.getBoard().claimTerritory(attackTo, uid);
                updatePlayers(move);
            }

            if(state.isEliminated(enemyUID)){
                if(state.eliminatePlayer(uid, enemyUID)){
                    return;
                }
                move = new Move(uid, PLAYER_ELIMINATED);
                move.setPlayer(enemyUID);
                updatePlayers(move);
                List<Card> hand = state.getPlayer(uid).getHand();
                if(hand.size() > 5){ // immediately trade in cards when at 6 or more
                    while(hand.size() >= 5){ // trade in cards and place armies until 4 or fewer cards
                        move = new Move(uid, TRADE_IN_CARDS);
                        move = getMove(move);
                        toTradeIn = move.getToTradeIn();
                        state.tradeInCards(uid, toTradeIn); 
                        updatePlayers(move);
                    
                        armies = state.incrementSetCounter();
                        while(armies != 0){
                            move = new Move(uid, PLACE_ARMIES);
                            move.setCurrentArmies(armies);
                            move = getMove(move);
                            state.getBoard().placeArmies(move.getTerritory(), move.getArmies());
                            armies -= move.getArmies();
                            updatePlayers(move);
                        }
                    }
                }
            }
        }

        if(territoryCaptured){
            Card newCard = state.getDeck().drawCard();
            if(newCard != null){
                state.getPlayer(uid).addCard(newCard);
                updatePlayers(new Move(uid, CARD_DRAWN));
            }
        }

        if(state.checkFortifyPossible(uid)){
            move = new Move(uid, DECIDE_FORTIFY);
            move = getMove(move);
            updatePlayers(move);

            boolean decideFortify = move.getDecision();
            if(decideFortify){
                move = new Move(uid, START_FORTIFY);
                move = getMove(move);
                int fortifyFrom = move.getFrom();
                int fortifyTo = move.getTo();
                updatePlayers(move);

                move = new Move(uid, FORTIFY_TERRITORY);
                move.setCurrentArmies(state.getBoard().getArmies(fortifyFrom));
                move = getMove(move);
                int numFortifyArmies = move.getArmies();
                state.getBoard().placeArmies(fortifyFrom, -numFortifyArmies);
                state.getBoard().placeArmies(fortifyTo, numFortifyArmies);
                updatePlayers(move);
            }
        }
    }

    public Move getMove(Move move) throws WrongMoveException{
        int currentPlayer = move.getUID();
        Stage stage = move.getStage();
        for(IPlayer p : playerInterfaces){
            p.nextMove(Move.describeStatus(currentPlayer, stage));
        }

        IPlayer player = playerInterfaces.get(currentPlayer);
        move = player.getMove(move);
        while(!checker.checkMove(stage, move)){
            move = player.getMove(move);
        }
        return move;
    }
}
