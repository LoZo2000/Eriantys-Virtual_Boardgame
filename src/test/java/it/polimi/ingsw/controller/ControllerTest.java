package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.AddMeMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.SelectCloudMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *This class contains the controller tests
 */
public class ControllerTest {
    Controller controller;

    /**
     * This method is called before each test and create a new Game, GameManager and Controller
     */
    @BeforeEach
    public void init(){
        Game game = new Game(true, 2);
        GameManager gameManager = new GameManager();
        controller = new Controller(game, gameManager);
    }

    /**
     * This method test the controller adding two players to the game and the execution of a message
     */
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

    /**
     * This method tests the getters of the class Controller
     */
    @Test
    public void getterTest(){
        assertEquals(true, controller.getCompleteRules());
        assertEquals(2, controller.getNumPlayers());
    }
}