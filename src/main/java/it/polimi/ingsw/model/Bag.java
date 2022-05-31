package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * The class bag represent the bag of the game, in the bag there are the students implemented as a stack, and
 * there are the methods to get random students from the bag and put them in the bag
 */
public class Bag{
    private final Stack<Student> students;

    /**
     * Creator of Bag
     * @param students is a Stack containing all the students who are to be thrown in the bag
     */
    public Bag(Stack<Student> students){
        this.students = (Stack<Student>) students.clone();
        Collections.shuffle(this.students);
    }

    /**
     * This method returns a student contained in Bag
     * @return object Student drawn
     * @throws NoMoreStudentsException if there are no more students in Bag
     */
    public Student getRandomStudent() throws NoMoreStudentsException {
        if(students.size()>0) {
            return this.students.pop();
        }
        else throw new NoMoreStudentsException("There are no more students in this bag!!!");
    }

    /**
     * This method returns N students randomly drawn
     * @param numberOfStudents number of students you want to draw
     * @return an ArrayList containing all the students drawn
     * @throws NoMoreStudentsException if there aren't enough students in the Bag
     */
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

    /**
     * Method to throw a student in the Bag. This method is needed only when character 12 is activated
     * @param s Student you want to put again in Bag
     */
    public void putBackStudent(Student s){
        this.students.add(s);
        Collections.shuffle(this.students);
    }
}