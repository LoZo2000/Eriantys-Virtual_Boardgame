package it.polimi.ingsw.controller.exceptions;

/**
 * This class is the exception thrown when a player try to do an action not permitted in a part of the turn
 */
public class IllegalActionException extends Exception {

    /**
     * This metod is the constructor of the class, call the constructor of the super class exception
     */
    public IllegalActionException(){
        super();
    }

    /**
     * This method is the constructor of the class with a parameter of type string, simply call the constructor of the super class exception
     * @param s
     */
    public IllegalActionException(String s){
        super(s);
    }
}
