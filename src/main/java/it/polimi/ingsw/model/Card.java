package it.polimi.ingsw.model;

import java.io.Serializable;

public class Card implements Serializable {
    private final int priority;
    private final int movement;
    //private final int back;

    public Card(int priority, int movement){
        this.priority = priority;
        this.movement = movement;
        //this.back=back;
    }

    public int getPriority(){
        return priority;
    }
    public int getMovement(){
        return movement;
    }

    public boolean equals(Object o){
        if(o instanceof Card){
            if(((Card)o).getPriority() == priority) return true;
        }
        return false;
    }

    public String toString(){
        return "(p:"+priority+", m:"+movement+")";
    }
}
