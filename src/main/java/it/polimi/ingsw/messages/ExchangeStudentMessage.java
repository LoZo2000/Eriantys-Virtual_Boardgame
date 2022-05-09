package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Movable;
import it.polimi.ingsw.model.exceptions.*;

public class ExchangeStudentMessage extends MovementMessage{
    final private int studentId1;
    final private int studentId2;

    public ExchangeStudentMessage(String sender, int studentId1, int studentId2, Location departureType, int departureId, Location arrivalType, int arrivalId){
        super(sender, Action.EXCHANGESTUDENT, departureType, departureId, arrivalType, arrivalId);
        this.studentId1 = studentId1;
        this.studentId2 = studentId2;
    }

    @Override
    public Update execute(Game game) throws NoActiveCardException, IllegalMoveException, NoIslandException, NoPlayerException {
        if(game.getActiveCard() != -1) {
            Action requestedActiveAction = game.getRequestedAction();
            if (requestedActiveAction != Action.EXCHANGESTUDENT)
                throw new IllegalMoveException("Wrong move: you should not move students now!");

            if (checkErrorMovementLocations(game))
                throw new IllegalMoveException("You can't move students from these locations");

        }

        Movable departure, arrival;

        departure = getLocation(game, departureType, -1);

        arrival = getLocation(game, arrivalType, -1);

        try {
            game.exchangeStudent(studentId1, studentId2, arrival, departure);
        }catch (Exception e){
            throw new IllegalMoveException("Student, arrival or departure missing...");
        }

        return new Update(null, null, null, null, false, null, game.getFinishedGame(), game.getWinner(), game.getIsLastTurn());

    }

    public int getStudentId(){
        return studentId1;
    }
    public int getStudentId2(){
        return studentId2;
    }
    public Location getDepartureType(){
        return departureType;
    }
    public int getDepartureId(){
        return departureId;
    }
    public Location getArrivalType(){
        return arrivalType;
    }
    public int getArrivalId(){
        return arrivalId;
    }
}
