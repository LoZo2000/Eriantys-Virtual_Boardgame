package it.polimi.ingsw.message;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.model.Color;
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
        assertEquals(-1, message.getCharacterCard());
        assertEquals(-1, message.getStudentId2());
        assertNull(message.getChosenColor());
        assertEquals(-1, message.getIdIsland());
    }

    @Test
    public void CREATEMATCHtest(){
        Message message = new CreateMatchMessage("player", Action.CREATEMATCH, false, 2);
        assertFalse(message.getCompleteRules());
        assertEquals(2, message.getNumPlayers());
    }

    @Test
    public void ADDMEtest(){
        Message message = new AddMeMessage("player", Action.ADDME, false, 3);
        assertFalse(message.getCompleteRules());
        assertEquals(3, message.getNumPlayers());
    }

    @Test
    public void PLAYCARDtest(){
        Message message = new PlayCardMessage("player", Action.PLAYCARD, 2);
        assertEquals(2, message.getPriority());
    }

    @Test
    public void MOVESTUDENTtest(){
        Message message = new MoveStudentMessage("player", Action.MOVESTUDENT, 2, Location.ISLAND, 0, Location.ISLAND, 1);
        assertEquals(2, message.getStudentId());
        assertEquals(Location.ISLAND, message.getDepartureType());
        assertEquals(0, message.getDepartureId());
        assertEquals(Location.ISLAND, message.getArrivalType());
        assertEquals(1, message.getArrivalId());
    }

    @Test
    public void MOVEMOTHERNATUREtest(){
        Message message = new MoveMotherNatureMessage("player", Action.MOVEMOTHERNATURE, 2);
        assertEquals(2, message.getMovement());
    }

    @Test
    public void SELECTCLOUDtest(){
        Message message = new SelectCloudMessage("player", Action.MOVEMOTHERNATURE, 2);
        assertEquals(2, message.getCloudPosition());
    }

    @Test
    public void SHOWMEtest(){
        Message message = new ShowMeMessage("player", Action.SHOWME);
    }

    @Test
    public void USEPOWERtest(){
        Message message = new UsePowerMessage("player", Action.USEPOWER, 0);
        assertEquals(0, message.getCharacterCard());
    }

    @Test
    public void EXCHANGESTUDENTtest(){
        Message message = new ExchangeStudentMessage("player", Action.EXCHANGESTUDENT, 0, 1, Location.CANTEEN, 2, Location.ISLAND, 3);
        assertEquals(0, message.getStudentId());
        assertEquals(1, message.getStudentId2());
        assertEquals(Location.CANTEEN, message.getDepartureType());
        assertEquals(2, message.getDepartureId());
        assertEquals(Location.ISLAND, message.getArrivalType());
        assertEquals(3, message.getArrivalId());
    }

    @Test
    public void CHOOSECOLORtest(){
        Message message = new ChooseColorMessage("player", Action.BLOCK_COLOR, Color.BLUE);
        assertEquals(Color.BLUE, message.getChosenColor());
    }

    @Test
    public void CHOOSEISLANDtest(){
        Message message = new ChooseIslandMessage("player", Action.BLOCK_ISLAND, 0);
        assertEquals(0, message.getIdIsland());
    }

    @Test
    public void ENDGAMEtest(){
        Message message = new ENDGAMEmessage("player", Action.ENDGAME);
    }
}