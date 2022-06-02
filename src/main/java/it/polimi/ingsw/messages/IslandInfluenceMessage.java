package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.exceptions.*;

public class IslandInfluenceMessage extends ChooseIslandMessage{

    public IslandInfluenceMessage(String sender, int idIsland) {
        super(sender, Action.ISLAND_INFLUENCE, idIsland);
    }

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

        return new Update(null, null, null, null, false, null, game.getFinishedGame(), game.getWinner(), game.getIsLastTurn());
    }
}