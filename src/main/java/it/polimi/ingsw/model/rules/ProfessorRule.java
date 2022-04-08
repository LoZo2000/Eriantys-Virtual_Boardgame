package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.Map;

public class ProfessorRule extends DecoratedRule{
    private final String playerWhoUsed;

    public ProfessorRule(String playerWhoUsed){
        this.playerWhoUsed = playerWhoUsed;
    }

    @Override
    public ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors) {
        return this.defaultRules.calculateInfluence(report, professors);
    }

    @Override
    public String updateProfessor(String nameOwner, Map<String, Integer> canteenStudents) {
        canteenStudents.replace(this.playerWhoUsed, canteenStudents.get(this.playerWhoUsed) + 1);
        return this.defaultRules.updateProfessor(nameOwner, canteenStudents);
    }

    @Override
    public boolean isActionNeeded() {
        return false;
    }

    @Override
    public int getMotherNatureExtraMovement() {
        return this.defaultRules.getMotherNatureExtraMovement();
    }
}
