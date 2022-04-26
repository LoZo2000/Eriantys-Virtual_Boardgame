package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.characters.CharacterType;
import it.polimi.ingsw.model.characters.JSONCharacter;
import it.polimi.ingsw.model.characters.MovementCharacter;
import it.polimi.ingsw.model.exceptions.NoCharacterSelectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

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

    @RepeatedTest(40)
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

    /*@Test
    public void refillSimulation() throws Exception{
        Character[] characters = new Character[1];
        Set<Location> allowedDepartures = Set.of(Location.CARD_ISLAND);
        Set<Location> allowedArrivals = Set.of(Location.ISLAND);

        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(31, Color.RED));
        students.add(new Student(32, Color.BLUE));
        students.add(new Student(33, Color.GREEN));
        students.add(new Student(34, Color.PINK));

        JSONCharacter jc = new JSONCharacter(1, CharacterType.MOVEMENT, "Move from card to island", 1, Action.MOVESTUDENT, 4, Location.CARD_ISLAND, true, allowedDepartures, allowedArrivals, false, 0, 0);
        characters[0] = new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), students, jc.getParams());
        gameHandler.getGame().setCharactersCards(characters);

        Message message;
        message = new ADDMEmessage("player1", Action.ADDME, true, 2);
        gameHandler.execute(message);
        message = new ADDMEmessage("player2", Action.ADDME, true, 2);
        gameHandler.execute(message);

        message = new PLAYCARDmessage("player1", Action.PLAYCARD, 2);
        gameHandler.execute(message);
        message = new PLAYCARDmessage("player2", Action.PLAYCARD, 1);
        gameHandler.execute(message);

        Player p2 = gameHandler.getGame().getPlayer("player2");
        try {
            gameHandler.getGame().usePower(p2, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        //students = gameHandler.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
        try {
            message = new MOVESTUDENTmessage("player2", Action.MOVESTUDENT, 31, Location.CARD_ISLAND, 0, Location.ISLAND, 1);
            gameHandler.execute(message);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        /*try {
            message = new MOVESTUDENTmessage("player2", Action.MOVESTUDENT, students.get(1).getId(), Location.ENTRANCE, 0, Location.ISLAND, 1);
            gameHandler.execute(message);
        }catch (Exception e){
            System.out.println("as23r32r");
            e.printStackTrace();
        }
    }*/
}