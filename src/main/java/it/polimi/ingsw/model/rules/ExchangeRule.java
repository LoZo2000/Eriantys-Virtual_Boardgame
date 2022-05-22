package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.Map;

public class ExchangeRule extends DecoratedRule{
    private final int maximumExchangeMoves;

    public ExchangeRule(int maximumMoves){
        this.maximumExchangeMoves = maximumMoves;
    }

    @Override
    public ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors) {
        return this.defaultRules.calculateInfluence(report, professors);
    }

    @Override
    public String updateProfessor(String nameOwner, Map<String, Integer> canteenStudents) {
        return this.defaultRules.updateProfessor(nameOwner, canteenStudents);
    }

    @Override
    public boolean isActionNeeded() {
        return true;
    }

    @Override
    public int getMotherNatureExtraMovement() {
        return this.defaultRules.getMotherNatureExtraMovement();
    }

    @Override
    public int getMaximumExchangeMoves() {
        return this.maximumExchangeMoves;
    }
}
