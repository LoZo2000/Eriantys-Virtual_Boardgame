package it.polimi.ingsw.model;


import java.util.ArrayList;

public class Game {
    private int lastPlayed;
    private final int numPlayers;
    ArrayList <Island> islands = new ArrayList<>();
    MotherNature motherNature;
    private ArrayList<Player> players = new ArrayList<Player>();
    Cloud[] clouds;
    Card[] playedCards;

    //Create game and add the first player
    public Game(String Gametype, int numPlayers, String nickFirstPlayer){
        this.numPlayers=numPlayers;
        this.addPlayer(nickFirstPlayer);
        lastPlayed=0;
    }

    //create player, dashboard and hand (hand not inizialized)
    private Player createPlayer(String nickName) {
        Dashboard dashboard = new Dashboard(numPlayers, players.size());
        return new Player (nickName, dashboard);
    }

    public void addPlayer(String nickName){
        if(players.size()>numPlayers) ; //throw TooManyPlayersException;
        else{
            players.add(createPlayer(nickName));
        }

    }

    public int getGameState(){  // return 1 if the game is full, 2 if can contain further player
        if (players.size() == numPlayers) {
            return 1;
        }
        else return 2;
    }

    public int getNumPlayers(){
        return numPlayers;
    }

    public int getLastPlayed(){
        return lastPlayed;
    }

    public Player getPlayerNum(int i){
        Player temp = new Player(players.get(i%numPlayers));
        return temp;
    }




}