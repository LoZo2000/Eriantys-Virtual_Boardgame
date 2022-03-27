package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.WrongNumberOfTowersException;

import java.util.ArrayList;

public class Dashboard{
    private Card graveyard;
    private Canteen canteen;
    private Entrance entrance;
    private final ColorTower color;
    private final int maxTowers;
    private int towers;

    //students will be inizialized when the game start not when the players are created
    public Dashboard(ArrayList<Student> entranceStudents, ColorTower color) {
        this.color = color;
        canteen = new Canteen();
        entrance = new Entrance(entranceStudents);
        //TODO numPlayer as parameter to change maxTowers
        maxTowers = 8;
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

    public void addTowers(int towers) throws WrongNumberOfTowersException {
        if(towers<0 || this.towers+towers > maxTowers) throw new WrongNumberOfTowersException();
        this.towers += towers;
    }

    public void removeTowers(int towers) throws WrongNumberOfTowersException {
        if(towers<0 || this.towers-towers <0) throw new WrongNumberOfTowersException();
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
