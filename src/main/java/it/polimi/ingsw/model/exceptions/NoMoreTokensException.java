package it.polimi.ingsw.model.exceptions;

/**
 * The class NoMoreStudentException represent the exception thrown when a method tries to use one or more tokens
 * but there are no tokens left
 */
public class NoMoreTokensException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NoMoreTokensException() {
    }

    /**
     * This method is the constructor of the class
     * @param message is the message printed out by the exception
     */
    public NoMoreTokensException(String message) {
        super(message);
    }
}
