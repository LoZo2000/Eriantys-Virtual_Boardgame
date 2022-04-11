package it.polimi.ingsw.messages;

import it.polimi.ingsw.model.Action;

public class ADDMEmessage extends Message{

    public ADDMEmessage(String sender, Action action){
        super(sender, action);
    }
}
