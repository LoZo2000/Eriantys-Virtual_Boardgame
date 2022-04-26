package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.CannotAddStudentException;
import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

public interface Movable {
    void addStudent(Student s) throws CannotAddStudentException;
    Student removeStudent(int id) throws NoSuchStudentException;
}
