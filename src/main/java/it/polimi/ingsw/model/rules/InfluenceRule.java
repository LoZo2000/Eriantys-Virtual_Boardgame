package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.HashMap;
import java.util.Map;

/**
 * The class InfluenceRule represent the rule active when is activated a card that modify the influence computation
 */
public class InfluenceRule extends DecoratedRule{
    private final ColorTower playerWhoUsed;
    private final Color blockedColor;
    private final int extraPoints;
    private final boolean disableTowers;

    /**
     * This method is the constructor of the class
     * @param playerWhoUsed represent the player who used the card
     * @param blockedColor represent the color blocked by the player
     * @param extraPoints represent the extra points that the player have
     * @param disableTowers report if the computation of the towers are disabled or not
     */
    public InfluenceRule(ColorTower playerWhoUsed, Color blockedColor, int extraPoints, boolean disableTowers){
        this.playerWhoUsed = playerWhoUsed;
        this.blockedColor = blockedColor;
        this.disableTowers = disableTowers;
        this.extraPoints = extraPoints;
    }

    /**
     * This method calculate the influence of an Island
     * @param report Report with the status of the Island on which we are calculating the influence.
     * @param professors Map which has the Color of the professor as key and as value the ColorTower that is the team
     *                   that has the professor.
     * @return the enum ColorTower representing the player or team that have conquest the island
     */
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
        return this.defaultRules.updateProfessor(nameOwner, canteenStudents);
    }

    /**
     * This method return a boolean that report if is needed an action or not
     * @return a boolean reporting if is needed an action by the player
     */
    @Override
    public boolean isActionNeeded() {
        return false;
    }

    /**
     * This method return the extra movement possible for mother nature
     * @return an int representing the extra movement mother nature can do
     */
    @Override
    public int getMotherNatureExtraMovement() {
        return this.defaultRules.getMotherNatureExtraMovement();
    }

    /**
     * This method return the maximum number of move of type exchange student the player can do
     * @return an int representing the number of exchange student tha player can do
     */
    @Override
    public int getMaximumExchangeMoves() {
        return this.defaultRules.getMaximumExchangeMoves();
    }

    /**
     * This method return the player who used the character card
     * @return an enum ColorTower representing the player who used the character card
     */
    public ColorTower getPlayerWhoUsed() {
        return playerWhoUsed;
    }

    /**
     * This method return the color blocked by the player
     * @return an enum Color representing the color blocked by the player
     */
    public Color getBlockedColor() {
        return blockedColor;
    }

    /**
     * This method return the extra points received by the player
     * @return an int representing tha amount of extra points received by the player
     */
    public int getExtraPoints() {
        return extraPoints;
    }

    /**
     * This method report if the towers are disabled in the computation or not
     * @return a boolean reporting if the towers are disabled in the computation or not
     */
    public boolean isDisableTowers() {
        return disableTowers;
    }
}