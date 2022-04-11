package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Bag implements Serializable {
    private final Stack<Student> students;

    public Bag(Stack<Student> students){
        this.students = (Stack<Student>) students.clone();
        Collections.shuffle(this.students);
    }

    public Student getRandomStudent() throws NoMoreStudentsException {
        if(students.size()>0) {
            return this.students.pop();
        }
        else throw new NoMoreStudentsException("There are no more students in this bag!!!");
    }

    public ArrayList<Student> getRandomStudent(int numberOfStudents) throws NoMoreStudentsException {
        ArrayList<Student> s = new ArrayList<>();
        for(int i = 0; i < numberOfStudents; i++){
            if(students.size()>0) {
                s.add(this.students.pop());
            } else
                throw new NoMoreStudentsException("There are no more students in this bag!!!");
        }
        return s;
    }

    public int getStudentsNum(){
        return students.size();
    }
}