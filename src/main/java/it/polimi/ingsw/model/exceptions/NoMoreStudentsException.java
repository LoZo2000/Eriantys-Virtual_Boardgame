package it.polimi.ingsw.model.exceptions;

public class NoMoreStudentsException extends Exception{
    public NoMoreStudentsException(){
        super();
    }
    public NoMoreStudentsException(String s){
        super(s);
    }
}