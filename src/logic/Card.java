package logic;

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
}

