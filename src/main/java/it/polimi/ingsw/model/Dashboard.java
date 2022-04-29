package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Dashboard{
    private Card graveyard;
    private Canteen canteen;
    private Entrance entrance;
    private final ColorTower color;
    private final int maxTowers;
    private int towers;

    //students will be inizialized when the game start not when the players are created
    public Dashboard(int numPlayers, ArrayList<Student> entranceStudents, ColorTower color) {
        this.color = color;
        canteen = new Canteen();
        entrance = new Entrance(entranceStudents);
        maxTowers = numPlayers != 3 ? 8 : 6;
        towers = maxTowers;
    }

    public Canteen getCanteen(){
        return canteen;
    }

    public Entrance getEntrance(){
        return entrance;
    }

    public int getTowers(){
        return towers;
    }

    public void addTowers(int towers){
        this.towers += towers;
    }

    public void removeTowers(int towers){
        this.towers -= towers;
    }

    public Card getGraveyard(){
        return graveyard;
    }

    public void setGraveyard(Card c){
        graveyard = c;
    }

    public ColorTower getColor(){
        return this.color;
    }
}
