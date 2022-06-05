package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Movable;
import it.polimi.ingsw.model.exceptions.*;

/**
 * The class ExchangeStudentMessage represent the action that exchange students in the game, extends the class Message and implement the method
 * execute that apply changes to the game
 */
public class ExchangeStudentMessage extends MovementMessage{
    final private int studentId1;
    final private int studentId2;

    /**
     * The constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param studentId1 is the id of the first student to exchange
     * @param studentId2 is the id of the second student to exchange
     * @param departureType is the type of the departure location
     * @param departureId id the id of the departure location
     * @param arrivalType is the type of the arrival location
     * @param arrivalId is the id of the arrival location
     */
    public ExchangeStudentMessage(String sender, int studentId1, int studentId2, Location departureType, int departureId, Location arrivalType, int arrivalId){
        super(sender, Action.EXCHANGESTUDENT, departureType, departureId, arrivalType, arrivalId);
        this.studentId1 = studentId1;
        this.studentId2 = studentId2;
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to exchange
     * two students
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     * @throws IllegalMoveException is the exception thrown if ExchangeStudent is not a permitted action
     * @throws NoIslandException  is the exception thrown if an island with a specific id isn't found
     * @throws NoPlayerException is the exception thrown if a player isn't found
     */
    @Override
    public Update execute(Game game) throws NoActiveCardException, IllegalMoveException, NoIslandException, NoPlayerException {
        if(game.canExchange()) {
            if (checkErrorMovementLocations(game))
                throw new IllegalMoveException("You can't move students from these locations");

        } else{
            throw new IllegalMoveException("Wrong move: you should not exchange students now!");
        }

        Movable departure, arrival;

        departure = getLocation(game, departureType, -1);

        arrival = getLocation(game, arrivalType, -1);

        try {
            game.exchangeStudent(studentId1, studentId2, arrival, departure);
        }catch (Exception e){
            System.out.println(e);
            throw new IllegalMoveException("Student, arrival or departure missing...");
        }

        game.reduceRemainingExchanges();

        return new Update(null, null, null, null, null, null, game.getFinishedGame(), game.getWinner(), game.getIsLastTurn());

    }

    /**
     *This method return the id of the first student exchanged
     * @return an int representing the id of the first student
     */
    public int getStudentId(){
        return studentId1;
    }

    /**
     * This method return the id of the second student exchanged
     * @return an int representing the id of the second student
     */
    public int getStudentId2(){
        return studentId2;
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
