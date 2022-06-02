package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * The class dashboard represent the dashboard of the player, so inside the class are stored the graveyard (last ccard played)
 * the canteen, the entrance, the towers and the color of tha player who owns the dashboard, the class dashboard is a way to
 * unify all this elements in the game so is method are principally setter and getter
 */
public class Dashboard{
    private Card graveyard = null;
    private Canteen canteen;
    private Entrance entrance;
    private final ColorTower color;
    private final int maxTowers;
    private int towers;

    /**
     * Creator of entity Dahboard. It contains all player's assets.
     * @param numPlayers is the number of players in the match. It is needed to know how many
     *                   students have to be placed in the Entrance and the number of towers
     *                   to win the match
     * @param entranceStudents is a list containing the first 7/9 students to be placed in Entrance
     * @param color is the towers'color of the player or of his/her team
     */
    public Dashboard(int numPlayers, ArrayList<Student> entranceStudents, ColorTower color) {
        this.color = color;
        canteen = new Canteen();
        entrance = new Entrance(entranceStudents);
        maxTowers = numPlayers != 3 ? 8 : 6;
        towers = maxTowers;
    }

    /**
     * Method to return the object Canteen contained by this Dashboard
     * @return the object Canteen
     */
    public Canteen getCanteen(){
        return canteen;
    }

    /**
     * Method to return the object Entrance contained by this Dashboard
     * @return the object Entrance
     */
    public Entrance getEntrance(){
        return entrance;
    }

    /**
     * Method to return the number of towers that the owner of this Dashboard has still to place
     * @return number of remaining towers
     */
    public int getTowers(){
        return towers;
    }

    /**
     * Method to add towers to the Dashboard
     * @param towers number of towers to be returned to the Dashboard
     */
    public void addTowers(int towers){
        this.towers += towers;
    }

    /**
     * Method to remove towers to the Dashboard
     * @param towers number of towers to be removed from the Dashboard
     */
    public void removeTowers(int towers){
        this.towers -= towers;
    }

    /**
     * Method to get the last Card played by the owner of this Dashboard
     * @return the last Card played by the owner of this Dashboard
     */
    public Card getGraveyard(){
        return graveyard;
    }

    /**
     * Method to set the last Card played by the owner of this Dashboard
     * @param c is the Card the owner has played
     */
    public void setGraveyard(Card c){
        graveyard = c;
    }

    /**
     * Method to return the color of the towers of this Dashboard's owner
     * @return Color of the towers on this Dashboard
     */
    public ColorTower getColor(){
        return this.color;
    }
}