package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.HashMap;
import java.util.Map;

public class InfluenceRule extends DecoratedRule{
    private final ColorTower playerWhoUsed;
    private final int extraPoints;
    private final boolean disableTowers;

    public InfluenceRule(ColorTower playerWhoUsed, int extraPoints, boolean disableTowers){
        this.playerWhoUsed = playerWhoUsed;
        this.disableTowers = disableTowers;
        this.extraPoints = extraPoints;
    }

    @Override
    public ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors) {
        HashMap<Color, Integer> students = new HashMap<>();
        for(Color c: Color.values()){
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
        return true;
    }
}
