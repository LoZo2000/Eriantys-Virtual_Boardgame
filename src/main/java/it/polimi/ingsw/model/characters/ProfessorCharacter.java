package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.rules.ProfessorRule;
import it.polimi.ingsw.model.rules.Rule;

/**
 * The class ProfessorCharacter is the class that represent the characters that modify the computation of the possession of the professor
 */
public class ProfessorCharacter extends Character{

    /**
     * This method is the constructor of the class
     * @param id is the id of the Character
     * @param type is the type of the Character
     * @param desc is the description of the character
     * @param desc_short is the short version of the description of the character
     * @param cost is the cost to use the power of the character
     */
    public ProfessorCharacter(int id, CharacterType type, String desc, String desc_short, int cost){
        super(id, type, desc, desc_short, cost);
    }

    /**
     * This method modify the model with the changed needed to use the power of the card
     * @param player is the player that use the character card
     * @return an object Rule containing the rule modified by the effect of the card
     * @throws IllegalMoveException when the player doesn't have enough money to use the character card
     */
    @Override
    public Rule usePower(Player player) throws IllegalMoveException {
        player.useCoins(this.getCost());
        this.increaseTimesUsed();

        return new ProfessorRule(player.getNickname());
    }

    /**
     * This method is the override of the method toString for the ExchangeCharacter objects
     * @return the String to use when toString is called
     */
    @Override
    public String toString(){
        return super.toString();
    }
}
