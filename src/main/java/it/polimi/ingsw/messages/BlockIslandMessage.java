package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;
import it.polimi.ingsw.model.exceptions.NoIslandException;
import it.polimi.ingsw.model.exceptions.NoMoreTokensException;

public class BlockIslandMessage extends ChooseIslandMessage{

    public BlockIslandMessage(String sender, int idIsland) {
        super(sender, Action.BLOCK_ISLAND, idIsland);
    }

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

        return new Update(null, null, null, null, false, null);
    }

}
