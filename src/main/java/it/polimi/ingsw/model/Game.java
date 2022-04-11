package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Game implements Serializable {
    private MotherNature motherNature;
    private LinkedList <Island> islands = new LinkedList<>();
    private Cloud[] clouds;
    //private Team[] teams;
    private ArrayList<Player> players = new ArrayList<>();
    private Card[] playedCards;
    private Bag bag;
    private Character[] CharactersCards;
    private Map<Color, Player> professors;
    private Rule currentRule;
    private final int numPlayers;
    private boolean completeRules;
    private int lastPlayed;

    private String currentPlayer = null;
    private Action curretAction = null;



    //Create game but no players are added;
    public Game(boolean completeRules, int numPlayers){
        lastPlayed = 0;
        this.numPlayers=numPlayers;
        this.completeRules=completeRules;
        FactoryBag fb = new FactoryBag();
        bag = fb.getBag();
        Bag initBag = fb.getInitBag();
        initIslands(initBag);
        motherNature = new MotherNature();
        motherNature.movement(islands.get(0));
        try{
            initClouds(numPlayers);
        } catch (Exception e ) {
            e.printStackTrace();
        }

        this.currentRule = new DefaultRule();
        this.professors = new HashMap<>();

        for(Color c: Color.values()){
            this.professors.put(c, null);
        }
    }

    public int getNumberOfStudentPerColorOnCloud(int i, Color color){
        return clouds[i].getNumberOfStudentPerColor(color);
    }

    public int getNumberOfClouds(){
        return clouds.length;
    }

    private void initClouds(int numPlayers) throws NoMoreStudentsException, TooManyStudentsException, StillStudentException {
        switch(numPlayers){
            case 2:
            case 4:
                clouds= new Cloud[numPlayers];
                for(int i=0; i<numPlayers; i++){
                    clouds[i]= new Cloud(3);
                    clouds[i].refillCloud(bag.getRandomStudent(3));
                }
                break;
            case 3:
                clouds= new Cloud[numPlayers];
                for(int i=0; i<numPlayers; i++){
                    clouds[i]= new Cloud(4);
                    clouds[i].refillCloud(bag.getRandomStudent(4));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value of Players: " + numPlayers);
        }
    }

    private void initIslands(Bag initBag){
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

    //TODO TEMPORARY METHOD
    public void addPlayer(Player p){
        this.players.add(p);
    }

    //TODO TEMPORARY METHOD
    public Map<Color, Player> getProfessors(){
        return this.professors;
    }

    public void addPlayer(String nickname, ColorTower color){
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

        Player newPlayer = new Player(nickname, numPlayers, color, entranceStudents);

        players.add(newPlayer);
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public int getRegisteredNumPlayers(){
        return this.players.size();
    }

    public LinkedList<Island> getAllIslands(){
        return (LinkedList<Island>)islands.clone();
    }

    public Island getMotherNaturePosition(){
        return motherNature.getPosition();
    }

    //TODO: new Exception added!!!!!
    public Island getIsland(int id) throws NoIslandException{
        for(Island i : islands){
            if(i.getId() == id) return i;
        }
        throw new NoIslandException();
    }

    //TODO: new Exception added!!!!!
    public Player getPlayer(String nickName) throws NoPlayerException{
        for(Player p : players){
            if(p.getNickname().equals(nickName)) return p;
        }
        throw new NoPlayerException();
    }

    public ArrayList<Player> getAllPlayers(){
        return (ArrayList<Player>) players.clone();
    }

    public void moveMotherNature(Island island){
        motherNature.movement(island);
        Report report = island.getReport();

        ColorTower higherInfluence = influence(report);

        if(higherInfluence != report.getOwner()){
            island.conquest(higherInfluence);
            //TODO Necessario passare per Team
            for(Player p: this.players){
                if(p.getColor() == report.getOwner()){
                    p.getDashboard().addTowers(report.getTowerNumbers());
                }
                if(p.getColor() == higherInfluence){
                    p.getDashboard().removeTowers(report.getTowerNumbers());
                }
            }

            int islandNumber = this.islands.indexOf(island);
            int previousPosition = (islandNumber - 1) % this.islands.size();
            if(previousPosition<0) previousPosition += this.islands.size();

            if(this.islands.get(previousPosition).getOwner() == island.getOwner()){
                try {
                    mergeIsland(this.islands.get(previousPosition), island);
                } catch (NoContiguousIslandException e) {
                    e.printStackTrace();
                }
            }

            islandNumber = this.islands.indexOf(island);
            int successivePosition = (islandNumber + 1) % this.islands.size();

            if(this.islands.get(successivePosition).getOwner() == island.getOwner()){
                try {
                    mergeIsland(island, this.islands.get(successivePosition));
                } catch (NoContiguousIslandException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void mergeIsland(Island i1, Island i2) throws NoContiguousIslandException {
        int indx1=islands.indexOf(i1);
        int indx2=islands.indexOf(i2);
        if(indx1-indx2==1 || indx1-indx2==-1 || (indx1==0 && indx2== islands.size()-1) || (indx1== islands.size()-1 && indx2==0)){
            Island temp= new Island(i1, i2);
            islands.remove(i1);
            islands.remove(i2);
            islands.add(indx1, temp);
            motherNature.movement(temp);
        }
        else throw new NoContiguousIslandException("Islands are not Contiguous");
    }

    //MOVE FUNCTIONS
    //function move written in a view where the parameters are message received by a client (temporary)
    //TODO: new Exception added!!!!!
    public void moveStudent(int studentId, Movable arrival, Movable departure) throws NoSuchStudentException{
        Student s = departure.removeStudent(studentId);
        arrival.addStudent(s);

        this.updateProfessors();
    }

    private ColorTower influence(Report report){
        //TODO Costruzione Mappa Professori da quella con Player

        //Map with the information about the Color of the professor and the Color of the Tower of the player or team who
        //has the professor
        HashMap<Color, ColorTower> profAndPlayer = new HashMap<>();
        for(Color c: Color.values()){
            Player owner = this.professors.get(c);
            if(owner == null)
                profAndPlayer.put(c, null);
            else
                profAndPlayer.put(c, owner.getColor());
        }

        return this.currentRule.calculateInfluence(report, profAndPlayer);
    }

    private void updateProfessors(){
        for(Color c: Color.values()){
            HashMap<String, Integer> counterCanteen = new HashMap<>();
            for(Player p: this.players){
                int numberStudents = p.getDashboard().getCanteen().getNumberStudentColor(c);
                counterCanteen.put(p.getNickname(), numberStudents);
            }
            Player currentOwner = this.professors.get(c);

            String currentOwnerNickname;
            if(currentOwner == null){
                currentOwnerNickname = null;
            } else{
                currentOwnerNickname = currentOwner.getNickname();
            }

            String newOwnerNickname = this.currentRule.updateProfessor(currentOwnerNickname, counterCanteen);

            Player newOwner = this.players.stream()
                    .filter(player -> player.getNickname().equals(newOwnerNickname))
                    .findAny()
                    .orElse(null);

            this.professors.put(c, newOwner);
        }
    }

    public void refillClouds() throws NoMoreStudentsException, TooManyStudentsException, StillStudentException {
        for (int i=0; i<clouds.length; i++){
            if (numPlayers==3) clouds[i].refillCloud(bag.getRandomStudent(4));
            else clouds[i].refillCloud(bag.getRandomStudent(3));
        }
    }

    //TODO: new Exception added!!!!!
    public void selectCloud(String playerNick, Cloud cloud) throws NoPlayerException{
        ArrayList<Student> students = cloud.chooseCloud();
        for (Student s : students) getPlayer(playerNick).getDashboard().getEntrance().addStudent(s);
    }

    public Cloud[] getAllClouds(){
        return clouds;
    }

    public void setCurrentPlayer(String nickname){
        currentPlayer = nickname;
    }
    public void setCurrentAction(Action action){
        curretAction = action;
    }
    public String getCurrentPlayer(){
        return currentPlayer;
    }
    public Action getCurretAction(){
        return curretAction;
    }
}