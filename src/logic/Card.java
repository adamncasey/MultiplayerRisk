package logic;

import java.io.*;
import java.util.*;

/**
 * Card --- Stores information about one of the RISK CARDS
 */
public class Card {

    private Integer territory; // the territory number, 0 = wildcard
    private Integer type; // infantry, cavalry or artillery - 1, 5, 10
    private String country;

    public Card(Integer territory, Integer type, String country){
        this.territory = territory;
        this.type = type;
        this.country = country;
    }

    public Integer getID(){
        return this.territory;
    }

    public Integer getType(){
        return this.type;
    }

    public String getName(){
        return this.country;
    }

    public boolean equals(Card c){
        if(territory != c.getID()){
            return false;
        }
        if(type != c.getType()){
            return false;
        }
        if(country != c.getName()){
            return false;
        }
        return true;
    }

    // Checks that the hand contains a set of 3 different cards, 3 of the same type of card, or a set using a wildcard.
    public static boolean containsSet(ArrayList<Card> hand){
        // Count how many times each type of card appears
        int iC = 0; int cC = 0; int aC = 0; int wC = 0;
        for(Card c : hand){
            if(c.getType() == 1){
                iC++; 
            } else if(c.getType() == 5){
                cC++;
            } else if(c.getType() == 10){
                aC++;
            } else if(c.getType() == 0){
                wC++;
            }
        }
        // Check if there are 3 or more of a type of card
        if(iC + wC >= 3 || cC +wC  >= 3 || aC + wC >= 3){
            return true;
        }
        // Check if there are 3 different cards
        if(iC >= 1 && cC >= 1 && aC >= 1){
            return true;
        }
        // Check if there is a set containing a wildcard and 2 different cards
        // the counters are converted to either 0 or 1, so that for example 2 infantry and 1 artillery is not counted as a set, but 1 infantry, 1 artillery and 1 wildcard is
        if(((iC != 0) ? 1 : 0) + ((cC != 0) ? 1 : 0) + ((aC != 0) ? 1 : 0) + wC >= 3){
            return true;
        }
        return false;
    }

    // Check that toTradeIn is a subset of hand
    public static boolean isSubset(ArrayList<Card> toTradeIn, ArrayList<Card> hand){
        toTradeIn = new ArrayList<Card>(toTradeIn);
        hand = new ArrayList<Card>(hand);
        // Loop until either all cards in toTradeIn are found, or a card is not found
        // If a card in toTradeIn is found in hand, remove it from hand
        for(Card c1: toTradeIn){
            Card found = null;
            for(Card c2: hand){
                if(c1.equals(c2)){
                    found = c2;
                    break;
                }
            }
            if(found == null){
                return false;
            }
            hand.remove(found);
        }
        return true;
    }

    public static String printHand(PrintWriter writer, ArrayList<Card> hand){
        String message = "";
        int counter = 1;
        for(Card c : hand){
            String type = "";
            if(c.getType() == 1){
                type = "infantry";
            }else if(c.getType() == 5){
                type = "cavalry";
            }else if(c.getType() == 10){
                type = "artillery";
            }
            message += String.format("%d. [%s-%s]  ", counter, c.getName(), type);
            counter++;
        }
        message += "\n";
        if(writer != null){
            writer.print(message);
            writer.flush();
            return "";
        }else{
            return message;
        }
    }
}

