package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;

public class ExchangeStudentMessage extends Message{
    final private int studentId1;
    final private int studentId2;
    final private Location departureType;
    final private int departureId;
    final private Location arrivalType;
    final private int arrivalId;

    public ExchangeStudentMessage(String sender, Action action, int studentId1, int studentId2, Location departureType, int departureId, Location arrivalType, int arrivalId){
        super(sender, action);
        this.studentId1 = studentId1;
        this.studentId2 = studentId2;
        this.departureType = departureType;
        this.departureId = departureId;
        this.arrivalType = arrivalType;
        this.arrivalId = arrivalId;
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
