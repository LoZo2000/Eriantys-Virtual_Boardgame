package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.rules.Rule;

public abstract class Character {
    private final int id;
    private final String desc;
    private final CharacterType typeCharacter;
    private final int cost;

    public Character(int id, CharacterType type, String desc, int cost){
        this.id = id;
        this.typeCharacter = type;
        this.desc = desc;
        this.cost = cost;
    }

    public int getId(){
        return id;
    }

    public int getCost() {
        return cost;
    }

    public CharacterType getTypeCharacter(){
        return this.typeCharacter;
    }

    public abstract Rule usePower(Player player);

    @Override
    public String toString(){
        String s = "\u001B[1mID:\u001B[0m " + this.id + " - \u001B[1mType:\u001B[0m " + this.typeCharacter + " \n";
        s += "\u001B[1mDescription:\u001B[0m " + this.desc + " - \u001B[1mCost:\u001B[0m " + this.cost + "\n";
        return s;
    }
}
