package it.polimi.ingsw.model;

/**
 * The class MotherNature represent the entity mother nature in the game, so have an attribute to show her position
 * and some method to get and set her position
 */
public class MotherNature{
    private Island position;

    /**
     * Creator of the class MotherNature. In the beginning, MotherNature is not positioned on an Island
     */
    public MotherNature(){position = null;}

    /**
     * Method to move MotherNature to another Island
     * @param arrival
     */
    public void movement(Island arrival){
        position = arrival;
    }

    /**
     * Method to return the current position of MotherNature
     * @return the Island where MotherNature is positioned
     */
    public Island getPosition(){
        return position;
    }
}