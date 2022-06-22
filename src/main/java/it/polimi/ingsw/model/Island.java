package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IllegalMoveException;

import java.io.Serializable;
import java.util.*;

/**
 * The class island represent an island in the game, in the island can be stored students, and can be present a different
 * number of towers, an island ccan have an owner if a player conquer it through the rule game
 */
public class Island implements Movable, Serializable {
    private final String id;
    private ArrayList<Student> students;
    private boolean prohibitionToken;
    private int maxTowers;
    private ColorTower owner;
    private String sprite;

    /**
     * Creator of the entity Island
     * @param id is the identification number (the first nickname) of the island
     */
    public Island(int id){
        //this.id = id;
        this.id = String.format("%02d", id);
        students = new ArrayList<>();
        maxTowers = 1;
        prohibitionToken = false;
        this.owner = null;

        int rand = 1+(int)(Math.random()*3);
        if(rand==1) sprite = "/Island_1.png";
        else if(rand==2) sprite = "/Island_2.png";
        else sprite = "/Island_3.png";
    }

    /**
     * Creator of a new Island merging two others
     * @param i1 is the first Island to be merged
     * @param i2 is the second Island to be merged
     */
    public Island(Island i1, Island i2){
        students = new ArrayList<>();
        sprite = i1.getSprite();
        String s = i1.id + ", " + i2.id;

        String[] arr = s.split(", ");

        this.id = Arrays.stream(arr)
                .map(Integer::parseInt)
                .sorted()
                .map(x -> String.format("%02d", x))
                .reduce((temp1, temp2) -> temp1 + ", " + temp2)
                .orElse(null);

        students.addAll(i1.students);
        students.addAll(i2.students);
        maxTowers = i1.maxTowers + i2.maxTowers;
        owner = i1.owner;
        prohibitionToken = i1.prohibitionToken || i2.prohibitionToken;
    }

    /**
     * Method to return the id of the Island
     * @return the String representing the id of the Island (or all the id of the Islands which made the major
     * Island)
     */
    public String getId(){
        return id;
    }

    /**
     * Method to get the current owner of the Island
     * @return the tower's color representing the current Player/Team owner
     */
    public ColorTower getOwner(){
        return this.owner;
    }

    /**
     * Method to return the Report of the Island (the Report contains all the info useful to compute
     * the influence of the Players on the Island)
     * @return tne Report of the Island
     */
    public Report getReport(){
        int numTowers;
        if(owner==null){
            numTowers=0;
        }
        else { numTowers=maxTowers;}

        Map<Color, Integer> colors = new HashMap<>();
        int[] colorsCont = {0, 0, 0, 0, 0};
        for(Student s: students){
            switch (s.getColor()){
                case BLUE -> colorsCont[0]++;
                case YELLOW -> colorsCont[1]++;
                case RED -> colorsCont[2]++;
                case GREEN -> colorsCont[3]++;
                case PINK -> colorsCont[4]++;
            }
        }

        Color[] possibleColors = Color.values();
        for(int i = 0; i < possibleColors.length; i++){
            colors.put(possibleColors[i], colorsCont[i]);
        }

        return new Report(owner, numTowers, colors, null, 0);
    }

    /**
     * Method to return the copy of all the Students contained on the Island
     * @return the ArrayList containing all the Students on the Island
     */
    public ArrayList<Student> getAllStudents(){
        return (ArrayList<Student>)students.clone();
    }

    /**
     * Method to add a Student on an Island
     * @param s is the Student to be added on the Island
     */
    public void addStudent(Student s){
        students.add(s);
    }

    /**
     * This method returns the sprite of the Island
     * @return the Sprite of the Island
     */
    public String getSprite(){
        return sprite;
    }

    /**
     * Method to remove a Student from the Island
     * @param id is the id of the Student to be removed
     * @return the Student removed
     * @throws IllegalMoveException if there is no Student with such id on this Island
     */
    public Student removeStudent(int id) throws IllegalMoveException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent = new Student(id, Color.BLUE);
        int positionInList = this.students.indexOf(tempStudent);

        if(positionInList == -1){
            throw new IllegalMoveException("There isn't any Student with that id in the list");
        }

        Student removedStudent = this.students.get(positionInList);
        this.students.remove(positionInList);

        return removedStudent;
    }

    /**
     * Method to change the owner of the Island
     * @param t is the Player's color who has to become the new owner of the Island
     */
    public void conquest(ColorTower t) {
        owner=t;
    }

    /**
     * Method to place a Prohibition-Token on the Island
     * @param prohibitionToken is the boolean indicating if a Token is placed or not on the Island
     * @throws IllegalMoveException if there is already a Prohibition Token on this Island
     */
    public void setProhibition(boolean prohibitionToken) throws IllegalMoveException {
        if(prohibitionToken && this.prohibitionToken) throw new IllegalMoveException("You cannot place another No Entry tile on this Island...");
        this.prohibitionToken = prohibitionToken;
    }

    /**
     * Method to return if a Prohibition-Token is on the Island or not
     * @return true if the Token is on the Island, false otherwise
     */
    public boolean getProhibition(){
        return prohibitionToken;
    }

    /**
     * Overriding method to compare two Islands
     * @param o is the Island to be compared with this
     * @return true if two Islands are the same (if the id are the same), false otherwise
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Island){
            return ((Island) o).getId().equals(id);
        }
        return false;
    }

    /**
     * Overriding method to return the id of the Island
     * @return the entire id of the Island
     */
    @Override
    public String toString(){
        //return String.valueOf(id);
        return id;
    }

    /**
     * Method to check if the id of the Island contains idToCheck. In fact this.id is made by all the
     * ids of the previous Islands, so through this method it is possible to check if this.Island is
     * made by an Island
     * @param idToCheck is the id of the Island to check if it is contained to this
     * @return true if idToCheck is contained into this.id
     */
    public boolean checkContainedId(int idToCheck){
        String x = String.format("%02d", idToCheck);
        String[] arr = this.id.split(", ");
        return Arrays.asList(arr).contains(x);
    }
}