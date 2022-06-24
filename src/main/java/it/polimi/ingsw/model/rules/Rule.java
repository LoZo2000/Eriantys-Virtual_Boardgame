package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.Map;

/**
 * The interface Rule has to be implemented by every Rule class, it assures the implementation of the methods calculateInfluence,
 *  updateProfessor, isActionNeeded, getMotherNatureExtraMovement, getMaximumExchangeMoves, exists different type of rules
 *  depending on the current active CharacterCard, and this methods cover the aspects that change in the game if a different card
 *  is active
 */
public interface Rule {
    /**
     * This method is used to establish the team that has the higher influence on the Island.
     * @param report Report with the status of the Island on which we are calculating the influence.
     * @param professors Map which has the Color of the professor as key and as value the ColorTower that is the team
     *                   that has the professor.
     * @return Returns the Color of the tower (So the color of the team) that has the highest influence on the island.
     * The returned value can be null if there isn't any player that has the professors of the students on the island.
     */

    ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors);

    /**
     * This method is used to check if there is a player that has a higher number of students in the canteen.
     * In this case the ownership of the professor change and the method returns the nickname of the player.
     * @param nameOwner Nickname of the player that has the ownership of the professor of a particular color at that
     *                  moment.
     * @param canteenStudents Map which has as key the nickname of a player and as value the number of students of that
     *                        color in the canteen of that player.
     * @return Nickname of the player that now has the professor.
     */
    String updateProfessor(String nameOwner, Map<String, Integer> canteenStudents);

    /**
     * This method return a boolean that report if is needed an action or not
     * @return a boolean reporting if is needed an action by the player
     */
    boolean isActionNeeded();

    /**
     * This method return the extra movement possible for mother nature
     * @return an int representing the extra movement mother nature can do
     */
    int getMotherNatureExtraMovement();

    /**
     * This method return the maximum number of move of type exchange student the player can do
     * @return an int representing the number of exchange student tha player can do
     */
    int getMaximumExchangeMoves();
}