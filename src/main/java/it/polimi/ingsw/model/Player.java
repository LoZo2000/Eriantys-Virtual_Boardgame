package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Comparable<Player> {

    final private Hand hand;
    final private Dashboard dashboard;
    final private String nickname;
    private int coins;

    public Player(String nickname, int numPlayers, ColorTower color, ArrayList<Student> entranceStudents) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.dashboard = new Dashboard(numPlayers, entranceStudents, color);
        //TODO IT'S 1 (DEBUG)
        this.coins = 1;
    }

    public Hand getHand(){
        return hand;
    }

    public ColorTower getColor(){
        return this.dashboard.getColor();
    }

    public Dashboard getDashboard(){
        return dashboard;
    }

    public String getNickname(){
        return new String(nickname);
    }

    public void giveCoin(){
        this.coins += 1;
    }

    public void useCoins(int usedCoins) throws NotEnoughMoneyException{
        if(this.coins < usedCoins){
            throw new NotEnoughMoneyException("You don't have enough money!");
        }
        this.coins -= usedCoins;
    }

    //TODO Test Method
    public int getCoins(){
        return this.coins;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }

        if(!(obj instanceof Player p)){
            return false;
        }

        return this.nickname.equals(p.nickname);
    }

    public String toString(){
        return nickname;
    }

    @Override
    public int compareTo(Player p) {
        Card c1 = this.getDashboard().getGraveyard();
        Card c2 = p.getDashboard().getGraveyard();

        if(c1.getPriority() < c2.getPriority()){
            return -1;
        } else{
            return 1;
        }
    }
}
