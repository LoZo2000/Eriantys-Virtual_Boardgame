package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

/**
 * The class PingMessage is used to send frequent messages to check if the connection is still up
 */
public class PingMessage extends Message{

    /**
     * Is the constructor of the class
     */
    public PingMessage() {
        super(null, Action.PING);
    }

    /**
     * The method execute is an override of the class Message, in this class it just return a null value
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws IllegalMoveException is the exception thrown if PingMessage is not a permitted action
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws NoPlayerException is the exception thrown if a player isn't found
     * @throws NoIslandException is the exception thrown if an island with a specific id isn't found
     * @throws NoCharacterSelectedException is the exception thrown if there isn't a character selected to use its power
     */
    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoPlayerException, NoIslandException, NoCharacterSelectedException {
        return null;
    }
}
