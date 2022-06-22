package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IllegalMoveException;

/**
 * Interface that represent the ability of a class to move student, so if a class want to move student have to
 * implement this interface
 */
public interface Movable {

    /**
     * method that add a student to the class that implement the interface movable
     * @param s the student to add
     * @throws IllegalMoveException if the students can't be added
     */
    void addStudent(Student s) throws IllegalMoveException;

    /**
     * method that remove a student to the class that implement the interface movable
     * @param id the id of the student to remove
     * @return the student removed
     * @throws IllegalMoveException if there are not students to remove
     */
    Student removeStudent(int id) throws IllegalMoveException;
}