package it.polimi.ingsw.model.exceptions;

public class CannotJoinException extends Exception{
    public CannotJoinException(){
        super();
    }
    public CannotJoinException(String s){
        super(s);
    }
}