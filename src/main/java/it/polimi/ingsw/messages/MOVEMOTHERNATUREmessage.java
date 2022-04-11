package it.polimi.ingsw.messages;

import it.polimi.ingsw.model.Action;

public class MOVEMOTHERNATUREmessage extends Message{
    final private int movement;

    public MOVEMOTHERNATUREmessage(String sender, Action action, int movement){
        super(sender, action);
        this.movement = movement;
    }

    public int getMovement(){
        return movement;
    }
}