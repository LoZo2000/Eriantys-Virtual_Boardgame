package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NoMoreTokensException;
import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.model.rules.ActionRule;
import it.polimi.ingsw.model.rules.Rule;

import static org.fusesource.jansi.Ansi.*;

/**
 * The class ActionCharacter is the class that represent the characters that require an action by the player
 */
public class ActionCharacter extends Character{
    private final Action type;
    private final int maxNumTokens;
    private int numTokens;

    /**
     * This method is the constructor of the class
     * @param id is the id of the Character
     * @param type is the type of the Character
     * @param desc is the description of the character
     * @param desc_short is the short version of the description of the character
     * @param cost is the cost to use the power of the character
     * @param params are the parameters of the card taken by a JSON file
     */
    public ActionCharacter(int id, CharacterType type, String desc, String desc_short, int cost, JSONParams params){
        super(id, type, desc, desc_short, cost);

        this.maxNumTokens = params.getNumThingOnIt();
        this.numTokens = params.getNumThingOnIt();
        this.type = params.getTypeAction();
    }

    /**
     * This method return the Action type
     * @return an enum representing the Action type
     */
    public Action getType() {
        return type;
    }

    /**
     * This method return the number of tokens
     * @return an int representing the number of tokens
     */
    public int getNumTokens(){
        return numTokens;
    }

    /**
     * This method return the maximum number of token
     * @return an int representing the maximum number of token
     */
    public int getMaxNumTokens(){
        return maxNumTokens;
    }

    /**
     * This method remove a token
     * @throws NoMoreTokensException when there are no token left to remove
     */
    public void removeToken() throws NoMoreTokensException {
        if(numTokens == 0){
            throw new NoMoreTokensException("There is no token left");
        }
        this.numTokens -= 1;
    }

    /**
     * This method add a token
     * @throws NoMoreTokensException when the token reached the maximum number permitted
     */
    public void addToken() throws NoMoreTokensException {
        if(numTokens == maxNumTokens){
            throw new NoMoreTokensException("There is the maximum number of token on the card");
        }
        this.numTokens += 1;
    }

    /**
     * This method modify the model with the changed needed to use the power of the card
     * @param player is the player that use the character card
     * @return an object Rule containing the rule modified by the effect of the card
     * @throws NotEnoughMoneyException when the player doesn't have enough money to use the character card
     * @throws IllegalMoveException when the action use power is not legit
     */
    @Override
    public Rule usePower(Player player) throws NotEnoughMoneyException, IllegalMoveException {
        player.useCoins(this.getCost());
        this.increaseTimesUsed();

        return new ActionRule();
    }

    /**
     * This method return a string that report some parameters of the character card
     * @return a String containing the parameters of the character card
     */
    @Override
    public String shortString(){
        String s = super.shortString();
        s += ansi().a(" - ").bold().a("Tokens: ").reset().a(numTokens).toString();
        return s;
    }

    /**
     * This method is the override of the method toString for the Action Character objects
     * @return the String to use when toString is called
     */
    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mActionType:\u001B[0m " + this.type + " - \u001B[1mMaxNumTokens:\u001B[0m " + this.maxNumTokens + "\n";
        s += "\u001B[1mActualNumTokens:\u001B[0m " + this.numTokens + "\n";
        return s;
    }
}
