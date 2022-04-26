package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class EndGameMessage extends Message{

    public EndGameMessage(String sender, Action action) {
        super(sender, action);
    }

}
