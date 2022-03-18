package it.polimi.ingsw.model.exceptions;

public class WrongNumberOfTowersException extends Exception{
    public WrongNumberOfTowersException(){
        super();
    }
    public WrongNumberOfTowersException(String s){
        super(s);
    }
}