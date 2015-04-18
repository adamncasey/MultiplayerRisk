package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import logic.move.Move;
import logic.move.MoveChecker;
import logic.rng.Int256;
import logic.rng.RNG;
import logic.state.GameState;
import logic.state.Deck;
import player.IPlayer;
import networking.LocalPlayerHandler;
import settings.Settings;
import static logic.move.Move.Stage.*;

/**
 * Game --- The main game loop that lets each player take their turn, updating
 * every player whenever anything happens.
 */
public class Game implements Runnable {
	private List<IPlayer> playerInterfaces;
    private Map<Integer, IPlayer> playerInterfacesMap;
	private int numPlayers = 0;

    private Map<Integer, String> names;

	private GameState state;
	private MoveChecker checker;

	public Game(List<IPlayer> playerInterfaces,
			LocalPlayerHandler handler, Deck deck) {
        this.playerInterfaces = new ArrayList<IPlayer>(playerInterfaces);
        this.playerInterfacesMap = new HashMap<Integer, IPlayer>();
        for(int i = 0; i != playerInterfaces.size(); ++i){
            this.playerInterfacesMap.put(playerInterfaces.get(i).getPlayerid(), playerInterfaces.get(i));
        }
		this.numPlayers = playerInterfaces.size();
		
		this.names = new HashMap<Integer, String>();
		for(IPlayer player : playerInterfaces) {
			this.names.put(player.getPlayerid(), player.getPlayerName());
		}

		this.state = new GameState(playerInterfaces, this.names, deck);
		this.checker = new MoveChecker(state);

		for (int i = 0; i != this.numPlayers; ++i) {
			this.playerInterfaces.get(i).setup(state.getPlayer(i),
					this.names, state.getBoard(), this.checker, handler);
		}
	}

	public void run() {
		this.setupGame();
		this.playGame();
	}

	private void setupGame() {
		if (numPlayers < Settings.MinNumberOfPlayers
				|| numPlayers > Settings.MaxNumberOfPlayers) {
			return;
		}
		Move setupMove = new Move(0, SETUP_BEGIN);
		setupMove.setPlayer(numPlayers);
		updatePlayers(setupMove);

		int setupValues[] = { 35, 30, 25, 20 };
		int armiesToPlace = numPlayers * setupValues[numPlayers - 3];
		int territoriesToClaim = state.getBoard().getNumTerritories();

		int currentPlayer = 0;
		while (armiesToPlace > 0) {
            int actualID = playerInterfaces.get(currentPlayer).getPlayerid();
			Move move;
			int territory;
			if (territoriesToClaim > 0) {
				move = new Move(actualID, CLAIM_TERRITORY);
			} else {
				move = new Move(actualID, REINFORCE_TERRITORY);
			}
			getMove(move);
			territory = move.getTerritory();
			if (territoriesToClaim > 0) {
				state.claimTerritory(territory, actualID);
				territoriesToClaim--;
			}
			state.placeArmies(territory, 1);
			updatePlayers(move);
			armiesToPlace--;
			currentPlayer = ++currentPlayer % numPlayers;
		}
		System.out.println();
		updatePlayers(new Move(-1, SETUP_END));
	}

	private void playGame() {
		if (numPlayers < Settings.MinNumberOfPlayers
				|| numPlayers > Settings.MaxNumberOfPlayers) {
			return;
		}
		updatePlayers(new Move(-1, GAME_BEGIN));

		int turnCounter = 0;
		int winner = 0;

		int currentPlayer = 0;
		while (state.getActivePlayerCount() != 1) {
            int actualID = playerInterfaces.get(currentPlayer).getPlayerid();
			if (isActive(actualID)) {
				playerTurn(actualID);
				turnCounter++;
			}
			currentPlayer = ++currentPlayer % numPlayers;
		}
		winner = --currentPlayer;
		if (winner == -1) {
			winner += numPlayers;
		}

		Move gameEnded = new Move(-1, GAME_END);
		gameEnded.setTurns(turnCounter);
		gameEnded.setPlayer(playerInterfaces.get(winner).getPlayerid());
		updatePlayers(gameEnded);
	}

	private void playerTurn(int uid) {
		Move move;
		List<Card> hand = state.getPlayer(uid).getHand();
		List<Card> toTradeIn = new ArrayList<Card>();

		while (hand.size() >= 5) { // Force more TRADE_IN_CARDS if hand is too big
			hand = tradeInCards(uid, toTradeIn);
		}
		// Always do atleast 1 TRADE_IN_CARDS
		hand = tradeInCards(uid, toTradeIn);

		int sets = state.tradeInCards(uid, toTradeIn);
		int tArmies = state.calculateTerritoryArmies(uid);
        int cArmies = state.calculateContinentArmies(uid);
        int setArmies = state.calculateSetArmies(sets);
		List<Integer> matchingCards = state.calculateMatchingCards(uid, toTradeIn);
        int extraArmies = state.calculateMatchingArmies(matchingCards);


        int armies = tArmies + cArmies + setArmies;

        System.out.format("Territory Armies : %d\n", tArmies);
        System.out.format("Continent Armies : %d\n", cArmies);
        System.out.format("Set Armies : %d\n", setArmies);
        System.out.format("Total Armies : %d\n", armies);
        System.out.format("Extra Armies : %d\n", extraArmies);
        System.out.flush();

		while ((armies + extraArmies) != 0) {
			move = new Move(uid, PLACE_ARMIES);
			move.setCurrentArmies(armies);
			move.setExtraArmies(extraArmies);
			move.setMatches(matchingCards);
			getMove(move);
			int newExtraArmies = state.updateExtraArmies(move.getTerritory(),
					move.getArmies(), extraArmies, matchingCards);
			int changeInExtraArmies = extraArmies - newExtraArmies;
			extraArmies = newExtraArmies;
			state.placeArmies(move.getTerritory(), move.getArmies());
			armies -= (move.getArmies() - changeInExtraArmies);
			updatePlayers(move);
		}

		boolean territoryCaptured = false;
		while (true) {
			if (!state.checkAttackPossible(uid)) {
				move = new Move(uid, DECIDE_ATTACK);
				move.setDecision(false);
				updatePlayers(move);
				break;
			}

			move = new Move(uid, DECIDE_ATTACK);
			getMove(move);
			updatePlayers(move);
			if (!move.getDecision()) {
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

			int defendingDice = 1;
			int enemyUID = state.getBoard().getOwner(attackTo);
			checkDisconnect(enemyUID);
			move = new Move(enemyUID, CHOOSE_DEFEND_DICE);
			move.setFrom(attackFrom);
			move.setTo(attackTo);
			getMove(move);
			defendingDice = move.getDefendDice();
			updatePlayers(move);

			List<Integer> attackDiceRolls = performDiceRolls(attackingDice);

			List<Integer> defendDiceRolls = performDiceRolls(defendingDice);

			List<Integer> attackResult = state.decideAttackResult(
					attackDiceRolls, defendDiceRolls);
			state.placeArmies(attackFrom, -attackResult.get(0));
			state.placeArmies(attackTo, -attackResult.get(1));

			move = new Move(uid, END_ATTACK);
			move.setAttackerLosses(attackResult.get(0));
			move.setDefenderLosses(attackResult.get(1));
			move.setAttackDiceRolls(attackDiceRolls);
			move.setDefendDiceRolls(defendDiceRolls);
			move.setFrom(attackFrom);
			move.setTo(attackTo);
			updatePlayers(move);

			boolean willCaptureTerritory = state.getBoard().getArmies(attackTo) == 0;
			if (willCaptureTerritory) {
				territoryCaptured = true;
				move = new Move(uid, OCCUPY_TERRITORY);
				move.setCurrentArmies(state.getBoard().getArmies(attackFrom));
				move.setFrom(attackFrom);
				move.setTo(attackTo);
				move.setAttackDice(attackingDice);
				getMove(move);
				int occupyArmies = move.getArmies();
				state.placeArmies(attackFrom, -occupyArmies);
				state.placeArmies(attackTo, occupyArmies);
				state.claimTerritory(attackTo, uid);
				updatePlayers(move);
			}

			if (state.isEliminated(enemyUID)) {
				if (state.eliminatePlayer(uid, enemyUID)) {
					return;
				}
				move = new Move(uid, PLAYER_ELIMINATED);
				move.setPlayer(enemyUID);
				updatePlayers(move);
			}
		}

		if (territoryCaptured) {
			Card newCard = state.getDeck().drawCard();
			if (newCard != null) {
				state.addCard(uid, newCard);
				move = new Move(uid, CARD_DRAWN);
				move.setCard(newCard);
				updatePlayers(move);
			}
		}

		if (!state.checkFortifyPossible(uid)) {
			move = new Move(uid, DECIDE_FORTIFY);
			move.setDecision(false);
			updatePlayers(move);
		} else {
			move = new Move(uid, DECIDE_FORTIFY);
			getMove(move);
			updatePlayers(move);

			boolean decideFortify = move.getDecision();
			if (decideFortify) {
				move = new Move(uid, START_FORTIFY);
				getMove(move);
				int fortifyFrom = move.getFrom();
				int fortifyTo = move.getTo();
				updatePlayers(move);

				move = new Move(uid, FORTIFY_TERRITORY);
				move.setCurrentArmies(state.getBoard().getArmies(fortifyFrom));
				move.setFrom(fortifyFrom);
				move.setTo(fortifyTo);
				getMove(move);
				int numFortifyArmies = move.getArmies();
				state.placeArmies(fortifyFrom, -numFortifyArmies);
				state.placeArmies(fortifyTo, numFortifyArmies);
				updatePlayers(move);
			}
		}
	}

	private void updatePlayers(Move move) {
		move.setReadOnly();
        for(int i = 0; i != numPlayers; ++i){
            playerInterfaces.get(i).updatePlayer(move);
        }
	}

	public void getMove(Move move){
        if(move.getUID() == -1){
            neutralMove(move);
            move.setReadOnly();
            return;
        }

        for(IPlayer p : playerInterfaces){
            p.nextMove(Move.describeStatus(move), move.getUID()); 
        }

        move.setReadOnlyInputs();
        IPlayer player = playerInterfacesMap.get(move.getUID());
        player.getMove(move);
        while(!checker.checkMove(move)){
            player.getMove(move);
        }
        move.setReadOnly();
    }

	public void neutralMove(Move move) {
		if (move.getStage() == CHOOSE_DEFEND_DICE) {
			int defendingDice = 1;
			if (state.getBoard().getArmies(move.getTo()) > 1) {
				defendingDice = 2;
			}
			move.setDefendDice(defendingDice);
		}
	}

	public boolean isActive(int uid) {
		return !(state.getPlayer(uid).isEliminated() || state.getPlayer(uid)
				.isDisconnected());
	}

	public void checkDisconnect(int uid) {
		if (uid == -1) {
			return;
		}
		if (state.getPlayer(uid).isDisconnected()
				&& !state.getPlayer(uid).isEliminated()) {
			state.disconnectPlayer(uid);
		}
	}

	private List<Card> tradeInCards(int uid, List<Card> toTradeIn) {
		Move move = new Move(uid, TRADE_IN_CARDS);
		getMove(move);
		toTradeIn.addAll(move.getToTradeIn());
		state.tradeInCards(uid, toTradeIn);
		updatePlayers(move);
		return state.getPlayer(uid).getHand();
	}

	public List<Integer> performDiceRolls(int numDice) {
		List<RNG> rngs = new ArrayList<RNG>();
		for (int i = 0; i != numPlayers; ++i) {
			rngs.add(new RNG());
		}

		List<Int256> rollHashes = new ArrayList<Int256>();
		for (int i = 0; i != numPlayers; ++i) {
			if (isActive(i)) {
				Move move = new Move(playerInterfaces.get(i).getPlayerid(), ROLL_HASH);
				move.setRNG(rngs.get(i));
				getMove(move);
				rollHashes.add(Int256.fromString(move.getRollHash()));
				updatePlayers(move);
			} else {
				rollHashes.add(null);
			}
		}

		List<Int256> rollNumbers = new ArrayList<Int256>();
		for (int i = 0; i != numPlayers; ++i) {
			if (isActive(i)) {
				Move move = new Move(playerInterfaces.get(i).getPlayerid(), ROLL_NUMBER);
				move.setRNG(rngs.get(i));
				move.setRollHash(rollHashes.get(i).string);
				getMove(move);
				rollNumbers.add(Int256.fromString(move.getRollNumber()));
				updatePlayers(move);
			}
		}

		return RNG.getDiceRolls(rollNumbers, numDice);
	}
}
