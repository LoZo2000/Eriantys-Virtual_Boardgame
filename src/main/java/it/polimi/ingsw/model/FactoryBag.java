package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Stack;

public class FactoryBag {
    Stack<Student> students;
    Bag bag;
    Bag initBag;

    public FactoryBag(){
        students = new Stack<>();
        int id = 0;

        for(Color c : Color.values()){
            students.push(new Student(id, c));
            students.push(new Student(id+1, c));
            id += 2;
        }
        initBag = new Bag(students);

        students.clear();
        for(Color c : Color.values()){
            for(int i=0; i<24; i++){
                students.push(new Student(id, c));
                id++;
            }
        }
        bag = new Bag(students);
    }

    public Bag getBag() {
        return bag;
    }

    public Bag getInitBag(){
        return initBag;
    }
}
