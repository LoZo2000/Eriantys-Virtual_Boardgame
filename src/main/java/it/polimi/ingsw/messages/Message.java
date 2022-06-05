package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

import java.io.Serializable;

/**
 * Message is an abstract class that every kind of message extend to represent different actions
 */
public abstract class Message implements Serializable {
    final protected String sender;
    final protected Action action;

    /**
     * Constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param action is the action that will be performed
     */
    public Message(String sender, Action action){
        this.sender = sender;
        this.action = action;
    }

    /**
     *
     * @param game
     * @return
     * @throws IllegalMoveException is the exception thrown if the action is not permitted
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws NoPlayerException is the exception thrown if a player isn't found
     * @throws NoIslandException is the exception thrown if an island with a specific id isn't found
     * @throws NoMoreTokensException is the exception thrown if there are not token left
     * @throws NotEnoughMoneyException is the exception thrown if the player doesn't have enough money to activate the character card
     * @throws NoCharacterSelectedException is the exception thrown if there isn't a character selected to use its power
     * @throws NoSuchStudentException is the exception thrown if there is not any student with a specific id
     * @throws CannotAddStudentException is the exception thrown if the student can't be added
     */
    public abstract Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoPlayerException, NoIslandException, NoMoreTokensException, NotEnoughMoneyException, NoCharacterSelectedException, NoSuchStudentException, CannotAddStudentException;

    /**
     * This method return the username of the sender of the message
     * @return a String representing the sender
     */
    public String getSender(){
        return sender;
    }

    /**
     * This method return the action of the message
     * @return the enum representing the Action of the message
     */
    public Action getAction(){
        return action;
    }
}
