package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.model.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the class Message an all his heirs
 */
public class MessageTest {

    /**
     * This method test the AddMeMessage
     */
    @Test
    public void ADDMEtest(){
        AddMeMessage message = new AddMeMessage("player", false, 3);
        assertFalse(message.getCompleteRules());
        assertEquals(3, message.getNumPlayers());
    }

    /**
     * This method test the PlayCardMessage
     */
    @Test
    public void PLAYCARDtest(){
        PlayCardMessage message = new PlayCardMessage("player", 2);
        assertEquals(2, message.getPriority());
    }

    /**
     * This method test the MoveStudentMessage
     */
    @Test
    public void MOVESTUDENTtest(){
        MoveStudentMessage message = new MoveStudentMessage("player", 2, Location.ISLAND, 0, Location.ISLAND, 1);
        assertEquals(2, message.getStudentId());
        assertEquals(Location.ISLAND, message.getDepartureType());
        assertEquals(0, message.getDepartureId());
        assertEquals(Location.ISLAND, message.getArrivalType());
        assertEquals(1, message.getArrivalId());
    }

    /**
     * This method test the MoveMotherNatureMessage
     */
    @Test
    public void MOVEMOTHERNATUREtest(){
        MoveMotherNatureMessage message = new MoveMotherNatureMessage("player", 2);
        assertEquals(2, message.getMovement());
    }

    /**
     * This method test the SelectCloudMessage
     */
    @Test
    public void SELECTCLOUDtest(){
        SelectCloudMessage message = new SelectCloudMessage("player", 2);
        assertEquals(2, message.getCloudPosition());
    }

    /**
     * This method test the UsePowerMessage
     */
    @Test
    public void USEPOWERtest(){
        UsePowerMessage message = new UsePowerMessage("player", 0);
        assertEquals(0, message.getCharacterCard());
    }

    /**
     * This method test the ExchangeStudentMessage
     */
    @Test
    public void EXCHANGESTUDENTtest(){
        ExchangeStudentMessage message = new ExchangeStudentMessage("player", 0, 1, Location.CANTEEN, 2, Location.ISLAND, 3);
        assertEquals(0, message.getStudentId());
        assertEquals(1, message.getStudentId2());
        assertEquals(Location.CANTEEN, message.getDepartureType());
        assertEquals(2, message.getDepartureId());
        assertEquals(Location.ISLAND, message.getArrivalType());
        assertEquals(3, message.getArrivalId());
    }

    /**
     * This method test the BlockColorMessage
     */
    @Test
    public void BLOCKCOLORtest(){
        BlockColorMessage message = new BlockColorMessage("player", Color.BLUE);
        assertEquals(Color.BLUE, message.getChosenColor());
    }

    /**
     * This method test the PutBackMessage
     */
    @Test
    public void PUTBACKtest(){
        PutBackMessage message = new PutBackMessage("player", Color.BLUE);
        assertEquals(Color.BLUE, message.getChosenColor());
    }

    /**
     * This method test the BlockIslandMessage
     */
    @Test
    public void BLOCKISLANDtest(){
        BlockIslandMessage message = new BlockIslandMessage("player", 0);
        assertEquals(0, message.getIdIsland());
    }

    /**
     * This method test the IslandInfluenceMessage
     */
    @Test
    public void ISLANDINFLUENCEtest(){
        IslandInfluenceMessage message = new IslandInfluenceMessage("player", 0);
        assertEquals(0, message.getIdIsland());
    }

    /**
     * This method test the PingMessage
     * @throws Exception
     */
    @Test
    public void PINGtest() throws Exception{
        PingMessage message = new PingMessage();
        assertNull(message.getSender());
        assertEquals(Action.PING, message.getAction());
        assertNull(message.execute(null));
    }
}