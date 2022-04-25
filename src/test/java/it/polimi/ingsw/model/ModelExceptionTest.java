package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ModelExceptionTest {

    @Test
    public void testAllExceptions(){
        try{
            testAlreadyPlayedCardException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testAlreadyPlayedCardException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testCannotJoinException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testCannotJoinException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testEndGameException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testEndGameException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testIllegalMoveException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testIllegalMoveException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoActiveCardException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoActiveCardException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoCharacterSelectedException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoCharacterSelectedException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoContiguousIslandException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoContiguousIslandException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoIslandException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoIslandException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoMoreStudentsException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoMoreStudentsException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoMoreTokensException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoMoreTokensException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoPlayerException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoPlayerException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoSuchStudentException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNoSuchStudentException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNotEnoughMoneyException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNotEnoughMoneyException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNotYourTurn(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testNotYourTurn(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testOverflowCardException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testOverflowCardException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testStillStudentException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testStillStudentException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testTooManyStudentsException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testTooManyStudentsException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testUnrecognizedPlayerOrActionException(true);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            testUnrecognizedPlayerOrActionException(false);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
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