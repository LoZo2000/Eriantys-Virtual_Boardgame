package it.polimi.ingsw.model.exceptions;

public class TooManyStudentsException extends Exception{
    public TooManyStudentsException(){
        super();
    }
    public TooManyStudentsException(String s){
        super(s);
    }
}
