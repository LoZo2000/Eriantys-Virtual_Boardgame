package it.polimi.ingsw.model;

import java.io.Serializable;

public class Card implements Serializable {
    private final int priority;
    private final int movement;
    private final String front;
    //private final int back;

    public Card(int priority, int movement){
        this.priority = priority;
        this.movement = movement;
        switch (priority){
            case 1 -> this.front = "/Ass_1.png";
            case 2 -> this.front = "/Ass_2.png";
            case 3 -> this.front = "/Ass_3.png";
            case 4 -> this.front = "/Ass_4.png";
            case 5 -> this.front = "/Ass_5.png";
            case 6 -> this.front = "/Ass_6.png";
            case 7 -> this.front = "/Ass_7.png";
            case 8 -> this.front = "/Ass_8.png";
            case 9 -> this.front = "/Ass_9.png";
            default -> this.front = "/Ass_10.png";
        }
        //this.back=back;
    }

    public int getPriority(){
        return priority;
    }
    public int getMovement(){
        return movement;
    }
    public String getFront(){
        return front;
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
