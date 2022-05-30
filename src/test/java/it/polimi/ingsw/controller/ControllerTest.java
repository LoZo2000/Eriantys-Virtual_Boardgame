package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.AddMeMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.SelectCloudMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    Controller controller;

    @BeforeEach
    public void init(){
        Game game = new Game(true, 2);
        GameManager gameManager = new GameManager();
        controller = new Controller(game, gameManager);
    }

    @Test
    public void test(){
        Message mes = new AddMeMessage("player1", true, 2);
        controller.update(mes);
        mes = new AddMeMessage("player2", true, 2);
        controller.update(mes);
        mes = new SelectCloudMessage("player1", 0);
        controller.update(mes);

        assertEquals("player1", controller.getNicknames().get(0));
        assertEquals("player2", controller.getNicknames().get(1));

        controller.disconnectedPlayer("player1");
    }
}