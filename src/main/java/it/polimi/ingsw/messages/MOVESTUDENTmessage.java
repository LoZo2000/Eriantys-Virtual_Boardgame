package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;

public class MOVESTUDENTmessage extends Message{
    final private int studentId;
    final private Location departureType;
    final private int departureId;
    final private Location arrivalType;
    final private int arrivalId;

    public MOVESTUDENTmessage(String sender, Action action, int studentId, Location departureType, int departureId, Location arrivalType, int arrivalId){
        super(sender, action);
        this.studentId = studentId;
        this.departureType = departureType;
        this.departureId = departureId;
        this.arrivalType = arrivalType;
        this.arrivalId = arrivalId;
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