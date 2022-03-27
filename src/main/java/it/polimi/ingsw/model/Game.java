package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;

import it.polimi.ingsw.model.exceptions.NoContiguousIslandException;
import it.polimi.ingsw.model.exceptions.StillStudentException;
import it.polimi.ingsw.model.exceptions.TooManyStudentsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Game {
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

    public int getNumberOfStudentPerColor(int i, Color color){
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

    /*public int getGameState(){  // return 1 if the game is full, 2 if can contain further player
        if (players.size() == numPlayers) {
            return 1;
        }
        else return 2;
    }*/

    /*public int getNumPlayers(){
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
    }*/

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
        Report report = island.getReport();

        ColorTower higherInfluence = influence(report);

        if(higherInfluence != report.getOwner()){
            island.conquest(higherInfluence);
            //TODO Necesario passare per Team
            for(Player p: this.players){
                if(p.getColor() == report.getOwner()){
                    try {
                        p.getDashboard().addTowers(report.getTowerNumbers());
                    } catch (WrongNumberOfTowersException e) {
                        e.printStackTrace();
                    }
                }
                if(p.getColor() == higherInfluence){
                    try {
                        p.getDashboard().removeTowers(report.getTowerNumbers());
                    } catch (WrongNumberOfTowersException e) {
                        e.printStackTrace();
                    }
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
                    mergeIsland(this.islands.get(successivePosition), island);
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
        }
        else throw new NoContiguousIslandException("Islands are not Contiguous");
    }

    // this method is temporary because i have to discuss with others about movement
    public void takeStudentsFromIsland(int CloudNumber, String nickName){
        Entrance to = getPlayer(nickName).getDashboard().getEntrance();
        ArrayList<Student> from = clouds[CloudNumber].chooseCloud();
        for (Student s : from){
            to.addStudent(s);
        }
    }

    //MOVE FUNCTIONS
    //function move written in a view where the parameters are message received by a client (temporary)
    public void moveStudent(int studentId, Movable arrival, Movable departure){
        Student s;
        try{
            s = departure.removeStudent(studentId);
            arrival.addStudent(s);

            this.updateProfessors();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*public void moveStudent(int from, int to, String playerNick, int studentId, int islandId){
        //Locations: 0=entrance, 1=canteen, 2=island
        Movable departure;
        Movable arrival;
        switch(from){
            case 0:
                departure = getPlayer(playerNick).getDashboard().getEntrance();
                break;
            case 1:
                departure = getPlayer(playerNick).getDashboard().getCanteen();
                break;
            case 2:

                departure = getIsland(islandId);
                break;
            default:
                departure = null;
                System.out.println("Something has gone wrong...");
        }
        switch(to) {
            case 0:
                arrival = getPlayer(playerNick).getDashboard().getEntrance();
                break;
            case 1:
                arrival = getPlayer(playerNick).getDashboard().getCanteen();
                break;
            case 2:
                arrival = getIsland(islandId);
                break;
            default:
                arrival = null;
                System.out.println("Something has gone wrong...");
        }
        Student temp;
        try{
            temp = departure.removeStudent(studentId);
            arrival.addStudent(temp);

            this.updateProfessors();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void moveStudent(int from, int to, String playerNick, int studentId){
        //Locations: 0=entrance, 1=canteen, 2=island
        Movable departure;
        Movable arrival;
        switch(from){
            case 0:
                departure = getPlayer(playerNick).getDashboard().getEntrance();
                break;
            case 1:
                departure = getPlayer(playerNick).getDashboard().getCanteen();
                break;
            case 2:
                departure = null;
                System.out.println("Insert the Island id");
                break;
            default:
                departure = null;
                System.out.println("Something has gone wrong...");
        }
        switch(to) {
            case 0:
                arrival = getPlayer(playerNick).getDashboard().getEntrance();
                break;
            case 1:
                arrival = getPlayer(playerNick).getDashboard().getCanteen();
                break;
            case 2:
                arrival = null;
                System.out.println("Insert the Island id");
                break;
            default:
                arrival = null;
                System.out.println("Something has gone wrong...");
        }
        Student temp;
        try{
            temp = departure.removeStudent(studentId);
            arrival.addStudent(temp);

            this.updateProfessors();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void moveStudent(int from, int to, String playerNick, int[] studentId, int islandId){
        for (int i=0; i<studentId.length; i++){
            moveStudent(from, to, playerNick, studentId[i],  islandId);
        }
    }

    public void moveStudent(int from, int to, String playerNick, int[] studentId){
        for (int i=0; i<studentId.length; i++){
            moveStudent(from, to, playerNick, studentId[i]);
        }
    }*/

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

    public void selectCloud(String playerNick, Cloud cloud){
        ArrayList<Student> students = cloud.chooseCloud();
        for(Student s : students) getPlayer(playerNick).getDashboard().getEntrance().addStudent(s);
    }

    public Cloud[] getAllClouds(){
        return clouds;
    }
}