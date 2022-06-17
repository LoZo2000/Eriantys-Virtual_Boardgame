package it.polimi.ingsw.model;

import java.util.Map;

/**
 * The report class represent the situation present of an island and permit the computation of the influence,
 * to represent the situation of the island the report have the attributes to represent
 * the owner of an island, the number of tower, the students for each color, and the extra point for a ColorTower due to the
 * activation of a character card
 */
public class Report {
    //private final String id;
    private final ColorTower owner;
    private final int towerNumbers;
    private final Map<Color, Integer> students;

    private final ColorTower extraPointReceiver;
    private final int extraPoint;

    /**
     * Constructor of the entity Report, Report is the entity used to calculate the influence of a island, is created by the
     * method getreport in island
     * @param owner is the owner of the island, is null if the island don't havve a owner
     * @param towerNumbers is the number of towers in the island
     * @param students a map of the students in the island
     * @param extraPointReceiver The colorTower (that represent the user) that will receive extrapoint (if a specific character card is active)
     * @param extrapoints the number of point the user receive (if active a specific character card)
     */
    public Report(ColorTower owner, int towerNumbers, Map<Color, Integer> students, ColorTower extraPointReceiver, int extrapoints){
        //this.id = id;
        this.owner= owner;
        this.towerNumbers=towerNumbers;
        this.students= students;
        this.extraPointReceiver = extraPointReceiver;
        this.extraPoint = extrapoints;
    }

    //public String getId(){ return id; }

    /**
     * method that return the owner of the island
     * @return the enum ColorTower representing the owner of the island
     */
    public ColorTower getOwner(){ return owner; }

    /**
     * method that return the number of tower in the island
     * @return the in that represent the number of tower in the island
     */
    public int getTowerNumbers(){ return towerNumbers; }

    /**
     * method that return the number of students of a color c
     * @param c the color of the number of students to return
     * @return the int that represent the number of student of the color c
     */
    public int getColorStudents(Color c){
        return students.get(c);
    }

    /**
     * method that return the ColorTower (user or team) that receive the extra points ( if a specific card is activated)
     * @return the enum ColorTower that represent user or team that receive the extra points
     */
    public ColorTower getExtraPointReceiver(){
        return extraPointReceiver;
    }

    /**
     * method that return the amount of extra point ( if a specific character card is activated)
     * @return the int that represent the amount of extra point that a specific ColorTower have
     */
    public int getExtraPoint(){
        return extraPoint;
    }
}