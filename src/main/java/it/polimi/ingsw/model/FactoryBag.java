package it.polimi.ingsw.model;

import java.util.Stack;

/**
 * The class bag is a class that permit to implement the factory pattern, it is called at the beginning of the game to create the bag,
 * it can create two different bag, the initbag is used to place students at the beginning of the game in the islands because are two
 * per color, bag is the normal bag used during the game with 24 students per color
 */
public class FactoryBag{
    Stack<Student> students;
    Bag bag;
    Bag initBag;

    /**
     * Creator of the class FactoryBag. Aim of this entity is to create one bag for every match and one
     * temporary bag only to fill the islands
     */
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

    /**
     * This method returns a full bag (x24 students for every color)
     * @return the object Bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * This method returns the temporary bag (x2 students for every color)
     * @return an object Bag representing a temporary Bag
     */
    public Bag getInitBag(){
        return initBag;
    }
}