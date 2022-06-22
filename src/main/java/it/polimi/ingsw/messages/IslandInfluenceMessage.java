package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.exceptions.*;


/**
 * The class IslandInfluenceMessage represent the action that compute the influence of an island without moving mother nature, extends the class Message
 * and implement the method execute that apply changes to the game
 */
public class IslandInfluenceMessage extends ChooseIslandMessage{

    /**
     * The constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param idIsland is the id of the island of which compute the influence
     */
    public IslandInfluenceMessage(String sender, int idIsland) {
        super(sender, Action.ISLAND_INFLUENCE, idIsland);
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to compute
     * the influence of the island
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws IllegalMoveException is the exception thrown if IslandInfluenceMessage is not a permitted action
     * @throws NoMoreTokensException is the exception thrown if there are no token left
     */
    @Override
    public Update execute(Game game) throws NoActiveCardException, IllegalMoveException, NoMoreTokensException {
        if (game.getActiveCard() != -1) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.ISLAND_INFLUENCE)
                throw new IllegalMoveException("Wrong move: you cannot choose an island!");
        }

        Island i;
        try {
            i = game.getIsland(idIsland);
        }catch(NoIslandException e){
            throw new IllegalMoveException("This island doesn't exist!");
        }

        game.moveMotherNature(i, false);

        return new Update(null, null, null, null, false, null, game.getFinishedGame(), game.getIsLastTurn());
    }
}