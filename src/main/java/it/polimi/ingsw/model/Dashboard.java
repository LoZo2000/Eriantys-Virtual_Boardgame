package it.polimi.ingsw.model;

public class Dashboard implements Cloneable{
    Card graveyard;
    Canteen canteen;
    Entrance entrance;
    int towers;

    //students will be inizialized when the game start not when the players are created
    public Dashboard(int towers) {
        canteen = new Canteen();
        entrance = new Entrance();
        this.towers = towers;
    }

    public Canteen getCanteen(){
        return (Canteen)canteen.clone();
    }

    public Entrance getEntrance(){
        return (Entrance)entrance.clone();
    }

    public void addTower(int t){
        towers += t;
    }

    public void removeTower(int t){
        towers -= t;
    }

    public Card getGraveyard(){
        return (Card)graveyard.clone();
    }

    public void setGraveyard(Card c){
        graveyard = c;
    }
}
