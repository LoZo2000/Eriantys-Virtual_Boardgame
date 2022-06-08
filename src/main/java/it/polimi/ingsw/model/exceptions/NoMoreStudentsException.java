package it.polimi.ingsw.model.exceptions;

/**
 * The class NoMoreStudentException represent the exception thrown when a method tries to extract one or more students
 * from the bag but there are no students left
 */
public class NoMoreStudentsException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NoMoreStudentsException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public NoMoreStudentsException(String s){
        super(s);
    }
}