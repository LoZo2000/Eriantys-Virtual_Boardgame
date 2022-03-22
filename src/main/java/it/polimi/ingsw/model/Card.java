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

    public Card getCard(){
        try {
            return (Card)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
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
}
