package it.polimi.ingsw.model.exceptions;

public class NoSuchStudentException extends Exception{
    public NoSuchStudentException(){
        super();
    }
    public NoSuchStudentException(String s){
        super(s);
    }
}