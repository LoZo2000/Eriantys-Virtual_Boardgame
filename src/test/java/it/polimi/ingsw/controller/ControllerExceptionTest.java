package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalMessageException;
import it.polimi.ingsw.model.exceptions.TooManyStudentsException;
import it.polimi.ingsw.model.exceptions.UnrecognizedPlayerOrActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ControllerExceptionTest {

    @Test
    public void testAllExceptions(){
        try{
            testIllegalActionException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testIllegalActionException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testIllegalMessageException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testIllegalMessageException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void testIllegalActionException(boolean withString) throws IllegalActionException {
        if(withString) throw new IllegalActionException("helo");
        throw new IllegalActionException();
    }
    private void testIllegalMessageException(boolean withString) throws IllegalMessageException {
        if(withString) throw new IllegalMessageException("helo");
        throw new IllegalMessageException();
    }
}