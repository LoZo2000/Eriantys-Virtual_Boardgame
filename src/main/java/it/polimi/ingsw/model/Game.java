package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class Game {
    private boolean gameType;
    private int lastPlayed;
    private final int numPlayers;
    LinkedList <Island> islands = new LinkedList<>();
    private MotherNature motherNature;
    private ArrayList<Player> players = new ArrayList<>();
    private Cloud[] clouds;
    //private Team[] teams;
    //private Player[] players;
    private Card[] playedCards;
    private Bag bag;
    private Bag initBag;
    private Character[] CharactersCards;
    private Map<Color, Player> professors;
    private Rule currentRule;


    //Create game but no players are added;
    public Game(boolean gameType, int numPlayers){
        this.numPlayers=numPlayers;
        this.gameType=gameType;
        lastPlayed=0;
        initIslands();
        motherNature= new MotherNature();
        clouds = new Cloud[3];
        FactoryBag fb = new FactoryBag();
        bag = fb.getBag();
        initBag = fb.getInitBag();
    }

    private void initIslands(){
        for(int i=0; i<12; i++){
            islands.add(new Island(i));
        }
    }

    public addPlayer(String NickName){
        players.add(new Player(NickName));
    }




    //create player, dashboard and hand (hand not inizialized)
   // private Player createPlayer(String nickName) {
     //   Dashboard dashboard = new Dashboard(numPlayers, players.size());
       // return new Player (nickName, dashboard);
    //}

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

    public void setLastPlayed(int lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
}