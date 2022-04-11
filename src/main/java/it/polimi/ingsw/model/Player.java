package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    final private Hand hand;
    final private Dashboard dashboard;
    final private String nickname;

    public Player(String nickname, int numPlayers, ColorTower color, ArrayList<Student> entranceStudents) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.dashboard = new Dashboard(numPlayers, entranceStudents, color);
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
}
