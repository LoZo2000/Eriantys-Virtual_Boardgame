package it.polimi.ingsw.model.exceptions;

/**
 * The class NoIslandException represent the exception thrown when there is not an island with a specific id
 */
public class NoIslandException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NoIslandException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public NoIslandException(String s){
        super(s);
    }
}