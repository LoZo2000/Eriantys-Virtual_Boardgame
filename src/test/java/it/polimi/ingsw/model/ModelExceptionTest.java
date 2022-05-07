package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class ModelExceptionTest {

    @Test
    public void testAllExceptions(){
        assertThrows(AlreadyPlayedCardException.class, () -> testAlreadyPlayedCardException(true));
        assertThrows(AlreadyPlayedCardException.class, () -> testAlreadyPlayedCardException(false));

        assertThrows(CannotJoinException.class, () -> testCannotJoinException(true));
        assertThrows(CannotJoinException.class, () -> testCannotJoinException(false));

        assertThrows(EndGameException.class, () -> testEndGameException(true));
        assertThrows(EndGameException.class, () -> testEndGameException(false));

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

        assertThrows(NoMoreTokensException.class, () -> testNoMoreTokensException(true));
        assertThrows(NoMoreTokensException.class, () -> testNoMoreTokensException(false));

        assertThrows(NoPlayerException.class, () -> testNoPlayerException(true));
        assertThrows(NoPlayerException.class, () -> testNoPlayerException(false));

        assertThrows(NoSuchStudentException.class, () -> testNoSuchStudentException(true));
        assertThrows(NoSuchStudentException.class, () -> testNoSuchStudentException(false));

        assertThrows(NotEnoughMoneyException.class, () -> testNotEnoughMoneyException(true));
        assertThrows(NotEnoughMoneyException.class, () -> testNotEnoughMoneyException(false));

        assertThrows(NotYourTurnException.class, () -> testNotYourTurn(true));
        assertThrows(NotYourTurnException.class, () -> testNotYourTurn(false));

        assertThrows(OverflowCardException.class, () -> testOverflowCardException(true));
        assertThrows(OverflowCardException.class, () -> testOverflowCardException(false));

        assertThrows(StillStudentException.class, () -> testStillStudentException(true));
        assertThrows(StillStudentException.class, () -> testStillStudentException(false));

        assertThrows(TooManyStudentsException.class, () -> testTooManyStudentsException(true));
        assertThrows(TooManyStudentsException.class, () -> testTooManyStudentsException(false));

        assertThrows(UnrecognizedPlayerOrActionException.class, () -> testUnrecognizedPlayerOrActionException(true));
        assertThrows(UnrecognizedPlayerOrActionException.class, () -> testUnrecognizedPlayerOrActionException(false));
    }

    private void testCannotJoinException(boolean withString) throws CannotJoinException{
        if(withString) throw new CannotJoinException("helo");
        throw new CannotJoinException();
    }
    private void testEndGameException(boolean withString) throws EndGameException{
        if(withString) throw new EndGameException("helo");
        throw new EndGameException();
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
    private void testNoMoreTokensException(boolean withString) throws NoMoreTokensException {
        if(withString) throw new NoMoreTokensException("helo");
        throw new NoMoreTokensException();
    }
    private void testNoPlayerException(boolean withString) throws NoPlayerException {
        if(withString) throw new NoPlayerException("helo");
        throw new NoPlayerException();
    }
    private void testNoSuchStudentException(boolean withString) throws NoSuchStudentException {
        if(withString) throw new NoSuchStudentException("helo");
        throw new NoSuchStudentException();
    }
    private void testNotEnoughMoneyException(boolean withString) throws NotEnoughMoneyException {
        if(withString) throw new NotEnoughMoneyException("helo");
        throw new NotEnoughMoneyException();
    }
    private void testNotYourTurn(boolean withString) throws NotYourTurnException {
        if(withString) throw new NotYourTurnException("helo");
        throw new NotYourTurnException();
    }
    private void testOverflowCardException(boolean withString) throws OverflowCardException {
        if(withString) throw new OverflowCardException("helo");
        throw new OverflowCardException();
    }
    private void testStillStudentException(boolean withString) throws StillStudentException {
        if(withString) throw new StillStudentException("helo");
        throw new StillStudentException();
    }
    private void testTooManyStudentsException(boolean withString) throws TooManyStudentsException {
        if(withString) throw new TooManyStudentsException("helo");
        throw new TooManyStudentsException();
    }
    private void testUnrecognizedPlayerOrActionException(boolean withString) throws UnrecognizedPlayerOrActionException {
        if(withString) throw new UnrecognizedPlayerOrActionException("helo");
        throw new UnrecognizedPlayerOrActionException();
    }

    private void testAlreadyPlayedCardException(boolean withString) throws AlreadyPlayedCardException {
        if(withString) throw new AlreadyPlayedCardException("helo");
        throw new AlreadyPlayedCardException();
    }
}