package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.*;

public class BlockColorMessage extends ChooseColorMessage{
    public BlockColorMessage(String sender, Color color) {
        super(sender, Action.BLOCK_COLOR, color);
    }

    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoPlayerException {
        if (game.getActiveCard() != -1) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.BLOCK_COLOR)
                throw new IllegalMoveException("Wrong move: you cannot choose a color to be blocked!");
        }

        Player p = game.getPlayer(sender);

        game.disableColor(p, chosenColor);

        return new Update(null, null, null, null, false, null);
    }
}
