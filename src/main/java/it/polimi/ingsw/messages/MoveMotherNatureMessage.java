package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class MoveMotherNatureMessage extends Message{
    final private int movement;

    public MoveMotherNatureMessage(String sender, Action action, int movement){
        super(sender, action);
        this.movement = movement;
    }

    public int getMovement(){
        return movement;
    }
}