package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.rules.ActionRule;
import it.polimi.ingsw.model.rules.Rule;

import java.util.ArrayList;
import java.util.Set;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * The class MovementCharacter is the class that represent the characters that require to make movements in their power
 */
public class MovementCharacter extends ActionCharacter implements Movable {
    protected final Location locationType;
    protected final boolean refill;
    protected ArrayList<Student> students;
    protected final Set<Location> allowedDepartures;
    protected final Set<Location> allowedArrivals;

    /**
     * This method is the constructor
     * @param id is the id of the Character
     * @param type is the type of the Character
     * @param desc is the description of the character
     * @param desc_short is the short version of the description of the character
     * @param cost is the cost to use the power of the character
     * @param students is an arraylist of the students to exchange
     * @param params are the parameters of the card taken by a JSON file
     */
    public MovementCharacter(int id, CharacterType type, String desc, String desc_short, int cost, ArrayList<Student> students, JSONParams params){
        super(id, type, desc, desc_short, cost, params);

        this.locationType = params.getLocationType();
        this.refill = params.getRefill();
        this.students = new ArrayList<>(students);

        this.allowedDepartures = params.getAllowedDepartures();
        this.allowedArrivals = params.getAllowedArrivals();
    }

    /**
     * This method report if the characther is refilled
     * @return a boolean reporting if the character is refilled
     */
    public boolean isRefill() {
        return refill;
    }

    /**
     * This method return the students on the card
     * @return an arraylist containing the students on the card
     */
    public ArrayList<Student> getStudents() {
        return (ArrayList<Student>) students.clone();
    }

    /**
     * This method return the location type
     * @return an enum Location representing the location type
     */
    public Location getLocationType(){
        return this.locationType;
    }

    /**
     * This method return the allowed arrivals
     * @return a set of enum Location containing the allowed arrivals
     */
    public Set<Location> getAllowedArrivals() {
        return Set.copyOf(this.allowedArrivals);
    }

    /**
     * This method return the allowed departures
     * @return a set of enum Location containing the allowed departures
     */
    public Set<Location> getAllowedDepartures() {
        return Set.copyOf(this.allowedDepartures);
    }

    /**
     * This method add a student to the card
     * @param s the student to add
     * @throws IllegalMoveException when is not possible to add the student
     */
    public void addStudent(Student s) throws IllegalMoveException {
        if(students.size() == getMaxNumTokens())
            throw new IllegalMoveException("You cannot add a student in this moment!");
        students.add(s);
    }

    /**
     * This method remove a student from the card
     * @param id the id of the student to remove
     * @return the student removed
     * @throws IllegalMoveException when there is not the student with the id passed in the parameter in the card
     */
    public Student removeStudent(int id) throws IllegalMoveException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent = new Student(id, Color.RED);
        int positionInList = this.students.indexOf(tempStudent);

        if(positionInList == -1){
            throw new IllegalMoveException("There isn't any Student with that id in the list");
        }

        Student removedStudent = this.students.get(positionInList);
        this.students.remove(positionInList);

        return removedStudent;
    }

    /**
     * This method return a string that report some parameters of the character card
     * @return a String containing the parameters of the character card
     */
    @Override
    public String shortString(){
        String s = super.shortString();
        s += ansi().a(" - ").bold().a("Students: ").reset().a(students).toString();
        return s;
    }

    /**
     * This method is the override of the method toString for the Movement Character objects
     * @return the String to use when toString is called
     */
    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mLocation Type:\u001B[0m " + this.locationType + " - \u001B[1mNeeds Refill?\u001B[0m " + this.refill + "\n";
        s += "\u001B[1mAllowed Departures:\u001B[0m " + this.allowedDepartures.toString() + "\n";
        s += "\u001B[1mAllowed Arrivals:\u001B[0m " + this.allowedArrivals.toString() + "\n";
        s += "\u001B[1mStudents:\u001B[0m " + this.students.toString() + "\n";
        return s;
    }

    /**
     * This method remove a token from the card
     * @throws IllegalMoveException when there are no tokens left
     */
    @Override
    public void removeToken() throws IllegalMoveException {
        throw new IllegalMoveException("You can't remove students without movement");
    }

    /**
     * This method add a token to the card
     * @throws IllegalMoveException when is not possible to add a token to the card
     */
    @Override
    public void addToken() throws IllegalMoveException {
        throw new IllegalMoveException("You can't add students without movement");
    }
}