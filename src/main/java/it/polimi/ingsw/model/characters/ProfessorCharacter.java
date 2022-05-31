package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.model.rules.ProfessorRule;
import it.polimi.ingsw.model.rules.Rule;

public class ProfessorCharacter extends Character{
    public ProfessorCharacter(int id, CharacterType type, String desc, String desc_short, int cost){
        super(id, type, desc, desc_short, cost);
    }

    @Override
    public Rule usePower(Player player) throws NotEnoughMoneyException {
        player.useCoins(this.getCost());
        this.increaseTimesUsed();

        return new ProfessorRule(player.getNickname());
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
