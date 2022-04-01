package it.polimi.ingsw.controller.exceptions;

public class IllegalMoveException extends Exception{
    public IllegalMoveException(){
        super();
    }
    public IllegalMoveException(String s){
        super(s);
    }
}