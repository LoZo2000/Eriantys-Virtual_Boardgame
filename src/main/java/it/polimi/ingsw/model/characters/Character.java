package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.model.rules.Rule;

import java.io.Serializable;

import static org.fusesource.jansi.Ansi.*;

/**
 * The abstract class character is the class inherited by each specific character class, in this class there are the elements
 * common by all classes
 */
public abstract class Character implements Serializable {
    private final int id;
    private final String desc;
    private final String desc_short;
    private final CharacterType typeCharacter;
    private final int cost;
    private int numberTimesUsed;
    private String sprite = null;

    /**
     * This method is the cconstructor of the class
     * @param id is the id of the Character
     * @param type is the type of the Character
     * @param desc is the description of the character
     * @param desc_short is the short version of the description of the character
     * @param cost is the cost to use the power of the character
     */
    public Character(int id, CharacterType type, String desc, String desc_short, int cost){
        this.id = id;
        this.typeCharacter = type;
        this.desc = desc;
        this.desc_short = desc_short;
        this.cost = cost;
        this.numberTimesUsed = 0;
        switch (id){
            case 1 -> sprite = "/Char_1.jpg";
            case 2 -> sprite = "/Char_2.jpg";
            case 3 -> sprite = "/Char_3.jpg";
            case 4 -> sprite = "/Char_4.jpg";
            case 5 -> sprite = "/Char_5.jpg";
            case 6 -> sprite = "/Char_6.jpg";
            case 7 -> sprite = "/Char_7.jpg";
            case 8 -> sprite = "/Char_8.jpg";
            case 9 -> sprite = "/Char_9.jpg";
            case 10 -> sprite = "/Char_10.jpg";
            case 11 -> sprite = "/Char_11.jpg";
            case 12 -> sprite = "/Char_12.jpg";
        }
    }

    /**
     * This method return the id of the character
     * @return an int representing the id of the character
     */
    public int getId(){
        return this.id;
    }

    /**
     * This method return the current cost of the character
     * @return an int representing the current cost of the character
     */
    public int getCost() {
        return cost + numberTimesUsed;
    }

    /**
     * This method return the original cost of the character (not modified by the game)
     * @return an int representing the original cost of the character
     */
    public int getOriginalCost(){
        return cost;
    }

    /**
     * This method is called when the card is used, to increase the attribute representing the number of times the card
     * hasa been used
     */
    protected void increaseTimesUsed(){
        this.numberTimesUsed += 1;
    }

    /**
     * This method return the character type
     * @return an enum representing the character type of the character
     */
    public CharacterType getTypeCharacter(){
        return this.typeCharacter;
    }

    /**
     * This method is called by the game to use the power of the character card
     * @param player is the player that use the character card
     * @return an object Rule containing the rule modified by the effect of the card
     * @throws NotEnoughMoneyException when the player doesn't have enough money to use the character card
     * @throws IllegalMoveException when the action use power is not legit
     */
    public abstract Rule usePower(Player player) throws NotEnoughMoneyException, IllegalMoveException;

    /**
     * This method return a string that report some parameters of the character card
     * @return a String containing the parameters of the character card
     */
    public String shortString(){
        String s = ansi().bold().a("Desc: ").reset().a(desc + "\n").toString();
        s += ansi().bold().a("Cost: ").reset().a(getCost()).toString();
        return s;
    }

    /**
     * This method is the override of the method toString for the Character classes
     * @return the String to use when toString is called
     */
    @Override
    public String toString(){
        String s = "\u001B[1mID:\u001B[0m " + this.id + " - \u001B[1mType:\u001B[0m " + this.typeCharacter + " \n";
        s += "\u001B[1mDescription:\u001B[0m " + this.desc + " - \u001B[1mCost:\u001B[0m " + this.cost + "\n";
        s += "\u001B[1mActual Cost:\u001B[0m " + this.getCost() + "\n";
        return s;
    }

    /**
     * This method return the sprite of the character
     * @return a String representing the address of the sprite
     */
    public String getSprite(){
        return sprite;
    }

    /**
     * This method return the short description of the character
     * @return a string that contain the short description of the character
     */
    public String getDesc_short(){
        return desc_short;
    }
}