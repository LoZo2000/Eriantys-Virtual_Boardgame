package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.Map;

/**
 * The class MotherNatureRule represent the rule active when is activated a card that modify the movement of mother nature
 */
public class MotherNatureRule extends DecoratedRule{
    private final int extraMovement;

    /**
     * This method is the constructor of the class
     * @param extraMovement represent the amount of extra movement allowed to mother nature
     */
    public MotherNatureRule(int extraMovement){
        this.extraMovement = extraMovement;
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
        return this.defaultRules.calculateInfluence(report, professors);
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
        return this.extraMovement + this.defaultRules.getMotherNatureExtraMovement();
    }

    /**
     * This method return the maximum number of move of type exchange student the player can do
     * @return an int representing the number of exchange student tha player can do
     */
    @Override
    public int getMaximumExchangeMoves() {
        return this.defaultRules.getMaximumExchangeMoves();
    }
}