package it.polimi.ingsw.model;

public class Player {
    Hand hand;
    Dashboard dashboard;
    final String nickname;


    public Player(String nickname, Dashboard dashboard) {
        this.nickname = nickname;
        this.dashboard = dashboard;
        this.hand = new Hand();
    }

    public Player(Player player) {
        this.nickname = player.nickname;
        this.dashboard = player.dashboard;
        this.hand = new Hand(); // da creare costruttore che duplica
    }
}
