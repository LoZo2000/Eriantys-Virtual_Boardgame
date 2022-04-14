package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.rules.MotherNatureRule;
import it.polimi.ingsw.model.rules.Rule;

public class MotherNatureCharacter extends Character{
    private final int extraMovement;

    public MotherNatureCharacter(int id, CharacterType type, String desc, int cost, JSONParams params){
        super(id, type, desc, cost);

        this.extraMovement = params.getExtraMNMovement();
    }

    public int getExtraMovement(){
        return this.extraMovement;
    }

    @Override
    public Rule usePower(Player player) {
        return new MotherNatureRule(extraMovement);
    }

    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mExtraMovement:\u001B[0m " + this.extraMovement + "\n";
        return s;
    }
}