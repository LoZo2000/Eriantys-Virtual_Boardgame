package it.polimi.ingsw.model;

import java.util.Map;

public interface Rule {
    /**
     * This method is used to establish the team that has the higher influence on the Island.
     * @param report Report with the status of the Island on which we are calculating the influence.
     * @param professors Map which has the Color of the professor as key and as value the ColorTower that is the team
     *                   that has the professor.
     * @return Returns the Color of the tower (So the color of the team) that has the highest influence on the island.
     * The returned value can be null if there isn't any player that has the professors of the students on the island.
     */
    public ColorTower calculateInfluence(Report report, Map<Color, ColorTower> professors);

    /**
     * This method is used to check if there is a player that has a higher number of students in the canteen.
     * In this case the ownership of the professor change and the method returns the nickname of the player.
     * @param nameOwner Nickname of the player that has the ownership of the professor of a particular color at that
     *                  moment.
     * @param canteenStudents Map which has as key the nickname of a player and as value the number of students of that
     *                        color in the canteen of that player.
     * @return Nickname of the player that now has the professor.
     */
    public String updateProfessor(String nameOwner, Map<String, Integer> canteenStudents);
}