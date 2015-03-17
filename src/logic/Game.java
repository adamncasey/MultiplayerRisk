package logic;

import static logic.Move.Stage.*;

import java.util.List;
import java.util.ArrayList;

import player.IPlayer;
import settings.Settings;

/**
 * Game --- The main game loop that lets each player take their turn, updating every player whenever anything happens.
 */
public class Game {

    private List<IPlayer> playerInterfaces;
    private int numPlayers = 0;

    private GameState state;
    private MoveChecker checker;

    public Game(List<IPlayer> playerInterfaces, int seed){
        this.playerInterfaces = new ArrayList<IPlayer>(playerInterfaces);
        this.numPlayers = playerInterfaces.size();

        this.state = new GameState(numPlayers, seed);
        this.checker = new MoveChecker(state);

        for(int i = 0; i != this.numPlayers; ++i){
            this.playerInterfaces.get(i).setup(state.getPlayer(i), state.getBoard(), this.checker);
        }
    }

    public void setupGame() throws WrongMoveException{
        if(numPlayers < Settings.MinNumberOfPlayers || numPlayers > Settings.MaxNumberOfPlayers){
            return;
        }
        Move setupMove = new Move(0, SETUP_BEGIN);
        setupMove.setPlayer(numPlayers);
        updatePlayers(setupMove);

        int setupValues[] = {35, 30, 25, 20};
        int armiesToPlace = numPlayers * setupValues[numPlayers-3];
        int territoriesToClaim = state.getBoard().getNumTerritories();

        int currentPlayer = 0;
        while(armiesToPlace > 0){
            Move move;
            int territory;
            if(territoriesToClaim > 0){
                move = new Move(currentPlayer, CLAIM_TERRITORY);
            }else{
                move = new Move(currentPlayer, REINFORCE_TERRITORY);
            }
            getMove(move);
            territory = move.getTerritory();
            if(territoriesToClaim > 0){
                state.getBoard().claimTerritory(territory, currentPlayer);
                territoriesToClaim--;
            }
            state.getBoard().placeArmies(territory, 1);
            updatePlayers(move);
            armiesToPlace--;
            currentPlayer = ++currentPlayer % numPlayers;
        }
        updatePlayers(new Move(-1, SETUP_END));
    }

    public void playGame() throws WrongMoveException{
        if(numPlayers < Settings.MinNumberOfPlayers || numPlayers > Settings.MaxNumberOfPlayers){
            return;
        }
        updatePlayers(new Move(-1, GAME_BEGIN));

        int turnCounter = 0;
        int winner = 0;

        int currentPlayer = 0;
        while(state.getActivePlayerCount() != 1){
            if(isActive(currentPlayer)){
                playerTurn(currentPlayer);
                turnCounter++;
            }
            currentPlayer = ++currentPlayer % numPlayers;
        }
        winner = --currentPlayer;
        if(winner == -1){
            winner += numPlayers;
        }

        Move gameEnded = new Move(-1, GAME_END);
        gameEnded.setTurns(turnCounter);
        gameEnded.setPlayer(winner);
        updatePlayers(gameEnded);
    }

    private void playerTurn(int uid) throws WrongMoveException{
        Move move = new Move(uid, TRADE_IN_CARDS);
        getMove(move);
        List<Card> toTradeIn = move.getToTradeIn();
        boolean traded = state.tradeInCards(uid, toTradeIn); 
        updatePlayers(move);

        int armies = state.calculatePlayerArmies(uid, traded, toTradeIn);
        while(armies != 0){
            move = new Move(uid, PLACE_ARMIES);
            move.setCurrentArmies(armies);
            getMove(move);
            state.getBoard().placeArmies(move.getTerritory(), move.getArmies());
            armies -= move.getArmies();
            updatePlayers(move);
        }

        boolean territoryCaptured = false;
        while(state.checkAttackPossible(uid)){
            move = new Move(uid, DECIDE_ATTACK);
            getMove(move);
            updatePlayers(move);
            boolean attacking = move.getDecision();
            if(!attacking){
                break;
            }

            move = new Move(uid, START_ATTACK);
            getMove(move);
            int attackFrom = move.getFrom();
            int attackTo = move.getTo();
            updatePlayers(move);

            move = new Move(uid, CHOOSE_ATTACK_DICE);
            move.setFrom(attackFrom);
            move.setTo(attackTo);
            getMove(move);
            int attackingDice = move.getAttackDice();
            updatePlayers(move);

            int enemyUID = state.getBoard().getOwner(attackTo);
            move = new Move(enemyUID, CHOOSE_DEFEND_DICE);
            move.setFrom(attackFrom);
            move.setTo(attackTo);
            getMove(move);
            int defendingDice = move.getDefendDice();
            updatePlayers(move);
 
            List<Integer> attackDiceRolls = state.rollDice(attackingDice);
            List<Integer> defendDiceRolls = state.rollDice(defendingDice);
            List<Integer> attackResult = state.decideAttackResult(attackDiceRolls, defendDiceRolls);
            state.getBoard().placeArmies(attackFrom, -attackResult.get(0));
            state.getBoard().placeArmies(attackTo, -attackResult.get(1));

            move = new Move(uid, END_ATTACK);
            move.setAttackerLosses(attackResult.get(0));
            move.setDefenderLosses(attackResult.get(1));
            move.setAttackDiceRolls(attackDiceRolls);
            move.setDefendDiceRolls(defendDiceRolls);
            updatePlayers(move);

            boolean willCaptureTerritory = state.getBoard().getArmies(attackTo) == 0;
            if(willCaptureTerritory){ 
                territoryCaptured = true;
                move = new Move(uid, OCCUPY_TERRITORY);
                move.setCurrentArmies(state.getBoard().getArmies(attackFrom));
                move.setAttackDice(attackingDice);
                getMove(move);
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
                        getMove(move);
                        toTradeIn = move.getToTradeIn();
                        state.tradeInCards(uid, toTradeIn); 
                        updatePlayers(move);
                    
                        armies = state.incrementSetCounter();
                        while(armies != 0){
                            move = new Move(uid, PLACE_ARMIES);
                            move.setCurrentArmies(armies);
                            getMove(move);
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
            getMove(move);
            updatePlayers(move);

            boolean decideFortify = move.getDecision();
            if(decideFortify){
                move = new Move(uid, START_FORTIFY);
                getMove(move);
                int fortifyFrom = move.getFrom();
                int fortifyTo = move.getTo();
                updatePlayers(move);

                move = new Move(uid, FORTIFY_TERRITORY);
                move.setCurrentArmies(state.getBoard().getArmies(fortifyFrom));
                getMove(move);
                int numFortifyArmies = move.getArmies();
                state.getBoard().placeArmies(fortifyFrom, -numFortifyArmies);
                state.getBoard().placeArmies(fortifyTo, numFortifyArmies);
                updatePlayers(move);
            }
        }
    }

    private void updatePlayers(Move move){
        move.setReadOnly();
        for(IPlayer pi : playerInterfaces){
            pi.updatePlayer(move);
        }
    }

    public void getMove(Move move) throws WrongMoveException{
        for(IPlayer p : playerInterfaces){
            p.nextMove(Move.describeStatus(move));
        }

        IPlayer player = playerInterfaces.get(move.getUID());
        player.getMove(move);
        while(!checker.checkMove(move)){
            player.getMove(move);
        }
        move.setReadOnly();
    }

    public boolean isActive(int uid){
       Player p = state.getPlayer(uid);
       return !p.isEliminated();
    }
}
