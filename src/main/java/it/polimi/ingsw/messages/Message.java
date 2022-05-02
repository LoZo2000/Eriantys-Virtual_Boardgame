package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

import java.io.Serializable;

public abstract class Message implements Serializable {
    final protected String sender;
    final protected Action action;

    public Message(String sender, Action action){
        this.sender = sender;
        this.action = action;
    }

    public abstract Update execute(Game game) throws IllegalMoveException, NoActiveCardException, NoPlayerException, NoIslandException, EndGameException, NoMoreTokensException, NotEnoughMoneyException, NoCharacterSelectedException, NoSuchStudentException, CannotAddStudentException;

    public String getSender(){
        return sender;
    }

    public Action getAction(){
        return action;
    }

    //For AddMeMessage:
    /*public boolean getCompleteRules(){
        return false;
    }
    public int getNumPlayers(){
        return -1;
    }

    //For PLAYCARDmessage:
    public int getPriority(){
        return -1;
    }

    //For MOVESTUDENTmessage:
    public int getStudentId(){
        return -1;
    }
    public Location getDepartureType(){
        return null;
    }
    public int getDepartureId(){
        return -1;
    }
    public Location getArrivalType(){
        return null;
    }
    public int getArrivalId(){
        return -1;
    }

    //For MOVEMOTHERNATUREmessage:
    public int getMovement(){
        return -1;
    }

    //For SELECTCLOUDmessage:
    public int getCloudPosition(){
        return -1;
    }

    //For USEPOWERmessage:
    public int getCharacterCard(){
        return -1;
    }

    //For EXCHANGESTUDENTmessage:
    public int getStudentId2(){
        return -1;
    }

    //For CHOOSECOLORmessage:
    public Color getChosenColor(){
        return null;
    }

    //For CHOOSEISLANDmessage:
    public int getIdIsland(){
        return -1;
    }*/
}
