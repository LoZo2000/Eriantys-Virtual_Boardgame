package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player implements Cloneable {
    final private Hand hand;
    final private Dashboard dashboard;
    final private String nickname;

    public Player getPlayer(){
        try {
            return (Player)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Player(String nickname, ArrayList<Student> entranceStudents) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.dashboard = new Dashboard(entranceStudents);
    }

    public Hand getHand(){
        return hand.getHand();
    }

    public Dashboard getDashboard(){
        return dashboard.getDashboard();
    }

    public String getNickname(){
        return new String(nickname);
    }
}
