package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

import java.util.ArrayList;
import java.util.Collections;

public class Entrance implements Movable, Cloneable {
    ArrayList<Student> students;

    public Entrance(ArrayList<Student> entranceStudents){
        students = new ArrayList<>(entranceStudents);
    }

    public Entrance getEntrance(){
        try {
            return (Entrance)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addStudent(Student s){
        students.add(s);
    }

    /*public Student removeStudent(int id) throws NoSuchStudentException {
        Student s;
        for(int i=0; i<students.size(); i++){
            if(students.get(i).getId() == id){
                s = students.get(i);
                students.remove(i);
                return s;
            }
        }
        throw new NoSuchStudentException();
    }*/
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

    public ArrayList<Student> getAllStudents(){
        return (ArrayList<Student>)students.clone();
    }
}
