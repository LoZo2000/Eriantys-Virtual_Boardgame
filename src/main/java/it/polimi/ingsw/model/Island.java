package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Island implements Cloneable, Movable{
    private final int id;
    private ArrayList<Student> students;
    private boolean prohibitionToken = false;
    private int maxTowers;
    private ColorTower owner;

    public Island(int id){
        this.id = id;
        students = new ArrayList<Student>();
        maxTowers = 1;
    }

    //Creator to merge two islands (in Game you still have to delete the 2 previous islands and move motherNature to this new created island!!!)
    //The new id will be the id of the first island (FEEL FREE TO CHANGE IT)
    public Island(Island i1, Island i2){
        this(i1.getId());
        students.addAll(i1.getAllStudents());
        students.addAll(i2.getAllStudents());
        maxTowers = i1.getMaxTowers() + i2.getMaxTowers();
    }

    public int getId(){
        return id;
    }

    public Island getIsland(){
        try {
            return (Island)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Integer> getStudentsId (Color color, int numberOfStudents){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        int cont=0;
        for(Student s : students){
            if(cont>numberOfStudents) break;
            if(s.getColor() == color){
                ids.add(s.getId());
                cont++;
            }
        }
        return ids;
    }

    public Report getReport(){
        int numTowers;
        if(owner==null){
            numTowers=0;
        }
        else { numTowers=maxTowers;}

        return new Report( owner, numTowers, getNumberStudentColor(Color.BLUE), getNumberStudentColor(Color.YELLOW),
                           getNumberStudentColor(Color.RED), getNumberStudentColor(Color.GREEN), getNumberStudentColor(Color.PINK));
    }


    public int getMaxTowers(){
        return maxTowers;
    }

    public ArrayList<Student> getAllStudents(){
        return (ArrayList<Student>)students.clone();
    }

    //Returns the color of the player who currently owns the island (null if the island is still free)
    public ColorTower getCurrentOwner(){
        return owner;
    }

    //Returns the number of students (to change in a private method probably)
    public int getNumberStudentColor(Color color){
        int cont = 0;
        for(Student s : students){
            if(s.getColor() == color) cont++;
        }
        return cont;
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

    //Change the owner of the island
    public void conquest(ColorTower t) {
        owner=t;
    }

    public void setProhibition(boolean prohibitionToken) {
        this.prohibitionToken = prohibitionToken;
    }

    public boolean getProhibition(){
        return prohibitionToken;
    }
}