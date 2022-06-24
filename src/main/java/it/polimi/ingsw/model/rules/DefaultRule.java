package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * The class DefaultRule represent the rule when there are no character cards active, so the methods of the interface rule
 * implemented represent the default rule of the normal game
 */
public class DefaultRule implements Rule, Serializable {

    /**
     * This method calculate the influence of an Island
     * @param report Report with the status of the Island on which we are calculating the influence.
     * @param professors Map which has the Color of the professor as key and as value the ColorTower that is the team
     *                   that has the professor.
     * @return the enum ColorTower representing the player or team that have conquest the island
     */
    @Override
    public ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors) {
        //influencePerColor has the points for each team (BLACK, WHITE, GREY)
        //We consider points:
        //  1. The number of students whose professor is in that team possession
        //  2. The number of towers on the island
        //  3. The extra points given by the Rule if active
        HashMap<ColorTower, Integer> influencePerColor = new HashMap<>();

        //With this variable we identify the owner of the island at that moment, therefore the points the other teams have
        //to exceed to get the ownership
        int influencePointsOwner;
        ColorTower colorWinner = null;

        //Init of the points of each team at 0
        for(ColorTower c: ColorTower.values()){
            influencePerColor.put(c, 0);
        }

        //Calculation of the points given by the students on the island
        for(Color c: Color.values()){
            ColorTower owner = professors.get(c);
            if(owner != null)
                influencePerColor.replace(owner, influencePerColor.get(owner) + report.getColorStudents(c));
        }

        //Calculation of the extraPoints
        if(report.getExtraPointReceiver() != null)
            influencePerColor.replace(report.getExtraPointReceiver(), influencePerColor.get(report.getExtraPointReceiver()) + report.getExtraPoint());

        //Calculation of the points given by the towers on the island
        if(report.getOwner() != null) {
            influencePerColor.replace(report.getOwner(), influencePerColor.get(report.getOwner()) + report.getTowerNumbers());

            //We set the points of the owner of the island
            influencePointsOwner = influencePerColor.get(report.getOwner());
            colorWinner = report.getOwner();
        } else{
            //There is no owner, so there is no value to exceed
            influencePointsOwner = 0;
        }

        for(ColorTower c: ColorTower.values()){
            //If there is a team that has higher points it will be considered the new owner
            if(influencePointsOwner < influencePerColor.get(c)){
                colorWinner = c;
                influencePointsOwner = influencePerColor.get(c);
            } else if(influencePointsOwner == influencePerColor.get(c)){        //If there is a tie between the highest, the owner will return to be the original owner
                //influencePointsOwner = influencePerColor.get(report.getOwner());
                colorWinner = report.getOwner();
            }
        }

        return colorWinner;
    }

    /**
     * This method calculate and return the player that owns the professor
     * @param nameOwner Nickname of the player that has the ownership of the professor of a particular color at that
     *                  moment.
     * @param canteenStudents Map which has as key the nickname of a player and as value the number of students of that
     *                        color in the canteen of that player.
     * @return a String representing the nickname of the player that owns the professor
     */
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

    /**
     * This method return a boolean that report if is needed an action or not
     * @return a boolean reporting if is needed an action by the player
     */
    @Override
    public boolean isActionNeeded(){
        return false;
    }

    /**
     * This method return the extra movement possible for mother nature
     * @return an int representing the extra movement mother nature can do
     */
    @Override
    public int getMotherNatureExtraMovement() {
        return 0;
    }

    /**
     * This method return the maximum number of move of type exchange student the player can do
     * @return an int representing the number of exchange student tha player can do
     */
    @Override
    public int getMaximumExchangeMoves() {
        return 0;
    }
}