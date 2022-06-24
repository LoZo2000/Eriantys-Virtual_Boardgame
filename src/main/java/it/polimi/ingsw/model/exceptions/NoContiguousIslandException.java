package it.polimi.ingsw.model.exceptions;

/**
 * The class NoContiguousIslandException represent the exception thrown when is called the method merge island in two islands
 * that are not contiguous
 */
public class NoContiguousIslandException extends Exception {

    /**
     *  This method is the constructor of the class
     */
    public NoContiguousIslandException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public NoContiguousIslandException(String s){
        super(s);
    }
}