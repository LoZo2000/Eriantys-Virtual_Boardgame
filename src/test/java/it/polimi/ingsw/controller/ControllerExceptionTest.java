package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class ControllerExceptionTest {

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