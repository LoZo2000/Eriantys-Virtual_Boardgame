package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;
import it.polimi.ingsw.model.exceptions.NoIslandException;
import it.polimi.ingsw.model.exceptions.NoMoreTokensException;

/**
 * The class BlockIslandMessage represent the action that block an island in the computation of the influence in the game, extends the class Message and implement the method
 * execute that apply changes to the game
 */
public class BlockIslandMessage extends ChooseIslandMessage{

    /**
     * This method is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param idIsland is an int that contain the island to "block" in the computation of the influence
     */
    public BlockIslandMessage(String sender, int idIsland) {
        super(sender, Action.BLOCK_ISLAND, idIsland);
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to block
     * an island
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws IllegalMoveException is the exception thrown if BlockIsland is not a permitted action or the island doesn't exist
     * @throws NoMoreTokensException is the exception thrown if there are no token left
     */
    @Override
    public Update execute(Game game) throws NoActiveCardException, IllegalMoveException, NoMoreTokensException {
        if (game.getActiveCard() != -1) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.BLOCK_ISLAND)
                throw new IllegalMoveException("Wrong move: you cannot block an island now!");
        }

        Island i;
        try {
            i = game.getIsland(idIsland);
        }catch(NoIslandException e){
            throw new IllegalMoveException("This island doesn't exist!");
        }

        game.disableIsland(i);

        return new Update(null, null, null, null, false, null, null, null);
    }

}
