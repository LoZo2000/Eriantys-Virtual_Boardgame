package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class SelectCloudMessage extends  Message{
    final private int cloudPosition;

    public SelectCloudMessage(String sender, Action action, int cloudPosition){
        super(sender, action);
        this.cloudPosition = cloudPosition;
    }

    public int getCloudPosition(){
        return cloudPosition;
    }
}