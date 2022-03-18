package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private List<Student> students;

    //'num' is the number of students to create FOR EVERY single color
    public Bag(int num){
        students = new ArrayList<Student>();
        for(Color c : Color.values())
            for(int i=0; i<num; i++)
                students.add(new Student(i,c));
        Collections.shuffle(students);
    }

    public Student getRandomStudent() throws NoMoreStudentsException {
        if(students.size()>0) {
            Student s = students.get(0);
            students.remove(0);
            return s;
        }
        else throw new NoMoreStudentsException("There are no more students in the bag!!!");
    }

    public int getStudentsNum(){
        return students.size();
    }
}