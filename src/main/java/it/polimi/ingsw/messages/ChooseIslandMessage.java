package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

/**
 * ChooseIslandMessage is an abstract class that is inherited by class that need to choose an island
 */
public abstract class ChooseIslandMessage extends Message{
    protected final int idIsland;

    /**
     * This method is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param action is the Action that will be performed
     * @param idIsland is an int that represent the island chosen
     */
    public ChooseIslandMessage(String sender, Action action, int idIsland) {
        super(sender, action);
        this.idIsland = idIsland;
    }

    /**
     * This method return the island chosen in the message
     * @return an int representing the id of the island chosen
     */
    public int getIdIsland(){
        return this.idIsland;
    }
}