package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;


public class DefaultRule implements Rule{

    @Override
    public ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors) {
        HashMap<ColorTower, Integer> influencePerColor = new HashMap<>();
        int influencePointsOwner;
        ColorTower colorWinner = null;

        for(ColorTower c: ColorTower.values()){
            influencePerColor.put(c, 0);
        }

        for(Color c: Color.values()){
            ColorTower owner = professors.get(c);
            if(owner != null)
                influencePerColor.replace(owner, influencePerColor.get(owner) + report.getColorStudents(c));
        }

        if(report.getOwner() != null) {
            influencePerColor.replace(report.getOwner(), influencePerColor.get(report.getOwner()) + report.getTowerNumbers());
            influencePointsOwner = influencePerColor.get(report.getOwner());
            colorWinner = report.getOwner();
        } else{
            influencePointsOwner = 0;
        }

        for(ColorTower c: ColorTower.values()){
            if(influencePointsOwner < influencePerColor.get(c)){
                colorWinner = c;
                influencePointsOwner = influencePerColor.get(c);
            } else if(influencePointsOwner == influencePerColor.get(c)){
                //influencePointsOwner = influencePerColor.get(report.getOwner());
                colorWinner = report.getOwner();
            }
        }

        return colorWinner;
    }

    @Override
    public String updateProfessor(String nameOwner, Map<String, Integer> canteenStudents) {
        String nicknameWinner = nameOwner;
        int highestNumber;

        if(nameOwner != null){
            highestNumber = canteenStudents.get(nameOwner);
        } else{
            highestNumber = 0;
        }

        for(Map.Entry<String, Integer> p: canteenStudents.entrySet()){
            if(highestNumber < p.getValue()){
                nicknameWinner = p.getKey();
                highestNumber = p.getValue();
            }
        }

        return nicknameWinner;
    }
}
