package it.polimi.ingsw.model;

import java.io.Serializable;

public class Card implements Serializable {
    private final int priority;
    private final int movement;
    private final String front;
    //private final int back;

    /**
     * Creator of the entity Card. It requires the main attributes of the card
     * @param priority is an integer representing the priority of the card
     * @param movement is an integer representing the maximum number of island MT can pass if this
     *                 card is activated
     */
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

    /**
     * Method to get the attribute Priority of this Card
     * @return integer representing Priority
     */
    public int getPriority(){
        return priority;
    }

    /**
     * Method to get the attribute Movement of this Card
     * @return integer representing Movement
     */
    public int getMovement(){
        return movement;
    }

    /**
     * Method to get a String representing the path to retrieve the png front image of the Card
     * @return the String representing the path
     */
    public String getFront(){
        return front;
    }

    /**
     * Method to override Equals: two Cards are equal only if their attributes Priority are equal
     * @param o other Card to be compared with This Card
     * @return true if their attributes Priority are equal, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Card){
            if(((Card)o).getPriority() == priority) return true;
        }
        return false;
    }

    /**
     * Method to override ToString: when we need to print Card on terminal we want to display the main
     * attributes Priority and Movement
     * @return a String representing this Card
     */
    @Override
    public String toString(){
        return "(p:"+priority+", m:"+movement+")";
    }
}
