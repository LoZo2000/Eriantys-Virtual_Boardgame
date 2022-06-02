package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

public class PingMessage extends Message{

    public PingMessage() {
        super(null, Action.PING);
    }

    @Override
    public Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoPlayerException, NoIslandException, NoMoreTokensException, NotEnoughMoneyException, NoCharacterSelectedException, NoSuchStudentException, CannotAddStudentException {
        return null;
    }
}
