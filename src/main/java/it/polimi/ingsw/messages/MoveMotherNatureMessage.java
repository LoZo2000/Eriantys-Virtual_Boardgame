package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.*;

import java.util.LinkedList;

/**
 * The class MoveMotherNatureMessage represent the action that move mother nature in the game, extends the class Message and implement the method
 * execute that apply changes to the game
 */
public class MoveMotherNatureMessage extends Message{
    final private int movement;

    /**
     * This method is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param movement is an int representing the entity (number of island) of the movement of mother nature
     */
    public MoveMotherNatureMessage(String sender, int movement){
        super(sender, Action.MOVEMOTHERNATURE);
        this.movement = movement;
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to move
     * other nature
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws IllegalMoveException is the exception thrown if MoveMotherNature is not a permitted action
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws NoMoreTokensException is the exception thrown if there are no token left
     * @throws NoPlayerException is the exception thrown if a player isn't found
     */
    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoMoreTokensException, NoPlayerException {
        LinkedList<Island> islands = game.getAllIslands();
        int currentMNposition = islands.indexOf(game.getMotherNaturePosition());
        int numIslands = islands.size();

        Player p = game.getPlayer(sender);
        if(movement == 0){
            throw new IllegalMoveException("You have to move Mother Nature!");
        }
        if(movement > (game.getMaximumMNMovement(p) + game.getMotherNatureExtraMovement()))
            throw new IllegalMoveException("You can't move that much Mother Nature!");

        int newMTposition = (currentMNposition + movement) % numIslands;
        game.moveMotherNature(islands.get(newMTposition), true);

        return new Update(null, null, null, null, null, true, game.getFinishedGame(), game.getWinner(), game.getIsLastTurn());
    }

    /**
     * This method return the movement of mother nature
     * @return an int representing the movement of mother nature
     */
    public int getMovement(){
        return movement;
    }
}