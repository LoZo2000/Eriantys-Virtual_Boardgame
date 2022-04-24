package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.exceptions.NoMoreTokensException;
import it.polimi.ingsw.model.exceptions.NoSuchStudentException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Set;

public class MovementCharacter extends ActionCharacter implements Movable {
    private final Location locationType;
    private final boolean refill;
    private ArrayList<Student> students;
    private final Set<Location> allowedDepartures;
    private final Set<Location> allowedArrivals;

    public MovementCharacter(int id, CharacterType type, String desc, int cost, ArrayList<Student> students, JSONParams params){
        super(id, type, desc, cost, params);

        this.locationType = params.getLocationType();
        this.refill = params.getRefill();
        this.students = new ArrayList<>(students);

        this.allowedDepartures = params.getAllowedDepartures();
        this.allowedArrivals = params.getAllowedArrivals();
    }

    public boolean isRefill() {
        return refill;
    }

    public ArrayList<Student> getStudents() {
        return (ArrayList<Student>) students.clone();
    }

    public Location getLocationType(){
        return this.locationType;
    }

    public Set<Location> getAllowedArrivals() {
        return Set.copyOf(this.allowedArrivals);
    }

    public Set<Location> getAllowedDepartures() {
        return Set.copyOf(this.allowedDepartures);
    }

    public void addStudent(Student s){
        students.add(s);
    }

    public Student removeStudent(int id) throws NoSuchStudentException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent = new Student(id, Color.RED);
        int positionInList = this.students.indexOf(tempStudent);

        if(positionInList == -1){
            throw new NoSuchStudentException("There isn't any Student with that id in the list");
        }

        Student removedStudent = this.students.get(positionInList);
        this.students.remove(positionInList);

        return removedStudent;
    }

    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mLocation Type:\u001B[0m " + this.locationType + " - \u001B[1mNeeds Refill?\u001B[0m " + this.refill + "\n";
        s += "\u001B[1mAllowed Departures:\u001B[0m " + this.allowedDepartures.toString() + "\n";
        s += "\u001B[1mAllowed Arrivals:\u001B[0m " + this.allowedArrivals.toString() + "\n";
        s += "\u001B[1mStudents:\u001B[0m " + this.students.toString() + "\n";
        return s;
    }

    @Override
    public void removeToken() throws NoMoreTokensException {
        throw new NoMoreTokensException("You can't remove students without movement");
    }

    @Override
    public void addToken() throws NoMoreTokensException {
        throw new NoMoreTokensException("You can't add students without movement");
    }
}
