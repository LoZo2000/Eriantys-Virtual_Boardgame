package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.rules.InfluenceRule;
import it.polimi.ingsw.model.rules.Rule;

/**
 * The class InfluenceCharacter is the class that represent the characters that modify the computation of the influence
 */
public class InfluenceCharacter extends Character{
    private final boolean towersDisabled;
    private final int extraPoints;

    /**
     * This method is the constructor of the class
     * @param id is the id of the Character
     * @param type is the type of the Character
     * @param desc is the description of the character
     * @param desc_short is the short version of the description of the character
     * @param cost is the cost to use the power of the character
     * @param params are the parameters of the card taken by a JSON file
     */
    public InfluenceCharacter(int id, CharacterType type, String desc, String desc_short, int cost, JSONParams params){
        super(id, type, desc, desc_short, cost);

        this.towersDisabled = params.getTowersDisabled();
        this.extraPoints = params.getExtraInfluencePoints();
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

        return new InfluenceRule(player.getColor(), null, extraPoints, towersDisabled);
    }

    /**
     * This method is the override of the method toString for the ExchangeCharacter objects
     * @return the String to use when toString is called
     */
    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mTowerDisabled:\u001B[0m " + this.towersDisabled + " - \u001B[1mExtraPoints:\u001B[0m " + this.extraPoints + "\n";
        return s;
    }
}