package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

/**
 * The class PutBackMessage represent the action that put back in the bag some students (according the rule) of a color, extends
 * the class Message and implement the method execute that apply changes to the game
 */
public class PutBackMessage extends ChooseColorMessage{

    /**
     * Is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param color is the color of the students to put back
     */
    public PutBackMessage(String sender, Color color) {
        super(sender, Action.PUT_BACK, color);
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to put back
     * the students in the bag, check if the character card is active
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws IllegalMoveException is the exception thrown if the action is not permitted
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws NoSuchStudentException is the exception thrown if there is not any student with a specific id
     */
    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoSuchStudentException {
        if (game.getActiveCard() != -1) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.PUT_BACK)
                throw new IllegalMoveException("Wrong move: you cannot put back students of a color now!");
        }

        game.putBackInBag(chosenColor);

        return new Update(null, null, null, null, false, null, game.getFinishedGame(), game.getIsLastTurn());
    }
}