package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.*;

/**
 * The class PlayCardMessage represent the action that play an assistant card in the game, extends the class Message and implement the method
 * execute that apply changes to the game
 */
public class PlayCardMessage extends Message{
    final private int priority;

    /**
     * Is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param priority is an int representing the priority of the card (and the card itself)
     */
    public PlayCardMessage(String sender, int priority){
        super(sender, Action.PLAYCARD);
        this.priority = priority;
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to play
     * an assistant card
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws IllegalMoveException is the exception thrown if the action is not permitted
     * @throws NoPlayerException is the exception thrown if a player isn't found
     * @throws OverflowCardException if the Card doesn't exist or has already been played
     */
    @Override
    public Update execute(Game game) throws IllegalMoveException, NoPlayerException, OverflowCardException {
        Player p = game.getPlayer(sender);
        game.playCard(p, priority);

        return new Update(null, true, null, null, null, null, game.getFinishedGame(), game.getWinner(), game.getIsLastTurn());
    }

    /**
     * This method return the priority of the card
     * @return an int representing the priority of the card
     */
    public int getPriority(){
        return priority;
    }
}
