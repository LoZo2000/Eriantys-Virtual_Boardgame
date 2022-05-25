package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

import java.io.Serializable;

public abstract class Message implements Serializable {
    final protected String sender;
    final protected Action action;

    public Message(String sender, Action action){
        this.sender = sender;
        this.action = action;
    }

    public abstract Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoPlayerException, NoIslandException, EndGameException, NoMoreTokensException, NotEnoughMoneyException, NoCharacterSelectedException, NoSuchStudentException, CannotAddStudentException;

    public String getSender(){
        return sender;
    }

    public Action getAction(){
        return action;
    }
}
