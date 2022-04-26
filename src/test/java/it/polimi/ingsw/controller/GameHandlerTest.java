package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.rules.InfluenceRule;
import it.polimi.ingsw.model.rules.MotherNatureRule;
import it.polimi.ingsw.model.rules.ProfessorRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameHandlerTest {
    GameHandler2 gameHandler;

    @BeforeEach
    public void init(){
        Message message = new CreateMatchMessage("player1", Action.CREATEMATCH, true, 3);
        try{
            gameHandler = new GameHandler2(message);
        }catch (Exception e){
            fail();
        }
        message = new CreateMatchMessage("player1", Action.CREATEMATCH, true, 2);
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
            message = new AddMeMessage("player1", Action.ADDME, true, 2);
            gameHandler.execute(message);
            message = new AddMeMessage("player2", Action.ADDME, true, 2);
            gameHandler.execute(message);

            message = new PlayCardMessage("player1", Action.PLAYCARD, 2);
            gameHandler.execute(message);
            message = new PlayCardMessage("player2", Action.PLAYCARD, 1);
            gameHandler.execute(message);

            students = gameHandler.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
            assertThrows(IllegalMoveException.class, ()->gameHandler.execute(new MoveStudentMessage("player2", Action.MOVESTUDENT, 2, Location.CANTEEN, -1, Location.ENTRANCE, -1)));
            for(int i=0; i<3; i++){
                message = new MoveStudentMessage("player2", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                gameHandler.execute(message);
            }

            message = new MoveMotherNatureMessage("player2", Action.MOVEMOTHERNATURE, 1);
            gameHandler.execute(message);

            message = new SelectCloudMessage("player2", Action.SELECTCLOUD, 0);
            gameHandler.execute(message);

            students = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
            for(int i=0; i<3; i++){
                message = new MoveStudentMessage("player1", Action.MOVESTUDENT, students.get(i).getId(), Location.ENTRANCE, 0, Location.CANTEEN, 0);
                gameHandler.execute(message);
            }

            message = new MoveMotherNatureMessage("player1", Action.MOVEMOTHERNATURE, 1);
            gameHandler.execute(message);

            message = new SelectCloudMessage("player1", Action.SELECTCLOUD, 1);
            gameHandler.execute(message);

            message = new ShowMeMessage("player2", Action.SHOWME);
            gameHandler.execute(message);

            message = new PlayCardMessage("player2", Action.PLAYCARD, 2);
            gameHandler.execute(message);
            message = new PlayCardMessage("player1", Action.PLAYCARD, 3);
            gameHandler.execute(message);
        }catch (Exception e){
            fail();
        }
    }

    @RepeatedTest(300)
    public void activePowerTest(){
        Message message;
        ArrayList<Student> students;
        try{
            message = new AddMeMessage("player1", Action.ADDME, true, 2);
            gameHandler.execute(message);
            message = new AddMeMessage("player2", Action.ADDME, true, 2);
            gameHandler.execute(message);

            //Add coins to avoid problems
            for(int i = 0; i<19; i++){
                gameHandler.getGame().getPlayer("player1").giveCoin();
            }

            message = new PlayCardMessage("player1", Action.PLAYCARD, 1);
            gameHandler.execute(message);
            message = new PlayCardMessage("player2", Action.PLAYCARD, 2);
            gameHandler.execute(message);

            Character c = gameHandler.getGame().getCharactersCards()[0];
            int idCharacter = c.getId();

            switch (idCharacter) {
                case 1 -> {
                    testCard1((MovementCharacter) c);
                }
                case 2 -> testCard2((ProfessorCharacter) c);
                case 3 -> testCard3((ActionCharacter) c);
                case 4 -> testCard4((MotherNatureCharacter) c);
                case 5 -> testCard5((ActionCharacter) c);
                case 6 -> testCard6((InfluenceCharacter) c);
                case 7 -> testCard7((MovementCharacter) c);
                case 8 -> testCard8((InfluenceCharacter) c);
                case 9 -> testCard9((ActionCharacter) c);
                case 10 -> testCard10((MovementCharacter) c);
                case 11 -> testCard11((MovementCharacter) c);
                case 12 -> testCard12((ActionCharacter) c);
            }

            /*message = new USEPOWERmessage("player1", Action.USEPOWER, 0);
            gameHandler.execute(message);

            students = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
            ArrayList<Student> students2 = gameHandler.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
            message = new EXCHANGESTUDENTmessage("player2", Action.EXCHANGESTUDENT, students.get(0).getId(), students2.get(0).getId(), Location.ENTRANCE,0, Location.ENTRANCE, 0);
            gameHandler.execute(message);*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void testCard1(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, 1, 2, Location.ENTRANCE, -1, Location.CANTEEN, -1))
        );

        ArrayList<Student> students = c.getStudents();
        Student s = students.get(0);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new MoveStudentMessage("player1", Action.MOVESTUDENT, s.getId(), Location.ENTRANCE, -1, Location.ISLAND, 1)),
        "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", Action.MOVESTUDENT, s.getId(), Location.CARD_ISLAND, -1, Location.ENTRANCE, -1)),
                "Wrong Arrival");

        Message m = new MoveStudentMessage("player1", Action.MOVESTUDENT, s.getId(), Location.CARD_ISLAND, -1, Location.ISLAND, 1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getIsland(1).getAllStudents().contains(s));
        assertEquals(4, c.getStudents().size());

        System.out.println("Card 1");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard2(ProfessorCharacter c) throws Exception {
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        System.out.println("Card 2");
        assertEquals(ProfessorRule.class, gameHandler.getGame().getCurrentRule().getClass());

        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard3(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new MoveStudentMessage("player1", Action.MOVESTUDENT, 1,  Location.ENTRANCE, -1, Location.CANTEEN, -1)),
        "You can't move students with the card that calculate influence of an island without moving Mother Nature");

        Island motherNatureIsland = gameHandler.getGame().getMotherNaturePosition();
        Entrance e = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance();
        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();

        e.addStudent(new Student(131, Color.RED));
        gameHandler.getGame().moveStudent(131, canteen, e);

        gameHandler.getGame().getIsland(1).addStudent(new Student(132, Color.RED));

        Message m = new ChooseIslandMessage("player1", Action.ISLAND_INFLUENCE, 1);
        gameHandler.execute(m);

        assertEquals(motherNatureIsland, gameHandler.getGame().getMotherNaturePosition());
        assertEquals(ColorTower.BLACK, gameHandler.getGame().getIsland(1).getOwner());

        System.out.println("Card 3");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard4(MotherNatureCharacter c) throws Exception {
        assertEquals(0, gameHandler.getGame().getMotherNatureExtraMovement());

        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        System.out.println("Card 4");
        assertEquals(2, gameHandler.getGame().getMotherNatureExtraMovement());
        assertEquals(MotherNatureRule.class, gameHandler.getGame().getCurrentRule().getClass());

        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard5(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ChooseIslandMessage("player1", Action.ISLAND_INFLUENCE, 1)),
                "You can't calculate influence of an island without moving Mother Nature with the card that block an island");

        Entrance e = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance();
        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();
        Island i = gameHandler.getGame().getIsland(1);

        e.addStudent(new Student(131, Color.RED));
        gameHandler.getGame().moveStudent(131, canteen, e);

        i.addStudent(new Student(132, Color.RED));

        Message m = new ChooseIslandMessage("player1", Action.BLOCK_ISLAND, 1);
        gameHandler.execute(m);

        assertTrue(i.getProhibition());

        gameHandler.getGame().moveMotherNature(i, true);

        assertEquals(i, gameHandler.getGame().getMotherNaturePosition());
        assertFalse(i.getProhibition());
        assertNull(i.getOwner());

        gameHandler.getGame().moveMotherNature(i, true);
        assertEquals(ColorTower.BLACK, i.getOwner());

        System.out.println("Card 5");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard6(InfluenceCharacter c) throws Exception {
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertEquals(InfluenceRule.class, gameHandler.getGame().getCurrentRule().getClass());
        InfluenceRule rule = (InfluenceRule) gameHandler.getGame().getCurrentRule();

        assertEquals(0, rule.getExtraPoints());
        assertTrue(rule.isDisableTowers());
        assertEquals(ColorTower.BLACK, rule.getPlayerWhoUsed());
        assertNull(rule.getBlockedColor());

        System.out.println("Card 6");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard7(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ChooseColorMessage("player1", Action.BLOCK_COLOR, Color.RED))
        );

        ArrayList<Student> students1 = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
        Student s1 = students1.get(0);

        ArrayList<Student> students2 = c.getStudents();
        Student s2 = students2.get(0);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.CANTEEN, -1, Location.CARD_EXCHANGE, -1)),
                "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.CANTEEN, -1)),
                "Wrong Arrival");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.ENTRANCE, -1)),
                "Same Arrival and Departure");

        Message m = new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.CARD_EXCHANGE, -1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents().contains(s2));
        assertTrue(c.getStudents().contains(s1));
        assertEquals(6, c.getStudents().size());

        System.out.println("Card 7");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard8(InfluenceCharacter c) throws Exception {
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertEquals(InfluenceRule.class, gameHandler.getGame().getCurrentRule().getClass());
        InfluenceRule rule = (InfluenceRule) gameHandler.getGame().getCurrentRule();

        assertEquals(2, rule.getExtraPoints());
        assertFalse(rule.isDisableTowers());
        assertEquals(ColorTower.BLACK, rule.getPlayerWhoUsed());
        assertNull(rule.getBlockedColor());

        System.out.println("Card 8");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard9(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ChooseIslandMessage("player1", Action.BLOCK_ISLAND, 1)),
                "You can't block an island with the card that block a color");

        Entrance e = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance();
        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();
        Island i = gameHandler.getGame().getIsland(1);

        e.addStudent(new Student(131, Color.RED));
        gameHandler.getGame().moveStudent(131, canteen, e);

        i.addStudent(new Student(132, Color.RED));

        Message m = new ChooseColorMessage("player1", Action.BLOCK_COLOR, Color.RED);
        gameHandler.execute(m);

        assertEquals(InfluenceRule.class, gameHandler.getGame().getCurrentRule().getClass());
        InfluenceRule rule = (InfluenceRule) gameHandler.getGame().getCurrentRule();

        assertEquals(ColorTower.BLACK, rule.getPlayerWhoUsed());
        assertEquals(Color.RED, rule.getBlockedColor());
        assertFalse(rule.isDisableTowers());
        assertEquals(0, rule.getExtraPoints());

        gameHandler.getGame().moveMotherNature(i, true);

        assertEquals(i, gameHandler.getGame().getMotherNaturePosition());
        assertNull(i.getOwner());

        System.out.println("Card 9");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard10(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ChooseColorMessage("player1", Action.PUT_BACK, Color.RED))
        );

        ArrayList<Student> students1 = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
        Student s1 = students1.get(0);

        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();
        canteen.addStudent(new Student(131, s1.getColor()));
        canteen.canGetCoin(Color.RED, true);

        ArrayList<Student> students2 = canteen.getStudents(s1.getColor());
        Student s2 = students2.get(0);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.CANTEEN, -1, Location.CARD_EXCHANGE, -1)),
                "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.ISLAND, 1)),
                "Wrong Arrival");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.ENTRANCE, -1)),
                "Same Arrival and Departure");

        Message m = new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.CANTEEN, -1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents().contains(s2));
        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen().getStudents(s1.getColor()).contains(s1));
        assertEquals(0, c.getStudents().size());

        System.out.println("Card 10");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard11(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ExchangeStudentMessage("player1", Action.EXCHANGESTUDENT, 1, 2, Location.ENTRANCE, -1, Location.CANTEEN, -1))
        );

        ArrayList<Student> students = c.getStudents();
        Student s = students.get(0);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", Action.MOVESTUDENT, s.getId(), Location.ENTRANCE, -1, Location.ISLAND, 1)),
                "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", Action.MOVESTUDENT, s.getId(), Location.CARD_ISLAND, -1, Location.ENTRANCE, -1)),
                "Wrong Arrival");

        Message m = new MoveStudentMessage("player1", Action.MOVESTUDENT, s.getId(), Location.CARD_CANTEEN, -1, Location.CANTEEN, -1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen().getStudents(s.getColor()).contains(s));
        assertEquals(4, c.getStudents().size());

        System.out.println("Card 11");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
    }

    private void testCard12(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", Action.USEPOWER, 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ChooseColorMessage("player1", Action.BLOCK_COLOR, Color.RED))
        );

        Canteen canteen1 = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();
        canteen1.addStudent(new Student(131, Color.RED));
        canteen1.canGetCoin(Color.RED, true);
        canteen1.addStudent(new Student(132, Color.RED));
        canteen1.canGetCoin(Color.RED, true);
        canteen1.addStudent(new Student(133, Color.RED));
        canteen1.canGetCoin(Color.RED, true);

        Canteen canteen2 = gameHandler.getGame().getPlayer("player2").getDashboard().getCanteen();
        canteen2.addStudent(new Student(134, Color.RED));
        canteen2.canGetCoin(Color.RED, true);
        canteen2.addStudent(new Student(135, Color.RED));
        canteen2.canGetCoin(Color.RED, true);
        canteen2.addStudent(new Student(136, Color.RED));
        canteen2.canGetCoin(Color.RED, true);

        assertEquals(3, gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen().getNumberStudentColor(Color.RED));
        assertEquals(3, gameHandler.getGame().getPlayer("player2").getDashboard().getCanteen().getNumberStudentColor(Color.RED));

        Message m = new ChooseColorMessage("player1", Action.PUT_BACK, Color.RED);
        gameHandler.execute(m);

        assertEquals(0, gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen().getNumberStudentColor(Color.RED));
        assertEquals(0, gameHandler.getGame().getPlayer("player2").getDashboard().getCanteen().getNumberStudentColor(Color.RED));

        System.out.println("Card 12");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", Action.USEPOWER, 0)));
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
        try {
            message = new AddMeMessage("player1", Action.ADDME, true, 2);
            gameHandler.execute(message);
            message = new AddMeMessage("player2", Action.ADDME, true, 2);
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

            students = gameHandler.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
            try {
                message = new MOVESTUDENTmessage("player2", Action.MOVESTUDENT, students.get(0).getId(), Location.CARD_ISLAND, 0, Location.ISLAND, 1);
                gameHandler.execute(message);
            }catch (Exception e){
                fail();
            }

            try {
                message = new MOVESTUDENTmessage("player2", Action.MOVESTUDENT, students.get(1).getId(), Location.ENTRANCE, 0, Location.ISLAND, 1);
                gameHandler.execute(message);
            }catch (Exception e){
                System.out.println("as23r32r");
                e.printStackTrace();
            }

            /*students = gameHandler.getGame().getPlayer("player2").getDashboard().getEntrance().getAllStudents();
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
    }*/
}