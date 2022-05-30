package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.model.rules.Rule;

import java.io.Serializable;

import static org.fusesource.jansi.Ansi.*;

public abstract class Character implements Serializable {
    private final int id;
    private final String desc;
    private final CharacterType typeCharacter;
    private final int cost;
    private int numberTimesUsed;
    private String sprite = null;

    public Character(int id, CharacterType type, String desc, int cost){
        this.id = id;
        this.typeCharacter = type;
        this.desc = desc;
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

    public int getId(){
        return this.id;
    }

    public int getCost() {
        return cost + numberTimesUsed;
    }
    public int getOriginalCost(){
        return cost;
    }

    protected void increaseTimesUsed(){
        this.numberTimesUsed += 1;
    }

    public CharacterType getTypeCharacter(){
        return this.typeCharacter;
    }

    public abstract Rule usePower(Player player) throws NotEnoughMoneyException, IllegalMoveException;

    public String shortString(){
        String s = ansi().bold().a("Desc: ").reset().a(desc + "\n").toString();
        s += ansi().bold().a("Cost: ").reset().a(getCost()).toString();
        return s;
    }

    @Override
    public String toString(){
        String s = "\u001B[1mID:\u001B[0m " + this.id + " - \u001B[1mType:\u001B[0m " + this.typeCharacter + " \n";
        s += "\u001B[1mDescription:\u001B[0m " + this.desc + " - \u001B[1mCost:\u001B[0m " + this.cost + "\n";
        s += "\u001B[1mActual Cost:\u001B[0m " + this.getCost() + "\n";
        return s;
    }

    public String getSprite(){
        return sprite;
    }
}