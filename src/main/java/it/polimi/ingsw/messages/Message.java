package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;

public class Message {
    final private String sender;
    final private Action action;

    public Message(String sender, Action action){
        this.sender = sender;
        this.action = action;
    }

    public String getSender(){
        return sender;
    }

    public Action getAction(){
        return action;
    }

    //For ADDMEmessage:
    public boolean getCompleteRules(){
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
}
