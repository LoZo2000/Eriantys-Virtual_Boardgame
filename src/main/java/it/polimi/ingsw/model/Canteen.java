package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import java.util.ArrayList;

/**
 * the class canteen represent a part of the player dashboard, inside the canteen can be stored students, and can give coins
 * to the players depending by the number of students in it
 */
public class Canteen implements Movable{
    private ArrayList<Student> blue;
    private ArrayList<Student> yellow;
    private ArrayList<Student> red;
    private ArrayList<Student> green;
    private ArrayList<Student> pink;

    private boolean giveCoin;
    private Color removedColor;

    /**
     * Creator for an object Canteen
     */
    public Canteen(){
        blue = new ArrayList<>();
        yellow = new ArrayList<>();
        red = new ArrayList<>();
        green = new ArrayList<>();
        pink = new ArrayList<>();

        this.giveCoin = false;
        this.removedColor = null;
    }

    /**
     * Method to add a student in the canteen. The method automatically insert the new student in
     * an ArrayList according to its color
     * @param s is the student you want to add in the canteen
     */
    public void addStudent(Student s){
        switch (s.getColor()){
            case BLUE -> blue.add(s);
            case YELLOW -> yellow.add(s);
            case RED -> red.add(s);
            case GREEN -> green.add(s);
            case PINK -> pink.add(s);
        }

        this.giveCoin = true;
    }

    /**
     * This method returns all the students of color C currently in the Canteen
     * @param c is the color of the students you want
     * @return an ArrayList of students of that color
     */
    public ArrayList<Student> getStudents(Color c){
        switch (c){
            case BLUE:
                return (ArrayList<Student>)blue.clone();
            case YELLOW:
                return (ArrayList<Student>)yellow.clone();
            case RED:
                return (ArrayList<Student>)red.clone();
            case GREEN:
                return (ArrayList<Student>)green.clone();
            default: //case PINK
                return (ArrayList<Student>)pink.clone();
        }
    }

    /**
     * This method, given a Color c, returns the number of students in the Canteen of Color c
     * @param c is the color of the students you want
     * @return an integer representing the number of students of Color c in the canteen
     */
    public int getNumberStudentColor(Color c){
        switch (c){
            case BLUE:
                return blue.size();
            case YELLOW:
                return yellow.size();
            case RED:
                return red.size();
            case GREEN:
                return green.size();
            default: //case PINK
                return pink.size();
        }
    }

    /**
     * Method to remove a student from the Canteen (needed only when certain characters are activated
     * @param id the identification number corresponding to the student you want to remove
     * @return the Student with the corresponding id (if it is in the Canteen)
     * @throws IllegalMoveException if there is no such Student in this Canteen
     */
    public Student removeStudent(int id) throws IllegalMoveException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent;
        int positionInList;

        tempStudent = new Student(id, Color.RED);
        positionInList = this.red.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.RED;

            Student removedStudent = this.red.get(positionInList);
            this.red.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.BLUE);
        positionInList = this.blue.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.BLUE;

            Student removedStudent = this.blue.get(positionInList);
            this.blue.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.GREEN);
        positionInList = this.green.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.GREEN;

            Student removedStudent = this.green.get(positionInList);
            this.green.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.PINK);
        positionInList = this.pink.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.PINK;

            Student removedStudent = this.pink.get(positionInList);
            this.pink.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.YELLOW);
        positionInList = this.yellow.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.YELLOW;

            Student removedStudent = this.yellow.get(positionInList);
            this.yellow.remove(positionInList);
            return removedStudent;
        }

        throw new IllegalMoveException("There isn't any Student with that id in the lists");
    }

    /**
     * Method to check if a player is eligible to get a coin. after a move
     * @param c is the Color of the last Student moved to the Canteen
     * @param reset is a flag that set to false the giveCoin boolean. When we exchange 2 students this flag can't be reset
     *     for the first color because we don't know if the first Movable in the moveStudents method was the Canteen or
     *     the Entrance.
     * @return true if the player is eligible to get a coin, false otherwise
     */
    public boolean canGetCoin(Color c, boolean reset){
        boolean result = false;
        if(this.giveCoin && this.getNumberStudentColor(c) % 3 == 0 && this.getNumberStudentColor(c) != 0 && this.removedColor != c){
            result = true;
        }
        if(reset) {
            this.giveCoin = false;
            this.removedColor = null;
        }
        return result;
    }
}