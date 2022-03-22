package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Dashboard implements Cloneable{
    Card graveyard;
    Canteen canteen;
    Entrance entrance;
    int towers;

    //students will be inizialized when the game start not when the players are created
    public Dashboard(ArrayList<Student> entranceStudents) {
        canteen = new Canteen();
        entrance = new Entrance(entranceStudents);
        this.towers = 8;
    }

    public Dashboard getDashboard(){
        try {
            return (Dashboard) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Canteen getCanteen(){
        return canteen.getCanteen();
    }

    public Entrance getEntrance(){
        return entrance.getEntrance();
    }

    public void addTower(int t){
        towers += t;
    }

    public void removeTower(int t){
        towers -= t;
    }

    public Card getGraveyard(){
        return graveyard.getCard();
    }

    public void setGraveyard(Card c){
        graveyard = c;
    }
}
