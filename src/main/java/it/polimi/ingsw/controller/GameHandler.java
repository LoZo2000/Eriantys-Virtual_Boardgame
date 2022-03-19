package it.polimi.ingsw.controller;

import  it.polimi.ingsw.model.*;

public class GameHandler {
    private boolean CharacterPlayed;
    private final Game game;
    private final int numPlayers;
    private int currentPlayer;

    public GameHandler(Game game) {
        this.game= game;
        this.numPlayers= game.getNumPlayers();
        CharacterPlayed=false;
    }

    public void Turn(){
        PreTurn();
        MiddleTurn();
        //EndTurn();
    }


    private void PreTurn(){
        int first = game.getLastPlayed();
        for(int i=0; i<numPlayers; i++){
            //tutti i metodi non ritornano nulla in quanto sono da implementare
            ThrowCard(game.getPlayerNum(i));
            for(int j=1; j<(numPlayers); j++) {
                WaitForThrownCard(game.getPlayerNum(i + j));
            }

            WaitForUserCard();

            SaveCard();

            WaitForThrownCard(game.getPlayerNum(i));
            for(int j=1; j<(numPlayers); j++) {
                SendCard(game.getPlayerNum(i+1));
            }

        }
        currentPlayer=WhoPlays();
        game.setLastPlayed(currentPlayer);
    }

    private void MiddleTurn(){
        for(int i=0; i<numPlayers; i++){
            StartTurn(game.getPlayerNum(currentPlayer));
            for(int j=1; j<(numPlayers); j++) {
                WaitTurn(game.getPlayerNum(currentPlayer+j));
            }

            WaitStudents();

            for(int j=1; j<(numPlayers); j++) {
                NotifyStudent(game.getPlayerNum(currentPlayer+j));
            }

            WaitMotherNature();

            for(int j=1; j<(numPlayers); j++) {
                NotifyMotherNature(game.getPlayerNum(currentPlayer+j));
            }

            WaitCloud();

            for(int j=1; j<(numPlayers); j++) {
                NotifyCloud(game.getPlayerNum(currentPlayer+j));
            }
            currentPlayer++;
        }
    }

    //da implementare
    private void ThrowCard(Player player){}

    private void WaitForThrownCard(Player player){}

    private void WaitForUserCard(){}

    private void SendCard(Player player){}

    private void SaveCard(){}

    private int WhoPlays(){}

    private void StartTurn(Player currentPlayer){}

    private void WaitTurn(Player nonCurrentPlayer){}

    private void WaitStudents(){}

    private void NotifyStudent(Player player){}

    private void WaitMotherNature(){}

    private void NotifyMotherNature(Player player){}

    private void WaitCloud(){}

    private void NotifyCloud(Player player){}

}
