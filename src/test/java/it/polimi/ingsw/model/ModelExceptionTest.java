package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class contains the test for all the exceptions in the model
 */
public class ModelExceptionTest {

    /**
     * This method tests the thrown of all the exception in the model
     */
    @Test
    public void testAllExceptions(){

        assertThrows(IllegalMoveException.class, () -> testIllegalMoveException(true));
        assertThrows(IllegalMoveException.class, () -> testIllegalMoveException(false));

        assertThrows(NoActiveCardException.class, () -> testNoActiveCardException(true));
        assertThrows(NoActiveCardException.class, () -> testNoActiveCardException(false));

        assertThrows(NoCharacterSelectedException.class, () -> testNoCharacterSelectedException(true));
        assertThrows(NoCharacterSelectedException.class, () -> testNoCharacterSelectedException(false));

        assertThrows(NoContiguousIslandException.class, () -> testNoContiguousIslandException(true));
        assertThrows(NoContiguousIslandException.class, () -> testNoContiguousIslandException(false));

        assertThrows(NoIslandException.class, () -> testNoIslandException(true));
        assertThrows(NoIslandException.class, () -> testNoIslandException(false));

        assertThrows(NoMoreStudentsException.class, () -> testNoMoreStudentsException(true));
        assertThrows(NoMoreStudentsException.class, () -> testNoMoreStudentsException(false));

        assertThrows(NoPlayerException.class, () -> testNoPlayerException(true));
        assertThrows(NoPlayerException.class, () -> testNoPlayerException(false));

        assertThrows(OverflowCardException.class, () -> testOverflowCardException(true));
        assertThrows(OverflowCardException.class, () -> testOverflowCardException(false));
    }

    private void testIllegalMoveException(boolean withString) throws IllegalMoveException {
        if(withString) throw new IllegalMoveException("helo");
        throw new IllegalMoveException();
    }
    private void testNoActiveCardException(boolean withString) throws NoActiveCardException {
        if(withString) throw new NoActiveCardException("helo");
        throw new NoActiveCardException();
    }
    private void testNoCharacterSelectedException(boolean withString) throws NoCharacterSelectedException {
        if(withString) throw new NoCharacterSelectedException("helo");
        throw new NoCharacterSelectedException();
    }
    private void testNoContiguousIslandException(boolean withString) throws NoContiguousIslandException {
        if(withString) throw new NoContiguousIslandException("helo");
        throw new NoContiguousIslandException();
    }
    private void testNoIslandException(boolean withString) throws NoIslandException {
        if(withString) throw new NoIslandException("helo");
        throw new NoIslandException();
    }
    private void testNoMoreStudentsException(boolean withString) throws NoMoreStudentsException {
        if(withString) throw new NoMoreStudentsException("helo");
        throw new NoMoreStudentsException();
    }
    private void testNoPlayerException(boolean withString) throws NoPlayerException {
        if(withString) throw new NoPlayerException("helo");
        throw new NoPlayerException();
    }
    private void testOverflowCardException(boolean withString) throws OverflowCardException {
        if(withString) throw new OverflowCardException("helo");
        throw new OverflowCardException();
    }
}