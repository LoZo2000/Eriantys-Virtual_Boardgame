package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.model.rules.Rule;

import java.io.Serializable;

public abstract class Character implements Serializable {
    private final int id;
    private final String desc;
    private final CharacterType typeCharacter;
    private final int cost;
    private int numberTimesUsed;

    public Character(int id, CharacterType type, String desc, int cost){
        this.id = id;
        this.typeCharacter = type;
        this.desc = desc;
        this.cost = cost;
        this.numberTimesUsed = 0;
    }

    public int getId(){
        return this.id;
    }

    public int getCost() {
        return cost + numberTimesUsed;
    }

    protected void increaseTimesUsed(){
        this.numberTimesUsed += 1;
    }

    public CharacterType getTypeCharacter(){
        return this.typeCharacter;
    }

    public abstract Rule usePower(Player player) throws NotEnoughMoneyException;

    @Override
    public String toString(){
        String s = "\u001B[1mID:\u001B[0m " + this.id + " - \u001B[1mType:\u001B[0m " + this.typeCharacter + " \n";
        s += "\u001B[1mDescription:\u001B[0m " + this.desc + " - \u001B[1mCost:\u001B[0m " + this.cost + "\n";
        s += "\u001B[1mActual Cost:\u001B[0m " + this.getCost() + "\n";
        return s;
    }
}
