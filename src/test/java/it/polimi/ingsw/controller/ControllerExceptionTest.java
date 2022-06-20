package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class test the exceptions thrown of the controller
 */
public class ControllerExceptionTest {

    /**
     * This method test the launch of all exceptions in the controller
     */
    @Test
    public void testAllExceptions(){
        assertThrows(IllegalActionException.class, () -> testIllegalActionException(true));
        assertThrows(IllegalActionException.class, () -> testIllegalActionException(false));
    }

    private void testIllegalActionException(boolean withString) throws IllegalActionException {
        if(withString) throw new IllegalActionException("helo");
        throw new IllegalActionException();
    }
}