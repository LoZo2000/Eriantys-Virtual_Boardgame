package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;

import java.util.ArrayList;
import java.util.Collections;

public class Bag {
    private ArrayList<Student> students;

    public Bag(ArrayList<Student> students){
        this.students = (ArrayList<Student>) students.clone();
        Collections.shuffle(students);
    }

    public Student getRandomStudent() throws NoMoreStudentsException {
        if(students.size()>0) {
            Student s = students.get(0);
            students.remove(0);
            return s;
        }
        else throw new NoMoreStudentsException("There are no more students in this bag!!!");
    }

    public int getStudentsNum(){
        return students.size();
    }
}