package it.polimi.ingsw.model.exceptions;

/**
 * The class IllegalMoveException represent the exception thrown when an action is not permitted
 */
public class IllegalMoveException extends Exception{

    /**
     * This method is the constructor of the class
     */
    public IllegalMoveException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public IllegalMoveException(String s){
        super(s);
    }
}