package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player{
    final private Hand hand;
    final private Dashboard dashboard;
    final private String nickname;

    public Player(String nickname, ArrayList<Student> entranceStudents) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.dashboard = new Dashboard(entranceStudents);
    }

    public Hand getHand(){
        return hand;
    }

    public Dashboard getDashboard(){
        return dashboard;
    }

    public String getNickname(){
        return new String(nickname);
    }
}
