package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class SELECTCLOUDmessage extends  Message{
    final private int cloudPosition;

    public SELECTCLOUDmessage(String sender, Action action, int cloudPosition){
        super(sender, action);
        this.cloudPosition = cloudPosition;
    }

    public int getCloudPosition(){
        return cloudPosition;
    }
}