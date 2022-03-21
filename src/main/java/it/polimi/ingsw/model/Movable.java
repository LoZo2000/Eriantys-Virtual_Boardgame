package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

public interface Movable {
    void addStudent(Student s);
    Student removeStudent(int id) throws NoSuchStudentException;
}
