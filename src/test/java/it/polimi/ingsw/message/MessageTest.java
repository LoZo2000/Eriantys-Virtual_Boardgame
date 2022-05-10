package it.polimi.ingsw.message;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void CREATEMATCHtest(){
        CreateMatchMessage message = new CreateMatchMessage("player", false, 2);
        assertFalse(message.getCompleteRules());
        assertEquals(2, message.getNumPlayers());
        assertNull(message.execute(null));
    }

    @Test
    public void ADDMEtest(){
        AddMeMessage message = new AddMeMessage("player", false, 3);
        assertFalse(message.getCompleteRules());
        assertEquals(3, message.getNumPlayers());
    }

    @Test
    public void PLAYCARDtest(){
        PlayCardMessage message = new PlayCardMessage("player", 2);
        assertEquals(2, message.getPriority());
    }

    @Test
    public void MOVESTUDENTtest(){
        MoveStudentMessage message = new MoveStudentMessage("player", 2, Location.ISLAND, 0, Location.ISLAND, 1);
        assertEquals(2, message.getStudentId());
        assertEquals(Location.ISLAND, message.getDepartureType());
        assertEquals(0, message.getDepartureId());
        assertEquals(Location.ISLAND, message.getArrivalType());
        assertEquals(1, message.getArrivalId());
    }

    @Test
    public void MOVEMOTHERNATUREtest(){
        MoveMotherNatureMessage message = new MoveMotherNatureMessage("player", 2);
        assertEquals(2, message.getMovement());
    }

    @Test
    public void SELECTCLOUDtest(){
        SelectCloudMessage message = new SelectCloudMessage("player", 2);
        assertEquals(2, message.getCloudPosition());
    }

    @Test
    public void SHOWMEtest(){
        ShowMeMessage message = new ShowMeMessage("player", Action.SHOWME);
        assertNull(message.execute(null));
    }

    @Test
    public void USEPOWERtest(){
        UsePowerMessage message = new UsePowerMessage("player", 0);
        assertEquals(0, message.getCharacterCard());
    }

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

    @Test
    public void BLOCKCOLORtest(){
        BlockColorMessage message = new BlockColorMessage("player", Color.BLUE);
        assertEquals(Color.BLUE, message.getChosenColor());
    }

    @Test
    public void PUTBACKtest(){
        PutBackMessage message = new PutBackMessage("player", Color.BLUE);
        assertEquals(Color.BLUE, message.getChosenColor());
    }

    @Test
    public void BLOCKISLANDtest(){
        BlockIslandMessage message = new BlockIslandMessage("player", 0);
        assertEquals(0, message.getIdIsland());
    }

    @Test
    public void ISLANDINFLUENCEtest(){
        IslandInfluenceMessage message = new IslandInfluenceMessage("player", 0);
        assertEquals(0, message.getIdIsland());
    }

    @Test
    public void ENDGAMEtest(){
        EndGameMessage message = new EndGameMessage("player", Action.ENDGAME);
    }
}