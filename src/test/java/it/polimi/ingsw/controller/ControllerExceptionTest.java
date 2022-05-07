package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalMessageException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class ControllerExceptionTest {

    @Test
    public void testAllExceptions(){
        assertThrows(IllegalActionException.class, () -> testIllegalActionException(true));
        assertThrows(IllegalActionException.class, () -> testIllegalActionException(false));

        assertThrows(IllegalMessageException.class, () -> testIllegalMessageException(true));
        assertThrows(IllegalMessageException.class, () -> testIllegalMessageException(false));
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