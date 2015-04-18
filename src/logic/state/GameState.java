package logic.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import logic.Card;
import logic.move.Move;
import player.IPlayer;

public class GameState {
    private Map<Integer, Player> players;

    private Board board;
    private Deck deck;
    private Map<Integer, String> names;

    private static int setValues[] = {4, 6, 8, 10, 12, 15};
    private int setCounter = 0;
    private int armyReward = setValues[setCounter];

    private int activePlayerCount;

    /**
     * Create a blank GameState
     */
    public GameState(List<IPlayer> playerInterfaces, Map<Integer, String> names, Deck deck) {
        this.players = new HashMap<>();
        this.names = names;

        for(IPlayer player : playerInterfaces) {
            players.put(player.getPlayerid(), new Player(player.getPlayerid()));
        }

        this.activePlayerCount = playerInterfaces.size();
        
        this.board = new Board();
        if(deck == null){
            this.deck = board.getDeck();
        }else{
            this.deck = deck;
        }
        Move.setNames(this.names);
    }

    /**
     * Load in a GameState
     */
    public GameState(boolean testing, int numPlayers, int[] owners, int[] armies){
        this.players = new HashMap<>();

        for(int i = 0; i != numPlayers; ++i){
            this.players.put(i, new Player(i));
        }
        this.activePlayerCount = numPlayers;

        this.board = new Board(testing, owners, armies);
        this.deck = board.getDeck();
    }

    public Board getBoard(){
        return this.board;
    }

    public Deck getDeck(){
        return this.deck;
    }

    public Map<Integer, String> getNames(){
        return Collections.unmodifiableMap(this.names);
    }

    public Player getPlayer(int uid){
        return players.get(uid);
    }

    public int getActivePlayerCount(){
        return activePlayerCount;
    }

    public int tradeInCards(int uid, List<Card> toTradeIn){
        List<Card> hand = players.get(uid).modifyHand();
        for(Card c: toTradeIn){
            hand.remove(c);
        }
        return toTradeIn.size() / 3;
    }

    public void claimTerritory(int tid, int uid){
        board.claimTerritory(tid, uid);
    }

    public void placeArmies(int tid, int numArmies){
        board.placeArmies(tid, numArmies);
    }

    public int calculateTerritoryArmies(int uid){
        return board.calculatePlayerTerritoryArmies(uid);
    }

    public int calculateContinentArmies(int uid){
        return board.calculatePlayerContinentArmies(uid);
    }

    public int calculateSetArmies(int sets){
        int reward = 0;
        for(int i = 0; i != sets; ++i){
            reward += armyReward;
            setCounter++;
            if(setCounter > setValues.length-1){
                armyReward = setValues[setValues.length-1] + (5 * (setCounter - (setValues.length-1)));
            } else {
                armyReward = setValues[setCounter];
            }
        }
        return reward;
    }

    public List<Integer> calculateMatchingCards(int uid, List<Card> toTradeIn){
        List<Integer> matches = new ArrayList<>();
        for(Card card : toTradeIn){
            if(board.getOwner(card.getID()) == uid){
                matches.add(card.getID());
            }
        }
        return matches;
    }

    public int calculateMatchingArmies(List<Integer> matchingCards){
        if(matchingCards.size() > 0){
            return 2;
        }
        return 0;
    }

    public int updateExtraArmies(int territory, int armies, int extraArmies, List<Integer> matches){
        for(int match : matches){
            if(territory == match){
                return Math.max(extraArmies - armies, 0);
            }
        }
        return extraArmies;
    }

    public boolean checkAttackPossible(int uid){
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) == uid && board.getArmies(i) >= 2){
                for(int j : board.getLinks(i)){
                    if(board.getOwner(j) != uid){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Integer> decideAttackResult(List<Integer> attack, List<Integer> defend){
        attack = new ArrayList<>(attack);
        defend = new ArrayList<>(defend);
        int attackerLosses = 0; int defenderLosses = 0;

        while(attack.size() != 0 && defend.size() != 0){
            int attackScore = 0; int defendScore = 0;
            int attackIndex = -1; int defendIndex = -1;
            for(int i = 0; i != attack.size(); ++i){
                if(attack.get(i) > attackScore){
                    attackScore = attack.get(i);
                    attackIndex = i;
                }
            }
            for(int i = 0; i != defend.size(); ++i){
                if(defend.get(i) > defendScore){
                    defendScore = defend.get(i);
                    defendIndex = i;
                }
            }
            if(attackScore > defendScore){
                defenderLosses++;
            } else {
                attackerLosses++;
            }
            attack.remove(attackIndex);
            defend.remove(defendIndex);
        }

        List<Integer> result = new ArrayList<>();
        result.add(attackerLosses);
        result.add(defenderLosses);
        return result;
    }

    public boolean checkFortifyPossible(int uid){
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) == uid && board.getArmies(i) >= 2){
                for(int j : board.getLinks(i)){
                    if(board.getOwner(j) == uid){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isEliminated(int uid){
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) == uid){
                return false;
            }
        }
        return true;
    }

    public boolean eliminatePlayer(int currentUID, int eliminatedUID){
        if(eliminatedUID == -1){
            return false;
        }
        List<Card> eliminatedHand = players.get(eliminatedUID).getHand();
        for(Card c : eliminatedHand){
            players.get(currentUID).addCard((c));
        }
        players.get(eliminatedUID).eliminate();
        activePlayerCount--;
        if(activePlayerCount == 1){
            return true;
        }
        return false;
    }

    public void addCard(int uid, Card newCard){
        Player p = players.get(uid);
        p.addCard(newCard);
    }

    public void disconnectPlayer(int uid){
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) == uid){
                claimTerritory(i, -1);
            }
        }
        players.get(uid).eliminate();
    }
}
