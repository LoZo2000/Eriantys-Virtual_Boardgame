package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

public class PutBackMessage extends ChooseColorMessage{
    public PutBackMessage(String sender, Color color) {
        super(sender, Action.PUT_BACK, color);
    }

    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoSuchStudentException {
        if (game.getActiveCard() != -1) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.PUT_BACK)
                throw new IllegalMoveException("Wrong move: you cannot put back students of a color now!");
        }

        game.putBackInBag(chosenColor);

        return new Update(null, null, null, null, false, null, game.getFinishedGame(), game.getWinner(), game.getIsLastTurn());
    }
}
