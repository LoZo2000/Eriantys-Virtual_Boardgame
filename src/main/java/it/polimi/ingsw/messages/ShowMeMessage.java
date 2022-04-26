package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class ShowMeMessage extends  Message{

    public ShowMeMessage(String sender, Action action){
        super(sender, action);
    }
}