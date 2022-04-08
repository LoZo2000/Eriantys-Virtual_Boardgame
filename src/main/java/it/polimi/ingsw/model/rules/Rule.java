package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;

import java.util.Map;

public interface Rule {
    //TODO Change parameters: add interface object of Model that can change the number of prohibition tokens on card when mother nature is on that island
    //Change ISLAND ID with String, but attention to id '1' and '10' because '1' is considered contained in '10'
    //Try to use id like '01'
    //To do that use "String.format("%02d", i)" in the initialization of the islands
    //In the ID of Island the single ids need to be sorted from lower to higher => Check order in string
    /**
     * This method is used to establish the team that has the higher influence on the Island.
     * @param report Report with the status of the Island on which we are calculating the influence.
     * @param professors Map which has the Color of the professor as key and as value the ColorTower that is the team
     *                   that has the professor.
     * @return Returns the Color of the tower (So the color of the team) that has the higher influence on the island.
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

    boolean isActionNeeded();

    int getMotherNatureExtraMovement();
}
