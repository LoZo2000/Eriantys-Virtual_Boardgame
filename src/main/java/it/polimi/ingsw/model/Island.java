package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Island implements Movable{
    private final int id;
    private ArrayList<Student> students;
    private boolean prohibitionToken = false;
    private int maxTowers;
    private ColorTower owner;

    public Island(int id){
        this.id = id;
        students = new ArrayList<>();
        maxTowers = 1;
    }

    //Creator to merge two islands (in Game you still have to delete the 2 previous islands and move motherNature to this new created island!!!)
    //The new id will be the id of the first island (FEEL FREE TO CHANGE IT)
    public Island(Island i1, Island i2){
        this(i1.getId());
        students.addAll(i1.getAllStudents());
        students.addAll(i2.getAllStudents());
        maxTowers = i1.getReport().getTowerNumbers() + i2.getReport().getTowerNumbers();
        owner = i1.getReport().getOwner();
    }

    public int getId(){
        return id;
    }

    private ArrayList<Integer> getStudentsId(Color color, int numberOfStudents){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        int cont=0;
        for(Student s : students){
            if(cont>=numberOfStudents) break;
            if(s.getColor() == color) {
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

        int[] colors = {0,0,0,0,0};
        for(Student s : students){
            switch (s.getColor()){
                case BLUE -> colors[0]++;
                case YELLOW -> colors[1]++;
                case RED -> colors[2]++;
                case GREEN -> colors[3]++;
                case PINK -> colors[4]++;
            }
        }
        return new Report(owner, numTowers, colors[0], colors[1], colors[2], colors[3], colors[4]);
    }

    public ArrayList<Student> getAllStudents(){
        return (ArrayList<Student>)students.clone();
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

    public boolean equals(Object o){
        if(o instanceof Island){
            if(((Island)o).getId()==id) return true;
        }
        return false;
    }
}