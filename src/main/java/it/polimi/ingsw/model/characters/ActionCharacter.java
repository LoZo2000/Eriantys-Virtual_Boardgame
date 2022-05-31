package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NoMoreTokensException;
import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.model.rules.ActionRule;
import it.polimi.ingsw.model.rules.Rule;

import static org.fusesource.jansi.Ansi.*;

public class ActionCharacter extends Character{
    private final Action type;
    private final int maxNumTokens;
    private int numTokens;

    public ActionCharacter(int id, CharacterType type, String desc, String desc_short, int cost, JSONParams params){
        super(id, type, desc, desc_short, cost);

        this.maxNumTokens = params.getNumThingOnIt();
        this.numTokens = params.getNumThingOnIt();
        this.type = params.getTypeAction();
    }

    public Action getType() {
        return type;
    }

    public int getNumTokens(){
        return numTokens;
    }

    public int getMaxNumTokens(){
        return maxNumTokens;
    }

    public void removeToken() throws NoMoreTokensException {
        if(numTokens == 0){
            throw new NoMoreTokensException("There is no token left");
        }
        this.numTokens -= 1;
    }

    public void addToken() throws NoMoreTokensException {
        if(numTokens == maxNumTokens){
            throw new NoMoreTokensException("There is the maximum number of token on the card");
        }
        this.numTokens += 1;
    }

    @Override
    public Rule usePower(Player player) throws NotEnoughMoneyException, IllegalMoveException {
        player.useCoins(this.getCost());
        this.increaseTimesUsed();

        return new ActionRule();
    }

    @Override
    public String shortString(){
        String s = super.shortString();
        s += ansi().a(" - ").bold().a("Tokens: ").reset().a(numTokens).toString();
        return s;
    }

    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mActionType:\u001B[0m " + this.type + " - \u001B[1mMaxNumTokens:\u001B[0m " + this.maxNumTokens + "\n";
        s += "\u001B[1mActualNumTokens:\u001B[0m " + this.numTokens + "\n";
        return s;
    }
}
