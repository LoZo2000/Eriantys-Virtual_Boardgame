package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.rules.ActionRule;
import it.polimi.ingsw.model.rules.ExchangeRule;
import it.polimi.ingsw.model.rules.Rule;

import java.util.ArrayList;
import java.util.Set;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * The class ExchangeCharacter is the class that represent the characters that enable exchange movements
 */
public class ExchangeCharacter extends MovementCharacter implements Movable {

    private final int maxMoves;

    /**
     * This method is the constructor of the class
     * @param id is the id of the Character
     * @param type is the type of the Character
     * @param desc is the description of the character
     * @param desc_short is the short version of the description of the character
     * @param cost is the cost to use the power of the character
     * @param students is an arraylist of the students to exchange
     * @param params are the parameters of the card taken by a JSON file
     */
    public ExchangeCharacter(int id, CharacterType type, String desc, String desc_short, int cost, ArrayList<Student> students, JSONParams params){
        super(id, type, desc, desc_short, cost, students, params);

        maxMoves = params.getMaxMoves();
    }

    /**
     * This method return the max moves permitted by the card
     * @return an int representing the number of max movement permitted
     */
    public int getMaxMoves(){
        return maxMoves;
    }

    /**
     * This method modify the model with the changed needed to use the power of the card
     * @param player is the player that use the character card
     * @return an object Rule containing the rule modified by the effect of the card
     * @throws IllegalMoveException when the player doesn't have enough money to use the character card or the action use power is not legit
     */
    @Override
    public Rule usePower(Player player) throws IllegalMoveException {
        if(allowedArrivals.contains(Location.CANTEEN)){
            if(checkEmptyCanteen(player)){
                throw new IllegalMoveException("You can't exchange students with the canteen if there isn't any student in it.");
            }
        }
        player.useCoins(this.getCost());
        this.increaseTimesUsed();

        return new ExchangeRule(maxMoves);
    }

    /**
     * This method return a string that report some parameters of the character card
     * @return a String containing the parameters of the character card
     */
    @Override
    public String shortString(){
        String s = super.shortString();
        s += ansi().a(" - ").bold().a("Max Exchanges: ").reset().a(maxMoves).toString();
        return s;
    }

    /**
     * This method is the override of the method toString for the ExchangeCharacter objects
     * @return the String to use when toString is called
     */
    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mMaximum Exchanges:\u001B[0m " + this.maxMoves + "\n";
        return s;
    }

    /**
     * This method check id the canteen is empty
     * @param p is the player that owns the canteen to check
     * @return  a boolean that report if the canteen is empty
     */
    private boolean checkEmptyCanteen(Player p) {
        for (Color c : Color.values()) {
            if (p.getDashboard().getCanteen().getNumberStudentColor(c) != 0)
                return false;
        }

        return true;
    }
}
