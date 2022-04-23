package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;
import it.polimi.ingsw.model.rules.InfluenceRule;
import it.polimi.ingsw.model.rules.Rule;

public class InfluenceCharacter extends Character{
    private final boolean towersDisabled;
    private final int extraPoints;

    public InfluenceCharacter(int id, CharacterType type, String desc, int cost, JSONParams params){
        super(id, type, desc, cost);

        this.towersDisabled = params.getTowersDisabled();
        this.extraPoints = params.getExtraInfluencePoints();
    }

    public boolean getTowersDisabled(){
        return this.towersDisabled;
    }

    public int getExtraPoints(){
        return this.extraPoints;
    }

    @Override
    public Rule usePower(Player player) throws NotEnoughMoneyException {
        player.useCoins(this.getCost());
        this.increaseTimesUsed();

        return new InfluenceRule(player.getColor(), null, extraPoints, towersDisabled);
    }

    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mTowerDisabled:\u001B[0m " + this.towersDisabled + " - \u001B[1mExtraPoints:\u001B[0m " + this.extraPoints + "\n";
        return s;
    }
}
