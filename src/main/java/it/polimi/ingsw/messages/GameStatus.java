package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Student;
import java.io.Serializable;
import java.util.List;

public class GameStatus implements Serializable{
    private String error = ""; //In case something goes wrong, Controller will send a GameStatus containing only this String
    private String turnOf;
    private Phase currentPhase;

    private String myId;
    private ColorTower myColor;
    private int myTowers;
    private List<Student> studentsInEntrance;
    private List<List<Student>> studentsInCanteen;
    private List<Card> myCards;

    private List<String> enemiesNames;
    private List<ColorTower> enemiesColors;
    private List<Integer> enemiesTowers;
    private List<List<Integer>> enemiesEntrances;
    private List<List<Integer>> enemiesCanteen;

    private List<Integer> islandsId;
    private List<ColorTower> owners;
    private List<Integer> numTowers;
    private List<List<Integer>> studentsOnIsland;
    private int MN;
    private List<String> professorsOwners;
    private List<List<Integer>> studentsOnCloud;
    Card myLastCard;
    List<Card> enemiesLastCards;



    //Only if an error is detected
    public GameStatus(String myId, String error, String turnOf){
        this.myId = myId;
        this.error = error;
        this.turnOf = turnOf;
    }



    //Default creator:
    public GameStatus(String turnOf, Phase currentPhase, String myId, ColorTower myColor, int myTowers, List<Student> studentsInEntrance, List<List<Student>> studentsInCanteen, List<Card> myCards, List<String> enemiesNames, List<ColorTower> enemiesColors, List<Integer> enemiesTowers, List<List<Integer>> enemiesEntrances, List<List<Integer>> enemiesCanteen, List<Integer> islandsId, List<ColorTower> owners, List<Integer> numTowers, List<List<Integer>> studentsOnIsland, int MN, List<String> professorsOwners, List<List<Integer>> studentsOnCloud, Card myLastCard, List<Card> enemiesLastCards){
        this.turnOf = turnOf;
        this.currentPhase = currentPhase;

        this.myId = myId;
        this.myColor = myColor;
        this.myTowers = myTowers;
        this.studentsInEntrance = studentsInEntrance;
        this.studentsInCanteen = studentsInCanteen;
        this.myCards = myCards;

        this.enemiesNames = enemiesNames;
        this.enemiesColors = enemiesColors;
        this.enemiesTowers = enemiesTowers;
        this.enemiesEntrances = enemiesEntrances;
        this.enemiesCanteen = enemiesCanteen;

        this.islandsId = islandsId;
        this.owners = owners;
        this. numTowers = numTowers;
        this.studentsOnIsland = studentsOnIsland;
        this.MN = MN;
        this.professorsOwners = professorsOwners;
        this.studentsOnCloud = studentsOnCloud;

        this.myLastCard = myLastCard;
        this.enemiesLastCards = enemiesLastCards;
    }

    public String getError(){
        return error;
    }
    public String getTurnOf(){
        return turnOf;
    }
    public Phase getCurrentPhase(){
        return currentPhase;
    }
    public String getMyId(){
        return myId;
    }
    public ColorTower getMyColor(){
        return myColor;
    }
    public int getMyTowers(){
        return myTowers;
    }
    public List<Student> getStudentsInEntrance(){
        return studentsInEntrance;
    }
    public List<List<Student>> getStudentsInCanteen(){
        return studentsInCanteen;
    }
    public List<Card> getMyCards(){
        return myCards;
    }
    public List<String> getEnemiesNames(){
        return enemiesNames;
    }
    public List<ColorTower> getEnemiesColors(){
        return enemiesColors;
    }
    public List<Integer> getEnemiesTowers(){
        return enemiesTowers;
    }
    public List<List<Integer>> getEnemiesEntrances(){
        return enemiesEntrances;
    }
    public List<List<Integer>> getEnemiesCanteen(){
        return enemiesCanteen;
    }
    public List<Integer> getIslandsId(){
        return islandsId;
    }
    public List<ColorTower> getOwners(){
        return owners;
    }
    public List<Integer> getNumTowers(){
        return numTowers;
    }
    public List<List<Integer>> getStudentsOnIsland(){
        return studentsOnIsland;
    }
    public int getMN(){
        return MN;
    }
    public List<String> getProfessorsOwners(){
        return professorsOwners;
    }
    public List<List<Integer>> getStudentsOnCloud(){
        return studentsOnCloud;
    }
    public Card getMyLastCard(){
        return myLastCard;
    }
    public List<Card> getEnemiesLastCards(){
        return enemiesLastCards;
    }
}