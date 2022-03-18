package it.polimi.ingsw.model;

public class Card {
    private int initiative;
    private int movement;

    public Card(int initiative, int movement){
        this.initiative = initiative;
        this.movement = movement;
    }

    public int getInitiative(){
        return initiative;
    }
    public int getMovement(){
        return movement;
    }
}
