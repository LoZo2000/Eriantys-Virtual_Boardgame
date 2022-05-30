package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Movable;
import it.polimi.ingsw.model.exceptions.*;

public class MoveStudentMessage extends MovementMessage{
    final private int studentId;

    public MoveStudentMessage(String sender, int studentId, Location departureType, int departureId, Location arrivalType, int arrivalId){
        super(sender, Action.MOVESTUDENT, departureType, departureId, arrivalType, arrivalId);
        this.studentId = studentId;
    }

    @Override
    public Update execute(Game game) throws NoActiveCardException, IllegalMoveException, NoPlayerException, NoIslandException, CannotAddStudentException {
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

        try {
            game.moveStudent(studentId, arrival, departure);
        }catch (Exception e){
            throw new IllegalMoveException("Student, arrival or departure missing...");
        }

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

    public int getStudentId(){
        return studentId;
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