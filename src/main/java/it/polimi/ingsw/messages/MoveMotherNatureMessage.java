package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.*;

import java.util.LinkedList;

public class MoveMotherNatureMessage extends Message{
    final private int movement;

    public MoveMotherNatureMessage(String sender, int movement){
        super(sender, Action.MOVEMOTHERNATURE);
        this.movement = movement;
    }

    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, EndGameException, NoMoreTokensException, NoPlayerException {
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

        return new Update(null, null, null, null, null, true);
    }

    public int getMovement(){
        return movement;
    }
}