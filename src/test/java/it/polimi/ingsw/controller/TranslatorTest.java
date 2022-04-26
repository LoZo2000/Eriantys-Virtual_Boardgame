package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {
    Translator translator;

    private final String JSON_PATH = "characters.json";

    @BeforeEach
    public void init() throws Exception{
        translator = new Translator(true, 3);
        Message message = new AddMeMessage("player1", Action.ADDME, true, 3);
        translator.translateThis(message);
        message = new AddMeMessage("player2", Action.ADDME, true, 3);
        translator.translateThis(message);
        message = new AddMeMessage("player3", Action.ADDME, true, 3);
        translator.translateThis(message);
    }

    private Character getCharacterFromJSON(int id){
        Reader reader = null;
        try {
            reader = new FileReader(JSON_PATH);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JSONCharacter[] jsonCharacters = gson.fromJson(reader, JSONCharacter[].class);
        List<Character> allCharacters = new ArrayList<>();
        for (JSONCharacter jc : jsonCharacters) {
            switch (jc.getTypeCharacter()) {
                case MOVEMENT:
                    ArrayList<Student> s = new ArrayList<>();
                    if(jc.getParams().getNumThingOnIt() > 0) {
                        s.add(new Student(31, Color.RED));
                        s.add(new Student(32, Color.BLUE));
                        s.add(new Student(33, Color.GREEN));
                        s.add(new Student(34, Color.PINK));
                    }
                    if(jc.getParams().getNumThingOnIt() == 6) {
                        s.add(new Student(35, Color.BLUE));
                        s.add(new Student(36, Color.BLUE));
                    }
                    allCharacters.add(new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), s, jc.getParams()));
                    break;

                case INFLUENCE:
                    allCharacters.add(new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams()));
                    break;

                case PROFESSOR:
                    allCharacters.add(new ProfessorCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost()));
                    break;

                case MOTHERNATURE:
                    allCharacters.add(new MotherNatureCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams()));
                    break;

                case ACTION:
                    allCharacters.add(new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams()));
                    break;
            }
        }

        return allCharacters.get(id-1);
    }

    @Test
    public void PLAYCARDtest(){
        Message message = new PlayCardMessage("player1", Action.PLAYCARD, 2);
        try{
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new PlayCardMessage("player2", Action.PLAYCARD, 3);
        try{
            translator.translateThis(message);
        }catch(Exception e){
            fail();
        }
        message = new PlayCardMessage("player3", Action.PLAYCARD, 11);
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
        Message message = new MoveStudentMessage("player1", Action.MOVESTUDENT, students.get(0).getId(), Location.ENTRANCE, 0, Location.ISLAND, 0);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            e.printStackTrace();
        }

        for(Location dep : Location.values()){
            for(Location arr : Location.values()){
                message = new MoveStudentMessage("player1", Action.MOVESTUDENT, 0, dep, 0, arr, 0);
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
        characters[0] = getCharacterFromJSON(1);
        translator.getGame().setCharactersCards(characters);

        Player p1 = translator.getGame().getPlayer("player1");
        try {
            translator.getGame().usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        Message message = new MoveStudentMessage("player1", Action.MOVESTUDENT, 31, Location.CARD_ISLAND, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new MoveStudentMessage("player1", Action.MOVESTUDENT, 32, Location.CARD_ISLAND, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new MoveStudentMessage("player1", Action.MOVESTUDENT, 32, Location.CARD_ISLAND, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void MOVEMOTHERNATUREtest(){
        Message message = new PlayCardMessage("player1", Action.PLAYCARD, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new MoveMotherNatureMessage("player1", Action.MOVEMOTHERNATURE, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void SELECTCARDtest(){
        Message message = new SelectCloudMessage("player1", Action.SELECTCLOUD, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
        message = new SelectCloudMessage("player3", Action.SELECTCLOUD, 9);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void SHOWMEtest(){
        Message message = new ShowMeMessage("player1", Action.SHOWME);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void USEPOWERtest(){
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
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
        Message message = new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, students.get(0).getId(), students1.get(0).getId(), Location.ENTRANCE, 0, Location.ISLAND, 1);
        try {
            translator.translateThis(message);
        }catch (Exception e){
            e.printStackTrace();
        }

        for(Location dep : Location.values()){
            for(Location arr : Location.values()){
                message = new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, 0, 1, dep, 0, arr, 0);
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
        characters[0] = getCharacterFromJSON(5);
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
        characters[0] = getCharacterFromJSON(3);
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
        characters[0] = getCharacterFromJSON(9);
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
        characters[0] = getCharacterFromJSON(12);
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
            Message message = new PlayCardMessage("player1", Action.PLAYCARD, 1);
            try {
                translator.translateThis(message);
            }catch (Exception e){
                fail();
            }
            for (int i = 0; i < 7; i++) {
                message = new MoveStudentMessage("player1", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                translator.translateThis(message);
            }
            message = new MoveMotherNatureMessage("player1", Action.MOVEMOTHERNATURE, 1);
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
            Message message = new PlayCardMessage("player2", Action.PLAYCARD, 1);
            try {
                translator.translateThis(message);
            }catch (Exception e){
                fail();
            }
            for (int i = 0; i < 7; i++) {
                message = new MoveStudentMessage("player2", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                translator.translateThis(message);
            }
            message = new MoveMotherNatureMessage("player2", Action.MOVEMOTHERNATURE, 1);
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
            Message message = new PlayCardMessage("player3", Action.PLAYCARD, 1);
            try {
                translator.translateThis(message);
            }catch (Exception e){
                fail();
            }
            for (int i = 0; i < 7; i++) {
                message = new MoveStudentMessage("player3", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                translator.translateThis(message);
            }
            message = new MoveMotherNatureMessage("player3", Action.MOVEMOTHERNATURE, 1);
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
            Message message = new PlayCardMessage("player1", Action.PLAYCARD, 11);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new PlayCardMessage("player1", Action.PLAYCARD, 5);
            translator.translateThis(message);
            message = new PlayCardMessage("player2", Action.PLAYCARD, 5);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new PlayCardMessage("player4", Action.PLAYCARD, 3);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void MoveMotherNatureExceptions(){
        try{
            Message message = new MoveMotherNatureMessage("player1", Action.MOVEMOTHERNATURE, 0);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new PlayCardMessage("player1", Action.PLAYCARD, 1);
            translator.translateThis(message);
            message = new MoveMotherNatureMessage("player1", Action.MOVEMOTHERNATURE, 2);
            translator.translateThis(message);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Message message = new MoveMotherNatureMessage("player4", Action.MOVEMOTHERNATURE, 1);
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
            characters[0] = getCharacterFromJSON(1);
            translator.getGame().setCharactersCards(characters);

            Player p1 = translator.getGame().getPlayer("player1");
            try {
                translator.getGame().usePower(p1, 0);
            } catch (NoCharacterSelectedException ex) {
                fail();
            }

            Message message = new ShowMeMessage("player1", Action.SHOWME);
            translator.translateThis(message);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void noCharacterSelectedTest(){
        assertThrows(NoCharacterSelectedException.class, ()->translator.translateThis(new UsePowerMessage("player1", Action.USEPOWER, 8)));
    }
}