package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

import java.util.ArrayList;
import java.util.List;

public class Entrance implements Movable, Cloneable {
    List<Student> students;

    public Entrance(){
        students = new ArrayList<>();
    }

    public void addStudent(Student s){
        students.add(s);
    }

    public Student removeStudent(int id) throws NoSuchStudentException {
        Student s;
        for(int i=0; i<students.size(); i++){
            if(students.get(i).getId() == id){
                s = students.get(i);
                students.remove(i);
                return s;
            }
        }
        throw new NoSuchStudentException();
    }
}
