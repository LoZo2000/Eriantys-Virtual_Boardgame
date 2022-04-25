package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.EndGameException;
import it.polimi.ingsw.model.exceptions.NoCharacterSelectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {
    Translator translator;

    @BeforeEach
    public void init() throws Exception{
        translator = new Translator(true, 3);
        Message message = new ADDMEmessage("player1", Action.ADDME, true, 3);
        translator.translateThis(message);
        message = new ADDMEmessage("player2", Action.ADDME, true, 3);
        translator.translateThis(message);
        message = new ADDMEmessage("player3", Action.ADDME, true, 3);
        translator.translateThis(message);
    }

    @Test
    public void PLAYCARDtest(){
        Message message = new PLAYCARDmessage("player1", Action.PLAYCARD, 2);
        try{
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new PLAYCARDmessage("player2", Action.PLAYCARD, 3);
        try{
            translator.translateThis(message);
        }catch(Exception e){
            fail();
        }
        message = new PLAYCARDmessage("player3", Action.PLAYCARD, 11);
        try{
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void MOVESTUDENTtest() throws Exception{
        ArrayList<Student> students = translator.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
        Message message = new MOVESTUDENTmessage("player1", Action.MOVESTUDENT, students.get(0).getId(), Location.ENTRANCE, 0, Location.ISLAND, 0);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            e.printStackTrace();
        }

        for(Location dep : Location.values()){
            for(Location arr : Location.values()){
                message = new MOVESTUDENTmessage("player1", Action.MOVESTUDENT, 0, dep, 0, arr, 0);
                try {
                    translator.translateThis(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void MOVESTUDENTpowertest() throws Exception{
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
        translator.getGame().setCharactersCards(characters);

        Player p1 = translator.getGame().getPlayer("player1");
        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        Message message = new MOVESTUDENTmessage("player1", Action.MOVESTUDENT, 31, Location.CARD_ISLAND, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new MOVESTUDENTmessage("player1", Action.MOVESTUDENT, 32, Location.CARD_ISLAND, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new MOVESTUDENTmessage("player1", Action.MOVESTUDENT, 32, Location.CARD_ISLAND, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void MOVEMOTHERNATUREtest(){
        Message message = new PLAYCARDmessage("player1", Action.PLAYCARD, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new MOVEMOTHERNATUREmessage("player1", Action.MOVEMOTHERNATURE, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void SELECTCARDtest(){
        Message message = new SELECTCLOUDmessage("player1", Action.SELECTCLOUD, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new SELECTCLOUDmessage("player3", Action.SELECTCLOUD, 9);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void SHOWMEtest(){
        Message message = new SHOWMEmessage("player1", Action.SHOWME);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void USEPOWERtest(){
        Message message = new USEPOWERmessage("player1", Action.USEPOWER, 0);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void EXCHANGESTUDENTtest() throws Exception{
        ArrayList<Student> students = translator.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
        ArrayList<Student> students1 = translator.getGame().getIsland(1).getAllStudents();
        Message message = new EXCHANGESTUDENTmessage("player1", Action.EXCHANGESTUDENT, students.get(0).getId(), students1.get(0).getId(), Location.ENTRANCE, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            e.printStackTrace();
        }

        for(Location dep : Location.values()){
            for(Location arr : Location.values()){
                message = new EXCHANGESTUDENTmessage("player1", Action.EXCHANGESTUDENT, 0, 1, dep, 0, arr, 0);
                try {
                    translator.translateThis(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void BLOCKISLANDtest() throws Exception{
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(5, CharacterType.ACTION, "Block island", 2, Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        translator.getGame().setCharactersCards(characters);

        Player p1 = translator.getGame().getPlayer("player1");
        p1.giveCoin();
        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        Message message = new ChooseIslandMessage("player1", Action.BLOCK_ISLAND, 0);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }

        p1.giveCoin();
        p1.giveCoin();
        p1.giveCoin();
        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        message = new ChooseIslandMessage("player1", Action.BLOCK_ISLAND, 13);
        try {
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void ISLANDINFLUENCEtest() throws Exception{
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(3, CharacterType.ACTION, "Calculate influence w/o moving MN", 3, Action.ISLAND_INFLUENCE, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        translator.getGame().setCharactersCards(characters);

        Player p1 = translator.getGame().getPlayer("player1");
        p1.giveCoin();
        p1.giveCoin();

        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        Message message = new ChooseIslandMessage("player1", Action.ISLAND_INFLUENCE, 0);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            e.printStackTrace();
        }

        p1.giveCoin();
        p1.giveCoin();
        p1.giveCoin();
        p1.giveCoin();
        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        message = new ChooseIslandMessage("player1", Action.ISLAND_INFLUENCE, 13);
        try {
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void BLOCKCOLORtest() throws Exception{
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(9, CharacterType.ACTION, "Block color", 3, Action.BLOCK_COLOR, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        translator.getGame().setCharactersCards(characters);

        Player p1 = translator.getGame().getPlayer("player1");
        p1.giveCoin();
        p1.giveCoin();
        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        Message message = new ChooseColorMessage("player1", Action.BLOCK_COLOR, Color.BLUE);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }

        message = new ChooseColorMessage("player4", Action.BLOCK_COLOR, Color.BLUE);
        try {
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void PUTBACKtest() throws Exception{
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(12, CharacterType.ACTION, "Put Back students", 3, Action.PUT_BACK, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        translator.getGame().setCharactersCards(characters);

        Player p1 = translator.getGame().getPlayer("player1");
        p1.giveCoin();
        p1.giveCoin();

        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        Message message = new ChooseColorMessage("player1", Action.PUT_BACK, Color.BLUE);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void gettersTest(){
        try{
            translator.getRequestedAction();
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            translator.getAllowedDepartures();
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            translator.getAllowedArrivals();
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            translator.getGame();
            translator.endTurn();
        }catch (Exception e){
            fail();
        }
    }

    @RepeatedTest(20)
    public void getWinnerTestGREY(){
        try {
            ArrayList<Student> students = translator.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
            Message message = new PLAYCARDmessage("player1", Action.PLAYCARD, 1);
            try {
                translator.translateThis(message);
            }catch (Exception e){
                fail();
            }
            for (int i = 0; i < 7; i++) {
                message = new MOVESTUDENTmessage("player1", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                translator.translateThis(message);
            }
            message = new MOVEMOTHERNATUREmessage("player1", Action.MOVEMOTHERNATURE, 1);
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        while(true){
            try {
                translator.endTurn();
            }catch (Exception e){
                if(e instanceof EndGameException) break;
            }
        }
    }
    @RepeatedTest(20)
    public void getWinnerTestBLACK(){
        try {
            ArrayList<Student> students = translator.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
            Message message = new PLAYCARDmessage("player2", Action.PLAYCARD, 1);
            try {
                translator.translateThis(message);
            }catch (Exception e){
                fail();
            }
            for (int i = 0; i < 7; i++) {
                message = new MOVESTUDENTmessage("player2", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                translator.translateThis(message);
            }
            message = new MOVEMOTHERNATUREmessage("player2", Action.MOVEMOTHERNATURE, 1);
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        while(true){
            try {
                translator.endTurn();
            }catch (Exception e){
                if(e instanceof EndGameException) break;
            }
        }
    }
    @RepeatedTest(20)
    public void getWinnerTestWHITE(){
        try {
            ArrayList<Student> students = translator.getGame().getPlayer("player3").getDashboard().getEntrance().getAllStudents();
            Message message = new PLAYCARDmessage("player3", Action.PLAYCARD, 1);
            try {
                translator.translateThis(message);
            }catch (Exception e){
                fail();
            }
            for (int i = 0; i < 7; i++) {
                message = new MOVESTUDENTmessage("player3", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                translator.translateThis(message);
            }
            message = new MOVEMOTHERNATUREmessage("player3", Action.MOVEMOTHERNATURE, 1);
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        while(true){
            try {
                translator.endTurn();
            }catch (Exception e){
                if(e instanceof EndGameException) break;
            }
        }
    }

    @Test
    public void playCardExceptions(){
        try{
            Message message = new PLAYCARDmessage("player1", Action.PLAYCARD, 11);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new PLAYCARDmessage("player1", Action.PLAYCARD, 5);
            translator.translateThis(message);
            message = new PLAYCARDmessage("player2", Action.PLAYCARD, 5);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new PLAYCARDmessage("player4", Action.PLAYCARD, 3);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void MoveMotherNatureExceptions(){
        try{
            Message message = new MOVEMOTHERNATUREmessage("player1", Action.MOVEMOTHERNATURE, 0);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new PLAYCARDmessage("player1", Action.PLAYCARD, 1);
            translator.translateThis(message);
            message = new MOVEMOTHERNATUREmessage("player1", Action.MOVEMOTHERNATURE, 2);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new MOVEMOTHERNATUREmessage("player4", Action.MOVEMOTHERNATURE, 1);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void showMeWithActiveCardTest(){
        try {
            Character[] characters = new Character[1];
            Set<Location> allowedDepartures = Set.of(Location.CARD_ISLAND);
            Set<Location> allowedArrivals = Set.of(Location.ISLAND);

            ArrayList<Student> students = new ArrayList<>();
            students.add(new Student(31, Color.RED));
            students.add(new Student(32, Color.BLUE));
            students.add(new Student(33, Color.GREEN));
            students.add(new Student(34, Color.PINK));

            JSONCharacter jc = new JSONCharacter(1, CharacterType.MOVEMENT, "Move from card to island", 1, Action.MOVESTUDENT, 4, Location.CARD_ISLAND, true, allowedDepartures, allowedArrivals, false, 0, 0);
            //JSONParams params = new JSONParams(Action.PUT_BACK, 0, null, false, null, null, false, 0, 0);
            characters[0] = new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), students, jc.getParams());
            translator.getGame().setCharactersCards(characters);

            Player p1 = translator.getGame().getPlayer("player1");
            try {
                translator.getGame().usePower(p1, 0);
            } catch (NoCharacterSelectedException ex) {
                fail();
            }

            Message message = new SHOWMEmessage("player1", Action.SHOWME);
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }
}