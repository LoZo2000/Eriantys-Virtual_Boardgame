package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Movable;
import it.polimi.ingsw.model.exceptions.*;

/**
 * The class MoveStudentMessage represent the action that move a student in the game, extends the class Message and implement the method
 * execute that apply changes to the game
 */
public class MoveStudentMessage extends MovementMessage{
    final private int studentId;

    /**
     * The constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param studentId is the id of the student to be moved
     * @param departureType is the type of the departure location
     * @param departureId is the id of the departure location
     * @param arrivalType is the type of the arrival location
     * @param arrivalId is the id of the arrival location
     */
    public MoveStudentMessage(String sender, int studentId, Location departureType, int departureId, Location arrivalType, int arrivalId){
        super(sender, Action.MOVESTUDENT, departureType, departureId, arrivalType, arrivalId);
        this.studentId = studentId;
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to move
     * a student in the game
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws IllegalMoveException is the exception thrown if MoveStudent is not a permitted action
     * @throws NoPlayerException is the exception thrown if a player isn't found
     * @throws NoIslandException is the exception thrown if an island isn't found
     * @throws CannotAddStudentException is the exception thrown if the student can't be added
     */
    @Override
    public Update execute(Game game) throws NoActiveCardException, IllegalMoveException, NoPlayerException, NoIslandException, CannotAddStudentException{
        Boolean usedCard = null;
        boolean activeCard = false;

        if(game.getActiveCard() != -1 && game.getRequestedAction() != Action.EXCHANGESTUDENT) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.MOVESTUDENT)
                throw new IllegalMoveException("Wrong move: you should not move students now!");

            if (checkErrorMovementLocations(game))
                throw new IllegalMoveException("You can't move students from these locations");

        } else if (departureType != Location.ENTRANCE || (arrivalType != Location.ISLAND && arrivalType != Location.CANTEEN))
            throw new IllegalMoveException("Illegal movement!");

        Movable departure=null, arrival=null;

        departure = getLocation(game, departureType, departureId);

        arrival = getLocation(game, arrivalType, arrivalId);

        if(game.getActiveCard() != -1 && game.getRequestedAction() == Action.MOVESTUDENT){
            usedCard = false;
            activeCard = true;
        }

        game.moveStudent(studentId, arrival, departure);

        try {
            if(game.needsRefill())
                game.refillActiveCard();
        }catch( NoMoreStudentsException e){
            game.setLastTurn(true);
        }

        if(game.getActiveCard() == -1 || game.getRequestedAction() == Action.EXCHANGESTUDENT) {
            game.reduceRemainingMoves(activeCard);
        }

        return new Update(null, null, game.getRemainingMoves(), null, usedCard, null, game.getFinishedGame(), game.getWinner(), game.getIsLastTurn());
    }

    /**
     * This method return the id of the student
     * @return an int representing the id of the student to move
     */
    public int getStudentId(){
        return studentId;
    }

    /**
     * This method return the type of the departure location
     * @return an Enum representing the type of the departure Location
     */
    public Location getDepartureType(){
        return departureType;
    }

    /**
     * This method return the id of the departure location
     * @return an int representing the id of the departure location
     */
    public int getDepartureId(){
        return departureId;
    }

    /**
     *This method return the type of the arrival location
     * @return an Enum representing the type of the arrival Location
     */
    public Location getArrivalType(){
        return arrivalType;
    }

    /**
     * This method return the id of the arrival location
     * @return an Enum representing the id of the arrival location
     */
    public int getArrivalId(){
        return arrivalId;
    }
}