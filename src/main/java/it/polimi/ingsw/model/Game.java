package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;

import it.polimi.ingsw.model.exceptions.NoContiguousIslandException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class Game {
    private MotherNature motherNature;
    LinkedList <Island> islands = new LinkedList<>();
    private Cloud[] clouds;
    //private Team[] teams;
    private ArrayList<Player> players = new ArrayList<>();
    private Card[] playedCards;
    private Bag bag;
    private Bag initBag;
    private Character[] CharactersCards;
    private Map<Color, Player> professors;
    private Rule currentRule;
    private final int numPlayers;
    private boolean completeRules;
    private int lastPlayed;



    //Create game but no players are added;
    public Game(boolean completeRules, int numPlayers){
        lastPlayed = 0;
        this.numPlayers=numPlayers;
        this.completeRules=completeRules;
        FactoryBag fb = new FactoryBag();
        bag = fb.getBag();
        initBag = fb.getInitBag();
        initIslands();
        motherNature = new MotherNature();
        motherNature.movement(islands.get(0));
        clouds = new Cloud[3];
    }

    private void initIslands(){
        for(int i=0; i<12; i++){
            islands.add(new Island(i));
            if(i!=0 && i!=6) {
                try {
                    islands.get(i).addStudent(initBag.getRandomStudent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addPlayer(String nickname){
        int numberOfStudents = this.numPlayers != 3 ? 7 : 9;
        ArrayList<Student> entranceStudents = new ArrayList<>();
        for(int i= 0; i<numberOfStudents; i++){
            try {
                Student s = this.bag.getRandomStudent();
                entranceStudents.add(s);
            } catch (NoMoreStudentsException e) {
                e.printStackTrace();
            }
        }

        Player newPlayer = new Player(nickname, entranceStudents);

        players.add(newPlayer);
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
        return players.get(i);
    }

    public void setLastPlayed(int lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public LinkedList<Island> getAllIslands(){
        return (LinkedList<Island>)islands.clone();
    }

    public Island getMotherNaturePosition(){
        return motherNature.getPosition();
    }

    public Island getIsland(int id){
        for(Island i : islands){
            if(i.getId() == id) return i;
        }
        return null;
    }

    public Player getPlayer(String nickName){
        for(Player p : players){
            if(p.getNickname().equals(nickName)) return p;
        }
        return null;
    }

    public void moveMotherNature(Island island){
        motherNature.movement(island);
    }

    public void mergeIsland(Island i1, Island i2) throws NoContiguousIslandException {
        int indx1=islands.indexOf(i1);
        int indx2=islands.indexOf(i2);
        if(indx1-indx2==1 || indx1-indx2==-1 || (indx1==0 && indx2== islands.size()-1) || (indx1== islands.size()-1 && indx2==0)){
            Island temp= new Island(i1, i2);
            islands.remove(i1);
            islands.remove(i2);
            islands.add(indx1, temp);
        }
        else throw new NoContiguousIslandException("Islands are not Contiguous");
    }
}