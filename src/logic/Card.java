package logic;

import java.util.Random;

/**
 * Card --- Stores information about one of the RISK CARDS
 */
public class Card {

   /**
    * The deck of cards.
    * cards[n][0] = the continent number, 0 = Wildcard
    * cards[n][1] = infantry, cavalry or artillery - 1, 5, 10
    * cards[n][2] = 0 or 1 - 0 = card still in deck - 1 = card in play
    */
//    private static int[44][3] cards;

    // Placeholder to draw a random card.
    private static Random random = new Random();

   /**
    * Fill the cards array with one card for each territory and two wildcards.
    */
    public static void initializeCards(){
        for(int i = 0; i != 42; ++i){
//            cards[i][0] = i + 1;
            int x = i % 3;
            if(x == 0){
//                cards[i][1] = 1;
            } else if(x == 1){
//                cards[i][1] = 5;
            } else if(x == 2){
//                cards[i][1] = 10;
            }
        }
//        cards[42][0] = 0;
//        cards[42][1] = 0;
//        cards[43][0] = 0;
//        cards[43][1] = 0;
        for(int i = 0; i != 44; ++i){
//            cards[i][2] = 0;
        }
    }

   /**
    * Draw a random card from the deck.
    */
    public static Card drawCard(){
        // How will cards be handled over the network?
        int randInt = random.nextInt(44);
//        while(cards[randInt][2] == 1){
//            randInt = random.nextInt(44);
//        }
        Card card = new Card();
//        card.territory = cards[randInt][0];
//        card.armyValue = cards[randInt][1];
//        cards[randInt][2] = 1;
        return card;
    }

    public int territory;
    public int armyValue;

    public Card(){
        territory = -1;
        armyValue = -1;
    } 
}

