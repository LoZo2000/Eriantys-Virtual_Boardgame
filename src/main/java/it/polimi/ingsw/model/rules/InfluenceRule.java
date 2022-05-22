package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.HashMap;
import java.util.Map;

public class InfluenceRule extends DecoratedRule{
    private final ColorTower playerWhoUsed;
    private final Color blockedColor;
    private final int extraPoints;
    private final boolean disableTowers;

    public InfluenceRule(ColorTower playerWhoUsed, Color blockedColor, int extraPoints, boolean disableTowers){
        this.playerWhoUsed = playerWhoUsed;
        this.blockedColor = blockedColor;
        this.disableTowers = disableTowers;
        this.extraPoints = extraPoints;
    }

    @Override
    public ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors) {
        HashMap<Color, Integer> students = new HashMap<>();
        for(Color c: Color.values()){
            if(c == blockedColor)
                students.put(c, 0);
            else
                students.put(c, report.getColorStudents(c));
        }
        Report newReport;
        if(disableTowers){
            //Tower put to 0 when towers are disabled
            newReport = new Report(report.getOwner(), 0, students, playerWhoUsed, extraPoints);
        } else{
            //There are extra points
            newReport = new Report(report.getOwner(), report.getTowerNumbers(), students, playerWhoUsed, extraPoints);
        }

        return this.defaultRules.calculateInfluence(newReport, professors);
    }

    @Override
    public String updateProfessor(String nameOwner, Map<String, Integer> canteenStudents) {
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

    @Override
    public int getMaximumExchangeMoves() {
        return this.defaultRules.getMaximumExchangeMoves();
    }

    public ColorTower getPlayerWhoUsed() {
        return playerWhoUsed;
    }

    public Color getBlockedColor() {
        return blockedColor;
    }

    public int getExtraPoints() {
        return extraPoints;
    }

    public boolean isDisableTowers() {
        return disableTowers;
    }
}
