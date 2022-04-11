package it.polimi.ingsw.model.exceptions;

public class IllegalMoveException extends Exception{
    public IllegalMoveException(){
        super();
    }
    public IllegalMoveException(String s){
        super(s);
    }
}