package it.polimi.ingsw.model;

public class Player {
    Hand hand;
    Dashboard dashboard;
    final String Nickname;


    public Player(String nickname, Dashboard dashboard) {
        this.Nickname = nickname;
        this.dashboard = dashboard;
        this.hand = new Hand();
    }
}
