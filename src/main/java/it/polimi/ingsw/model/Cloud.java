package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.TooManyStudentsException;
import it.polimi.ingsw.model.exceptions.StillStudentException;

import java.io.Serializable;
import java.util.ArrayList;

public class Cloud {
    private final int maxStudents;

    private boolean isFull;
    private ArrayList<Student> students;

    public Cloud(int maxStudents){
        this.maxStudents = maxStudents;
        this.isFull = false;
        students = new ArrayList<>();
    }

    public void refillCloud(ArrayList<Student> newStudents) {
        for(Student s : newStudents){
            addStudent(s);
        }
        this.isFull = true;
    }

    public int getNumberOfStudentPerColor(Color color){
        long contStudents = this.students.stream()
                .filter((s) -> s.getColor() == color)
                .count();
        return (int) contStudents;
    }

    //To pass students one at a time
    public void addStudent(Student student) {
        if(students.size()<maxStudents) students.add(student);
    }

    //Returns a copy of students (to let the users examine the cloud)
    public ArrayList<Student> getStudents(){
        return (ArrayList<Student>)students.clone();
    }

    //Returns the students and empties the cloud
    public ArrayList<Student> chooseCloud(){
        ArrayList<Student> s = (ArrayList<Student>)students.clone();
        students.clear();
        this.isFull = false;
        return s;
    }

    public boolean isFull(){
        return isFull;
    }

    public void setIsFull(Boolean isFull){
        this.isFull=isFull;
    }
}