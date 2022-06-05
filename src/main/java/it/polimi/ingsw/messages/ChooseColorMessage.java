package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;

/**
 * ChooseColorMessage is an abstract class that is inherited by class that need to choose a color
 */
public abstract class ChooseColorMessage extends Message{
    protected final Color chosenColor;

    /**
     * This method is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param action is the Action that will be perfomed
     * @param color is an Enum that contain the color that will be used by the action
     */
    public ChooseColorMessage(String sender, Action action, Color color) {
        super(sender, action);
        this.chosenColor = color;
    }

    /**
     * This method return the color chosen in the message
     * @return an Enum color that represent the color chosen
     */
    public Color getChosenColor(){
        return this.chosenColor;
    }
}
