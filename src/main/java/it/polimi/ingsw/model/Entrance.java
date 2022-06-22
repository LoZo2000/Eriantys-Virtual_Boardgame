package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IllegalMoveException;

import java.util.ArrayList;

/**
 * The class entrance represent the entrance inside the dashboard, in the entrance can be stored students and implements the
 * class movable to add or remove students from it
 */
public class Entrance implements Movable{
    ArrayList<Student> students;

    /**
     * Creator to build the Entrance
     * @param entranceStudents are the first 7 or 9 students (according to the number of players
     *                         contained in Entrance
     */
    public Entrance(ArrayList<Student> entranceStudents){
        students = new ArrayList<>(entranceStudents);
    }

    /**
     * Method to add a student to the Entrance
     * @param s is the Student to be added
     */
    public void addStudent(Student s){
        students.add(s);
    }

    /**
     * Method to remove a Student from Entrance
     * @param id is the id of the student to be removed
     * @return the student removed from the Entrance
     * @throws IllegalMoveException is thrown if there is no a student with this id on Entrance
     */
    public Student removeStudent(int id) throws IllegalMoveException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent = new Student(id, Color.RED);
        int positionInList = this.students.indexOf(tempStudent);

        if(positionInList == -1){
            throw new IllegalMoveException("There isn't any Student with that id in the list");
        }

        Student removedStudent = this.students.get(positionInList);
        this.students.remove(positionInList);

        return removedStudent;
    }

    /**
     * Returns an ArrayList containing a copy of all the students in Entrance
     * @return an ArrayList containing a copy of all the students in Entrance
     */
    public ArrayList<Student> getAllStudents(){
        return (ArrayList<Student>)students.clone();
    }
}