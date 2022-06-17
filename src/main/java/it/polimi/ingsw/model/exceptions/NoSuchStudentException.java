package it.polimi.ingsw.model.exceptions;

/**
 * The class NoSuckStudentException represent the exception thrown when a method doesn't found a student with a specific id
 */
public class NoSuchStudentException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NoSuchStudentException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public NoSuchStudentException(String s){
        super(s);
    }
}