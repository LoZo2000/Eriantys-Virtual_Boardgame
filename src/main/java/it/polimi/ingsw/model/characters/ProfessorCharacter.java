package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.rules.ProfessorRule;
import it.polimi.ingsw.model.rules.Rule;

public class ProfessorCharacter extends Character{
    public ProfessorCharacter(int id, CharacterType type, String desc, int cost){
        super(id, type, desc, cost);
    }

    @Override
    public Rule usePower(Player player) {
        return new ProfessorRule(player.getNickname());
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
