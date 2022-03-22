package it.polimi.ingsw.model;

public class Card implements Cloneable {
    private final int priority;
    private final int movement;
    //private final int back;

    public Card(int priority, int movement){
        this.priority = priority;
        this.movement = movement;
        //this.back=back;
    }

    public int getInitiative(){
        return priority;
    }
    public int getMovement(){
        return movement;
    }

    public boolean equals(Object o){
        if(o instanceof Card){
            if(((Card)o).getInitiative() == initiative) return true;
        }
        return false;
    }
}
