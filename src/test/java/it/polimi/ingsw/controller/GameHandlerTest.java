package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalMessageException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NoIslandException;
import it.polimi.ingsw.model.exceptions.NotYourTurnException;
import it.polimi.ingsw.model.rules.InfluenceRule;
import it.polimi.ingsw.model.rules.MotherNatureRule;
import it.polimi.ingsw.model.rules.ProfessorRule;
import it.polimi.ingsw.view.Columns;
import it.polimi.ingsw.view.GameReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class GameHandlerTest {
    private GameHandler gameHandler;

    @BeforeEach
    public void init(){
        Game game = new Game(true, 3);
        try{
            gameHandler = new GameHandler(game);
        }catch (Exception e){
            fail();
        }
        assertEquals(0, gameHandler.getActivePlayers());
    }

    private void init2Players(){
        Game game = new Game(true, 2);
        try{
            gameHandler = new GameHandler(game);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void addPlayersTest() throws Exception{
        ArrayList<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");

        addPlayers();

        assertEquals(players, gameHandler.getPlayers());
        assertEquals(Phase.PRETURN, gameHandler.getPhase());

        Message m = new AddMeMessage("player4", true, 3);
        assertThrows(NotYourTurnException.class, () -> gameHandler.execute(m));
    }

    private void addPlayers() throws Exception{
        Message message;

        message = new AddMeMessage("player1", true, 3);
        gameHandler.execute(message);

        Message wrongMessage = new PlayCardMessage("player1", 5);
        assertThrows(IllegalActionException.class, () -> gameHandler.execute(wrongMessage), "Wrong Action");

        assertEquals(1, gameHandler.getActivePlayers());

        message = new AddMeMessage("player2", true, 3);
        gameHandler.execute(message);

        assertEquals(2, gameHandler.getActivePlayers());

        message = new AddMeMessage("player3", true, 3);
        gameHandler.execute(message);

        assertEquals(3, gameHandler.getActivePlayers());
    }

    @Test
    public void playCardsTest() throws Exception{
        ArrayList<String> players = new ArrayList<>();

        addPlayers();
        playCards();

        players.add("player3");
        players.add("player1");
        players.add("player2");

        assertEquals(players, gameHandler.getPlayers());
        assertEquals(Phase.MIDDLETURN, gameHandler.getPhase());
    }

    private void playCards() throws Exception{
        Message message;

        Message wrongMessage = new PlayCardMessage("player1", -1);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage), "Play a card that doesn't exist");

        Message wrongMessage2 = new PlayCardMessage("player1", 15);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage2), "Play a card that doesn't exist");

        message = new PlayCardMessage("player1", 5);
        gameHandler.execute(message);

        Message wrongMessage3 = new PlayCardMessage("player2", 5);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage3), "Play an already played card");

        message = new PlayCardMessage("player2", 7);
        gameHandler.execute(message);

        message = new PlayCardMessage("player3", 2);
        gameHandler.execute(message);
    }

    @Test
    public void moveStudentsTest() throws Exception{
        addPlayers();
        playCards();

        List<Student> s = moveStudents("player3");

        assertEquals(Phase.MOVEMNTURN, gameHandler.getPhase());

        List<Student> remainingStudents = gameHandler.getGame().getPlayer("player3").getDashboard().getEntrance().getAllStudents();

        assertFalse(remainingStudents.contains(s.get(0)));
        assertFalse(remainingStudents.contains(s.get(1)));
        assertFalse(remainingStudents.contains(s.get(2)));
        assertFalse(remainingStudents.contains(s.get(3)));

        List<Student> canteenFirst = gameHandler.getGame().getPlayer("player3").getDashboard().getCanteen().getStudents(s.get(0).getColor());
        List<Student> canteenSecond = gameHandler.getGame().getPlayer("player3").getDashboard().getCanteen().getStudents(s.get(1).getColor());

        assertTrue(canteenFirst.contains(s.get(0)));
        assertTrue(canteenSecond.contains(s.get(1)));

        List<Student> islandFirst = gameHandler.getGame().getIsland(8).getAllStudents();
        List<Student> islandSecond = gameHandler.getGame().getIsland(9).getAllStudents();

        assertTrue(islandFirst.contains(s.get(2)));
        assertTrue(islandSecond.contains(s.get(3)));
    }

    private List<Student> moveStudents(String sender) throws Exception{
        Message message;

        List<Student> students = gameHandler.getGame().getPlayer(sender).getDashboard().getEntrance().getAllStudents();

        assertEquals(Phase.MIDDLETURN, gameHandler.getPhase());

        Message wrongMessage = new MoveStudentMessage(sender, students.get(3).getId(), Location.ENTRANCE, -1, Location.ISLAND, 15);
        assertThrows(NoIslandException.class, () -> gameHandler.execute(wrongMessage), "Impossible Island ID");

        Message wrongMessage2 = new MoveStudentMessage(sender, 148, Location.ENTRANCE, -1, Location.CANTEEN, -1);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage2), "Impossible Student ID");

        Message wrongMessage3 = new MoveStudentMessage(sender, students.get(3).getId(), Location.ENTRANCE, -1, Location.CARD_ISLAND, -1);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage3), "Wrong arrival");

        Message wrongMessage4 = new MoveStudentMessage(sender, students.get(3).getId(), Location.CARD_CANTEEN, -1, Location.CANTEEN, -1);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage4), "Wrong departure");

        message = new MoveStudentMessage(sender, students.get(0).getId(), Location.ENTRANCE, -1, Location.CANTEEN, -1);
        gameHandler.execute(message);

        message = new MoveStudentMessage(sender, students.get(1).getId(), Location.ENTRANCE, -1, Location.CANTEEN, -1);
        gameHandler.execute(message);

        message = new MoveStudentMessage(sender, students.get(2).getId(), Location.ENTRANCE, -1, Location.ISLAND, 8);
        gameHandler.execute(message);

        message = new MoveStudentMessage(sender, students.get(3).getId(), Location.ENTRANCE, -1, Location.ISLAND, 9);
        gameHandler.execute(message);

        return students;
    }

    @Test
    public void moveMotherNatureTest() throws Exception{
        addPlayers();
        playCards();
        moveStudents("player3");

        moveMotherNature("player3");

        assertEquals(Phase.ENDTURN, gameHandler.getPhase());
        assertEquals(gameHandler.getGame().getIsland(1), gameHandler.getGame().getMotherNaturePosition());
    }

    private void moveMotherNature(String sender) throws Exception{
        Message wrongMessage = new MoveMotherNatureMessage(sender, 0);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage), "Empty Movement");

        Message wrongMessage2 = new MoveMotherNatureMessage(sender, 5);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage2), "Movement Higher than allowed");

        Message message = new MoveMotherNatureMessage(sender, 1);
        gameHandler.execute(message);
    }

    @Test
    public void selectCloudTest() throws Exception{
        addPlayers();
        playCards();
        moveStudents("player3");
        moveMotherNature("player3");

        List<Student> students = gameHandler.getGame().getAllClouds()[0].getStudents();

        Message wrongMessage = new SelectCloudMessage("player3", -1);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage));

        Message wrongMessage2 = new SelectCloudMessage("player3", 15);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage2));

        selectCloud("player3", 0);

        assertEquals("player1", gameHandler.getPlayers().get(0));
        assertEquals(Phase.MIDDLETURN, gameHandler.getPhase());

        assertFalse(gameHandler.getGame().getAllClouds()[0].isFull());

        List<Student> entrance = gameHandler.getGame().getPlayer("player3").getDashboard().getEntrance().getAllStudents();

        assertTrue(entrance.containsAll(students));
        assertEquals(4, gameHandler.getGame().getRemainingMoves());
    }

    private void selectCloud(String sender, int number) throws Exception{
        Message message = new SelectCloudMessage(sender, number);
        gameHandler.execute(message);
    }

    @Test
    public void testWholeRound() throws Exception{
        addPlayers();
        playCards();
        List<String> players = new ArrayList<>();
        players.add("player3");
        players.add("player1");
        players.add("player2");

        for(int i=0; i<3; i++){
            moveStudents(players.get(i));
            moveMotherNature(players.get(i));

            assertEquals(gameHandler.getGame().getIsland(i+1), gameHandler.getGame().getMotherNaturePosition());

            if(i!=0){
                Message wrongMessage = new SelectCloudMessage(players.get(i), i-1);
                assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage));
            }

            selectCloud(players.get(i), i);
        }

        assertEquals(Phase.PRETURN, gameHandler.getPhase());
        assertEquals("player3", gameHandler.getPlayers().get(0));
    }

    @RepeatedTest(300)
    public void activePowerTest(){
        Message message;
        ArrayList<Student> students;
        init2Players();
        try{
            message = new AddMeMessage("player1", true, 2);
            gameHandler.execute(message);
            message = new AddMeMessage("player2", true, 2);
            gameHandler.execute(message);

            //Add coins to avoid problems
            for(int i = 0; i<19; i++){
                gameHandler.getGame().getPlayer("player1").giveCoin();
            }

            message = new PlayCardMessage("player1", 1);
            gameHandler.execute(message);
            message = new PlayCardMessage("player2", 2);
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
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    private void testCard1(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ExchangeStudentMessage("player1", 1, 2, Location.ENTRANCE, -1, Location.CANTEEN, -1))
        );

        ArrayList<Student> students = c.getStudents();
        Student s = students.get(0);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", s.getId(), Location.ENTRANCE, -1, Location.ISLAND, 1)),
                "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", s.getId(), Location.CARD_ISLAND, -1, Location.ENTRANCE, -1)),
                "Wrong Arrival");

        Message m = new MoveStudentMessage("player1", s.getId(), Location.CARD_ISLAND, -1, Location.ISLAND, 1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getIsland(1).getAllStudents().contains(s));
        assertEquals(4, c.getStudents().size());

        System.out.println("Card 1");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard2(ProfessorCharacter c) throws Exception {
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        System.out.println("Card 2");
        assertEquals(ProfessorRule.class, gameHandler.getGame().getCurrentRule().getClass());

        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard3(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", 1,  Location.ENTRANCE, -1, Location.CANTEEN, -1)),
                "You can't move students with the card that calculate influence of an island without moving Mother Nature");

        Island motherNatureIsland = gameHandler.getGame().getMotherNaturePosition();
        Entrance e = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance();
        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();

        e.addStudent(new Student(131, Color.RED));
        gameHandler.getGame().moveStudent(131, canteen, e);

        gameHandler.getGame().getIsland(1).addStudent(new Student(132, Color.RED));

        Message wrongMessage = new IslandInfluenceMessage("player1", 42);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage));

        Message m = new IslandInfluenceMessage("player1", 1);
        gameHandler.execute(m);

        assertEquals(motherNatureIsland, gameHandler.getGame().getMotherNaturePosition());
        assertEquals(ColorTower.BLACK, gameHandler.getGame().getIsland(1).getOwner());

        System.out.println("Card 3");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard4(MotherNatureCharacter c) throws Exception {
        assertEquals(0, gameHandler.getGame().getMotherNatureExtraMovement());

        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        System.out.println("Card 4");
        assertEquals(2, gameHandler.getGame().getMotherNatureExtraMovement());
        assertEquals(MotherNatureRule.class, gameHandler.getGame().getCurrentRule().getClass());

        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard5(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new IslandInfluenceMessage("player1", 1)),
                "You can't calculate influence of an island without moving Mother Nature with the card that block an island");

        Entrance e = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance();
        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();
        Island i = gameHandler.getGame().getIsland(1);

        e.addStudent(new Student(131, Color.RED));
        gameHandler.getGame().moveStudent(131, canteen, e);

        i.addStudent(new Student(132, Color.RED));

        Message wrongMessage = new BlockIslandMessage("player1", 42);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage));

        Message m = new BlockIslandMessage("player1", 1);
        gameHandler.execute(m);

        assertTrue(i.getProhibition());

        gameHandler.getGame().moveMotherNature(i, true);

        assertEquals(i, gameHandler.getGame().getMotherNaturePosition());
        assertFalse(i.getProhibition());
        assertNull(i.getOwner());

        gameHandler.getGame().moveMotherNature(i, true);
        assertEquals(ColorTower.BLACK, i.getOwner());

        System.out.println("Card 5");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard6(InfluenceCharacter c) throws Exception {
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertEquals(InfluenceRule.class, gameHandler.getGame().getCurrentRule().getClass());
        InfluenceRule rule = (InfluenceRule) gameHandler.getGame().getCurrentRule();

        assertEquals(0, rule.getExtraPoints());
        assertTrue(rule.isDisableTowers());
        assertEquals(ColorTower.BLACK, rule.getPlayerWhoUsed());
        assertNull(rule.getBlockedColor());

        System.out.println("Card 6");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard7(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new BlockColorMessage("player1", Color.RED))
        );

        ArrayList<Student> students1 = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
        Student s1 = students1.get(0);

        ArrayList<Student> students2 = c.getStudents();
        Student s2 = students2.get(0);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.CANTEEN, -1, Location.CARD_EXCHANGE, -1)),
                "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.CANTEEN, -1)),
                "Wrong Arrival");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.ENTRANCE, -1)),
                "Same Arrival and Departure");

        Message m = new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.CARD_EXCHANGE, -1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents().contains(s2));
        assertTrue(c.getStudents().contains(s1));
        assertEquals(6, c.getStudents().size());

        System.out.println("Card 7");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard8(InfluenceCharacter c) throws Exception {
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertEquals(InfluenceRule.class, gameHandler.getGame().getCurrentRule().getClass());
        InfluenceRule rule = (InfluenceRule) gameHandler.getGame().getCurrentRule();

        assertEquals(2, rule.getExtraPoints());
        assertFalse(rule.isDisableTowers());
        assertEquals(ColorTower.BLACK, rule.getPlayerWhoUsed());
        assertNull(rule.getBlockedColor());

        System.out.println("Card 8");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard9(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new BlockIslandMessage("player1", 1)),
                "You can't block an island with the card that block a color");

        Entrance e = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance();
        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();
        Island i = gameHandler.getGame().getIsland(1);

        e.addStudent(new Student(131, Color.RED));
        gameHandler.getGame().moveStudent(131, canteen, e);

        i.addStudent(new Student(132, Color.RED));

        Message m = new BlockColorMessage("player1", Color.RED);
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
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard10(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new PutBackMessage("player1", Color.RED))
        );

        ArrayList<Student> students1 = gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents();
        Student s1 = students1.get(0);

        Canteen canteen = gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen();
        canteen.addStudent(new Student(131, s1.getColor()));
        canteen.canGetCoin(Color.RED, true);

        ArrayList<Student> students2 = canteen.getStudents(s1.getColor());
        Student s2 = students2.get(0);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.CANTEEN, -1, Location.CARD_EXCHANGE, -1)),
                "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.ISLAND, 1)),
                "Wrong Arrival");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.ENTRANCE, -1)),
                "Same Arrival and Departure");

        Message wrongMessage = new ExchangeStudentMessage("player1", 200, -8, Location.ENTRANCE, -1, Location.CANTEEN, -1);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage));

        Message m = new ExchangeStudentMessage("player1", s1.getId(), s2.getId(), Location.ENTRANCE, -1, Location.CANTEEN, -1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getEntrance().getAllStudents().contains(s2));
        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen().getStudents(s1.getColor()).contains(s1));
        assertEquals(0, c.getStudents().size());

        System.out.println("Card 10");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard11(MovementCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new ExchangeStudentMessage("player1", 1, 2, Location.ENTRANCE, -1, Location.CANTEEN, -1))
        );

        ArrayList<Student> students = c.getStudents();
        Student s = students.get(0);

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", s.getId(), Location.ENTRANCE, -1, Location.ISLAND, 1)),
                "Wrong Departure");

        assertThrows(IllegalMoveException.class, () ->
                        gameHandler.execute(new MoveStudentMessage("player1", s.getId(), Location.CARD_CANTEEN, -1, Location.ENTRANCE, -1)),
                "Wrong Arrival");

        Message wrongMessage = new MoveStudentMessage("player1", -8, Location.CARD_CANTEEN, -1, Location.CANTEEN, -1);
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(wrongMessage));

        Message m = new MoveStudentMessage("player1", s.getId(), Location.CARD_CANTEEN, -1, Location.CANTEEN, -1);
        gameHandler.execute(m);

        assertTrue(gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen().getStudents(s.getColor()).contains(s));
        assertEquals(4, c.getStudents().size());

        System.out.println("Card 11");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }

    private void testCard12(ActionCharacter c) throws Exception{
        Message message = new UsePowerMessage("player1", 0);
        gameHandler.execute(message);

        assertThrows(IllegalMoveException.class, () ->
                gameHandler.execute(new BlockColorMessage("player1", Color.RED))
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

        Message m = new PutBackMessage("player1", Color.RED);
        gameHandler.execute(m);

        assertEquals(0, gameHandler.getGame().getPlayer("player1").getDashboard().getCanteen().getNumberStudentColor(Color.RED));
        assertEquals(0, gameHandler.getGame().getPlayer("player2").getDashboard().getCanteen().getNumberStudentColor(Color.RED));

        System.out.println("Card 12");
        assertThrows(IllegalMoveException.class, () -> gameHandler.execute(new UsePowerMessage("player1", 0)));
    }
}
