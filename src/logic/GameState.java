package logic;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GameState {
    private static Random random;

    private List<Player> players;

    private Board board;
    private Deck deck;

    private static int setValues[] = {4, 6, 8, 10, 12, 15};
    private int setCounter = 0;
    private int armyReward = setValues[0];

    private int activePlayerCount;

    public GameState(int numPlayers, int seed, String boardFilename){
        GameState.random = new Random(seed);

        this.players = new ArrayList<Player>();
        for(int i = 0; i != numPlayers; ++i){
            this.players.add(new Player(i));
        }
        this.activePlayerCount = numPlayers;
        
        this.board = new Board(boardFilename);
        this.deck = board.getDeck();
        this.deck.shuffle(seed);
    }


    public Board getBoard(){
        return this.board;
    }

    public Deck getDeck(){
        return this.deck;
    }

    public Player getPlayer(int uid){
        return players.get(uid);
    }

    public int getActivePlayerCount(){
        return activePlayerCount;
    }

    public boolean tradeInCards(int uid, List<Card> toTradeIn){
        List<Card> hand = players.get(uid).getHand();
        for(Card c: toTradeIn){
            hand.remove(c);
        }
        if(toTradeIn.size() == 3){
            return true;
        }
        return false;
    }

    public int calculatePlayerArmies(int uid, boolean traded, List<Card> toTradeIn){
        int armies = 0;

        armies += board.calculatePlayerTerritoryArmies(uid);
        armies += board.calculatePlayerContinentArmies(uid);

        if(traded){
            armies += incrementSetCounter();
        }

        int extraArmies = 0;
        for(Card card : toTradeIn){
            if(board.getOwner(card.getID()) == uid){
                extraArmies = 2;
            }
        }
        armies += extraArmies;

        return armies;
    }

    // returns the number of armies rewarded for trading in the set
    public int incrementSetCounter(){
        int reward = armyReward;
        setCounter++;
        if(setCounter > setValues.length-1){
            armyReward = setValues[setValues.length-1] + (5 * (setCounter - (setValues.length-1)));
        } else {
            armyReward = setValues[setCounter];
        }
        return reward; 
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

    public static List<Integer> rollDice(int numDice){
        List<Integer> diceRolls = new ArrayList<Integer>();
        for(int i = 0; i != numDice; ++i){
            diceRolls.add(random.nextInt(6)+1);
        }
        return diceRolls;
    }

    public static List<Integer> decideAttackResult(List<Integer> attack, List<Integer> defend){
        attack = new ArrayList<Integer>(attack);
        defend = new ArrayList<Integer>(defend);
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

        List<Integer> result = new ArrayList<Integer>();
        result.add(attackerLosses);
        result.add(defenderLosses);
        return result;
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
}
