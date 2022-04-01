package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class PLAYCARDmessage extends Message{
    final private int priority;

    public PLAYCARDmessage(String sender, Action action, int priority){
        super(sender, action);
        this.priority = priority;
    }

    public int getPriority(){
        return priority;
    }
}
