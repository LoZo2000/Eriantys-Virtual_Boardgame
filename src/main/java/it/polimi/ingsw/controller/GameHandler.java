package it.polimi.ingsw.controller;

import  it.polimi.ingsw.model.*;

public class GameHandler {
    private final Game game;

    public GameHandler(Game game) {
        this.game= game;
    }


    public void PreTurn(){
        int first = game.getLastPlayed();
        int numPlayers= game.getNumPlayers();
        for(int i=0; i<numPlayers; i++){
            //tutti i metodi non ritornano nulla in quanto sono da implementare
            ThrowCard(game.getPlayerNum(i));
            WaitForThrowCard(game.getPlayerNum(i+1));
            WaitForThrowCard(game.getPlayerNum(i+2));

            WaitForUserCard();

            SaveCard();

            WaitForThrowCard(game.getPlayerNum(i));
            SendCard(game.getPlayerNum(i+1));
            SendCard(game.getPlayerNum(i+2));

        }
        WhoPlays();

    }
    //da implementare
    private void ThrowCard(Player player){}

    private void WaitForThrowCard(Player player){}

    private void WaitForUserCard(){}

    private void SendCard(Player player){}

    private void SaveCard(){}

    private void WhoPlays(){}

}
