package it.polimi.ingsw.model.exceptions;

/**
 * The class CannotAddStudentException represent the exception thrown when it is not possible to add a student
 */
public class CannotAddStudentException extends Exception{

    /**
     * This method is the constructor of the class
     * @param message is the message that will be printed out
     */
    public CannotAddStudentException(String message) {
        super(message);
    }
}
