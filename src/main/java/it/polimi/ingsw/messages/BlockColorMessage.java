package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.*;

/**
 * The class BlockColorMessage represent the action that block a color in the computation of the influence in the game, extends the class Message and implement the method
 * execute that apply changes to the game
 */
public class BlockColorMessage extends ChooseColorMessage{

    /**
     * This method is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param color is an Enum that contain the color to "block" in the computation of the influence
     */
    public BlockColorMessage(String sender, Color color) {
        super(sender, Action.BLOCK_COLOR, color);
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to block
     * a color
     * @param game   is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws IllegalMoveException is the exception thrown if BlockColor is not a permitted action
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws NoPlayerException is the exception thrown if a player isn't found
     */
    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoPlayerException {
        if (game.getActiveCard() != -1) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.BLOCK_COLOR)
                throw new IllegalMoveException("Wrong move: you cannot choose a color to be blocked!");
        }

        Player p = game.getPlayer(sender);

        game.disableColor(p, chosenColor);

        return new Update(null, null, null, null, false, null, null, null);
    }
}