package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class GameHandlerTest {
    GameHandler2 gameHandler;

    @BeforeEach
    public void init(){
        Message message = new CREATEMATCHmessage("player1", Action.CREATEMATCH, true, 3);
        try{
            gameHandler = new GameHandler2(message);
        }catch (Exception e){
            fail();
        }
        message = new CREATEMATCHmessage("player1", Action.CREATEMATCH, true, 2);
        try{
            gameHandler = new GameHandler2(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void getterTest(){
        gameHandler.getGame();
        gameHandler.getPhase();
        gameHandler.getActivePlayers();
    }

    @Test
    public void gameSimulation(){
        Message message;
        ArrayList<Student> students;
        try {
            message = new ADDMEmessage("player1", Action.ADDME, true, 2);
            gameHandler.execute(message);
            message = new ADDMEmessage("player2", Action.ADDME, true, 2);
            gameHandler.execute(message);

            message = new PLAYCARDmessage("player1", Action.PLAYCARD, 2);
            gameHandler.execute(message);
            message = new PLAYCARDmessage("player2", Action.PLAYCARD, 1);
            gameHandler.execute(message);

            students = gameHandler.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
            for(int i=0; i<3; i++){
                message = new MOVESTUDENTmessage("player2", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                gameHandler.execute(message);
            }

            message = new MOVEMOTHERNATUREmessage("player2", Action.MOVEMOTHERNATURE, 1);
            gameHandler.execute(message);

            message = new SELECTCLOUDmessage("player2", Action.SELECTCLOUD, 0);
            gameHandler.execute(message);

            students = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
            for(int i=0; i<3; i++){
                message = new MOVESTUDENTmessage("player1", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                gameHandler.execute(message);
            }

            message = new MOVEMOTHERNATUREmessage("player1", Action.MOVEMOTHERNATURE, 1);
            gameHandler.execute(message);

            message = new SELECTCLOUDmessage("player1", Action.SELECTCLOUD, 1);
            gameHandler.execute(message);

            message = new SHOWMEmessage("player2", Action.SHOWME);
            gameHandler.execute(message);

            message = new PLAYCARDmessage("player2", Action.PLAYCARD, 2);
            gameHandler.execute(message);
            message = new PLAYCARDmessage("player1", Action.PLAYCARD, 3);
            gameHandler.execute(message);
        }catch (Exception e){
            fail();
        }
    }

    @RepeatedTest(10)
    public void activePowerTest(){
        Message message;
        ArrayList<Student> students;
        try{
            message = new ADDMEmessage("player1", Action.ADDME, true, 2);
            gameHandler.execute(message);
            message = new ADDMEmessage("player2", Action.ADDME, true, 2);
            gameHandler.execute(message);

            message = new PLAYCARDmessage("player1", Action.PLAYCARD, 2);
            gameHandler.execute(message);
            message = new PLAYCARDmessage("player2", Action.PLAYCARD, 1);
            gameHandler.execute(message);

            message = new USEPOWERmessage("player2", Action.USEPOWER, 0);
            gameHandler.execute(message);

            students = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
            ArrayList<Student> students2 = gameHandler.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
            message = new EXCHANGESTUDENTmessage("player2", Action.EXCHANGESTUDENT, students.get(0).getId(), students2.get(0).getId(), Location.ENTRANCE,0, Location.ENTRANCE, 0);
            gameHandler.execute(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}