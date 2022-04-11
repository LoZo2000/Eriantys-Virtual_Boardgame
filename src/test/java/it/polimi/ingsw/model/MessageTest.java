package it.polimi.ingsw.model;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void MessageTest(){
        Message message = new Message("player", Action.ADDME);
        assertEquals("player", message.getSender());
        assertEquals(Action.ADDME, message.getAction());
        assertFalse(message.getCompleteRules());
        assertEquals(-1, message.getNumPlayers());
        assertEquals(-1, message.getPriority());
        assertEquals(-1, message.getStudentId());
        assertNull(message.getDepartureType());
        assertEquals(-1, message.getDepartureId());
        assertNull(message.getArrivalType());
        assertEquals(-1, message.getArrivalId());
        assertEquals(-1, message.getMovement());
        assertEquals(-1, message.getCloudPosition());
    }

    @Test
    public void CREATEMATCHtest(){
        Message message = new CREATEMATCHmessage("player", Action.CREATEMATCH, false, 2);
        assertFalse(message.getCompleteRules());
        assertEquals(2, message.getNumPlayers());
    }

    @Test
    public void PLAYCARDtest(){
        Message message = new PLAYCARDmessage("player", Action.PLAYCARD, 2);
        assertEquals(2, message.getPriority());
    }

    @Test
    public void MOVESTUDENTtest(){
        Message message = new MOVESTUDENTmessage("player", Action.MOVESTUDENT, 2, Location.ISLAND, 0, Location.ISLAND, 1);
        assertEquals(2, message.getStudentId());
        assertEquals(Location.ISLAND, message.getDepartureType());
        assertEquals(0, message.getDepartureId());
        assertEquals(Location.ISLAND, message.getArrivalType());
        assertEquals(1, message.getArrivalId());
    }

    @Test
    public void MOVEMOTHERNATUREtest(){
        Message message = new MOVEMOTHERNATUREmessage("player", Action.MOVEMOTHERNATURE, 2);
        assertEquals(2, message.getMovement());
    }

    @Test
    public void SELECTCLOUDtest(){
        Message message = new SELECTCLOUDmessage("player", Action.MOVEMOTHERNATURE, 2);
        assertEquals(2, message.getCloudPosition());
    }

    @Test
    public void SHOWMEtest(){
        Message message = new SHOWMEmessage("player", Action.SHOWME);
    }
}