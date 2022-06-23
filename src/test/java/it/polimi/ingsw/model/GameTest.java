package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.controller.Phase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class Game
 */
class GameTest {
    private Game game;

    /**
     * This method is called before each test, it creates a game with two players
     */
    @BeforeEach
    void init(){
        ArrayList<Student> students1 = new ArrayList<>();
        students1.add(new Student(1, Color.RED));
        students1.add(new Student(2, Color.BLUE));
        students1.add(new Student(3, Color.GREEN));
        students1.add(new Student(4, Color.PINK));
        students1.add(new Student(5, Color.BLUE));
        students1.add(new Student(6, Color.BLUE));
        students1.add(new Student(7, Color.BLUE));

        Player player1 = new Player("player1",2, ColorTower.BLACK, students1);

        ArrayList<Student> students2 = new ArrayList<>();
        students2.add(new Student(8, Color.RED));
        students2.add(new Student(9, Color.BLUE));
        students2.add(new Student(10, Color.RED));
        students2.add(new Student(11, Color.RED));
        students2.add(new Student(12, Color.RED));
        students2.add(new Student(13, Color.RED));
        students2.add(new Student(14, Color.BLUE));

        Player player2 = new Player("player2", 2, ColorTower.WHITE, students2);

        this.game = new Game(false, 2);

        this.game.addPlayer(player1);
        this.game.addPlayer(player2);
    }

    /**
     * This method tests the moveMotherNature method of the class game
     * @throws Exception
     */
    @Test
    void moveMotherNature() throws Exception{
        Entrance e = null;
        Canteen c = null;
        try {
            e = this.game.getPlayer("player1").getDashboard().getEntrance();
            c = this.game.getPlayer("player1").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;
        try {
            i = this.game.getIsland(3);
        }catch(Exception ex){
            fail();
        }

        try {
            this.game.moveStudent(6, c, e);
            this.game.moveStudent(2, i, e);
            this.game.moveStudent(7, i, e);
        }catch (Exception ex){
            fail();
        }

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        } /*catch (EndGameException ex) {
            ex.printStackTrace();
        } */

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        try {
            i = this.game.getIsland(4);
        }catch (Exception ex){
            fail();
        }
        try {
            this.game.moveStudent(5, i, e);
        }catch (Exception ex){
            fail();
        }

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        } /*catch (EndGameException ex) {
            ex.printStackTrace();
        } */

        assertEquals(11, this.game.getAllIslands().size());
        assertEquals("03, 04", this.game.getAllIslands().get(3).getId());

        assertEquals("03, 04", this.game.getIsland(3).getId());
        assertEquals("03, 04", this.game.getIsland(4).getId());
    }

    /**
     * This method test the merging of the island until is caused an endgame because the towers of one player finish
     * @throws Exception
     */
    @Test
    void mergeIslandFirstLastUntilEndGame1() throws Exception{
        Entrance e = null;
        Canteen c = null;
        try {
            e = this.game.getPlayer("player1").getDashboard().getEntrance();
            c = this.game.getPlayer("player1").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;
        try {
            i = this.game.getIsland(0);
        } catch (Exception ex) {
            fail();
        }

        try {
            this.game.moveStudent(6, c, e);
            this.game.moveStudent(2, i, e);
            this.game.moveStudent(7, i, e);
        } catch (Exception ex) {
            fail();
        }

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());
        String id = "00";
        for(int cont = 0; cont < 8; cont++) {
            try {
                i = this.game.getIsland(game.getAllIslands().size()-1);
            } catch (Exception ex) {
                fail();
            }

            e.addStudent(new Student(40+cont, Color.BLUE));

            try {
                this.game.moveStudent(40+cont, i, e);
            } catch (Exception ex) {
                fail();
            }

            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(11-cont, this.game.getAllIslands().size());
            id += String.format(", %02d", 11-cont);
            //System.out.println(this.game.getAllIslands().size());

            String[] arr = id.split(", ");

            String orderedId = Arrays.stream(arr)
                    .map(Integer::parseInt)
                    .sorted()
                    .map(x -> String.format("%02d", x))
                    .reduce((temp1, temp2) -> temp1 + ", " + temp2)
                    .orElse(null);

            assertEquals(orderedId, this.game.getIsland(0).getId());
            assertFalse(this.game.getAllIslands().contains(new Island(11-cont)));
        }



        assertEquals(true, game.getFinishedGame());
        assertEquals("player1", game.getWinner());
        //System.out.println("The winner is"+ game.getWinner());
    }

    //util method that print all the students in every island
    private void printStudentsInIslands() {
        Island island=null;
        ArrayList<Student> students=null;
        for(int i=0; i<12; i++){
            try {
                island = game.getIsland(i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            students=island.getAllStudents();
            for(Student t: students){
                System.out.println(""+t+"");
            }
            System.out.println("fine isola"+i);
        }
    }

    /**
     * This method test the merging of the island until is caused an endgame because there are only 3 islands
     * @throws Exception
     */
    @Test
    void mergeIslandFirstLastUntilEndGame2() throws Exception{
        Entrance e1 = null;
        Canteen c1 = null;
        Entrance e2 = null;
        Canteen c2 = null;
        try {
            e1 = this.game.getPlayer("player1").getDashboard().getEntrance();
            c1 = this.game.getPlayer("player1").getDashboard().getCanteen();
            e2 = this.game.getPlayer("player2").getDashboard().getEntrance();
            c2 = this.game.getPlayer("player2").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;

        //assign the island to let the player2 conquest, the first
        try {
            i = this.game.getIsland(0);
        } catch (Exception ex) {
            fail();
        }

        // the player1 obtain the blue professor
        try {
            this.game.moveStudent(6, c1, e1);
            this.game.moveStudent(2, i, e1);
            this.game.moveStudent(7, i, e1);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.BLACK, i.getReport().getOwner());

        //assign the island to let the player2 conquest, the last
        try {
            i = this.game.getIsland(11);
        } catch (Exception ex) {
            fail();
        }

        // the player2 obtain the red professor
        try {
            this.game.moveStudent(8, c2, e2);
            this.game.moveStudent(10, i, e2);
            this.game.moveStudent(11, i, e2);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.WHITE, i.getReport().getOwner());

        //printStudentsInIslands();

        assertEquals(1, i.getReport().getTowerNumbers());

        for(int cont = 0; cont < 4; cont++) {

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(game.getAllIslands().size() - 2 +cont);
            } catch (Exception ex) {
                fail();
            }
            e2.addStudent(new Student(40 + cont, Color.RED));
            e2.addStudent(new Student(80 + cont, Color.RED));

            try {
                this.game.moveStudent(40 + cont, i, e2);
                this.game.moveStudent(80 + cont, i, e2);
            } catch (Exception ex) {
                fail();
            }

            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.WHITE, i.getReport().getOwner());

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(1 + cont);
            } catch (Exception ex) {
                fail();
            }
            e1.addStudent(new Student(60 + cont, Color.BLUE));
            e1.addStudent(new Student(90 + cont, Color.BLUE));

            try {
                this.game.moveStudent(60 + cont, i, e1);
                this.game.moveStudent(90 + cont, i, e1);
            } catch (Exception ex) {
                fail();
            }


            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.BLACK, i.getReport().getOwner());

            assertEquals(11 - (2 * cont) - 1, this.game.getAllIslands().size());


        }

        try {
            i = this.game.getIsland(6);
        } catch (Exception ex) {
            fail();
        }

        e2.addStudent(new Student(50, Color.RED));
        e2.addStudent(new Student(51, Color.RED));

        try {
            this.game.moveStudent(50, i, e2);
            this.game.moveStudent(51, i, e2);
        } catch (Exception ex) {
            fail();
        }

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(true, game.getFinishedGame());
        //System.out.println("The winner is"+ game.getWinner());
        assertEquals(3, this.game.getAllIslands().size());
    }

    private void create4PlayerGame(){
        ArrayList<Student> students1 = new ArrayList<>();
        students1.add(new Student(1, Color.RED));
        students1.add(new Student(2, Color.BLUE));
        students1.add(new Student(3, Color.GREEN));
        students1.add(new Student(4, Color.PINK));
        students1.add(new Student(5, Color.BLUE));
        students1.add(new Student(6, Color.BLUE));
        students1.add(new Student(7, Color.BLUE));

        Player player1 = new Player("player1",4, ColorTower.BLACK, students1);

        ArrayList<Student> students2 = new ArrayList<>();
        students2.add(new Student(8, Color.RED));
        students2.add(new Student(9, Color.BLUE));
        students2.add(new Student(10, Color.RED));
        students2.add(new Student(11, Color.RED));
        students2.add(new Student(12, Color.RED));
        students2.add(new Student(13, Color.RED));
        students2.add(new Student(14, Color.BLUE));

        Player player2 = new Player("player2", 4, ColorTower.WHITE, students2);

        ArrayList<Student> students3 = new ArrayList<>();
        students3.add(new Student(8, Color.RED));
        students3.add(new Student(9, Color.BLUE));
        students3.add(new Student(10, Color.RED));
        students3.add(new Student(11, Color.RED));
        students3.add(new Student(12, Color.RED));
        students3.add(new Student(13, Color.RED));
        students3.add(new Student(14, Color.BLUE));

        Player player3 = new Player("player3", 4, ColorTower.BLACK, students3);

        ArrayList<Student> students4 = new ArrayList<>();
        students4.add(new Student(8, Color.RED));
        students4.add(new Student(9, Color.BLUE));
        students4.add(new Student(10, Color.RED));
        students4.add(new Student(11, Color.RED));
        students4.add(new Student(12, Color.RED));
        students4.add(new Student(13, Color.RED));
        students4.add(new Student(14, Color.BLUE));

        Player player4 = new Player("player4", 4, ColorTower.WHITE, students4);

        this.game = new Game(false, 4);

        this.game.addPlayer(player1);
        this.game.addPlayer(player2);
        this.game.addPlayer(player3);
        this.game.addPlayer(player4);
    }

    /**
     * This method test the merging of the island until is caused an endgame because there are only 3 islands in the 4
     * players game
     */
    @Test
    void mergeIslandUntilEndGame4Player() throws Exception{
        create4PlayerGame();
        Entrance e1 = null;
        Canteen c1 = null;
        Entrance e2 = null;
        Canteen c2 = null;
        try {
            e1 = this.game.getPlayer("player1").getDashboard().getEntrance();
            c1 = this.game.getPlayer("player1").getDashboard().getCanteen();
            e2 = this.game.getPlayer("player2").getDashboard().getEntrance();
            c2 = this.game.getPlayer("player2").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;

        //assign the island to let the player2 conquest, the first
        try {
            i = this.game.getIsland(0);
        } catch (Exception ex) {
            fail();
        }

        // the player1 obtain the blue professor
        try {
            this.game.moveStudent(6, c1, e1);
            this.game.moveStudent(2, i, e1);
            this.game.moveStudent(7, i, e1);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.BLACK, i.getReport().getOwner());

        //assign the island to let the player2 conquest, the last
        try {
            i = this.game.getIsland(11);
        } catch (Exception ex) {
            fail();
        }

        // the player2 obtain the red professor
        try {
            this.game.moveStudent(8, c2, e2);
            this.game.moveStudent(10, i, e2);
            this.game.moveStudent(11, i, e2);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.WHITE, i.getReport().getOwner());

        //printStudentsInIslands();

        assertEquals(1, i.getReport().getTowerNumbers());

        for(int cont = 0; cont < 4; cont++) {

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(game.getAllIslands().size() - 2 +cont);
            } catch (Exception ex) {
                fail();
            }
            e2.addStudent(new Student(40 + cont, Color.RED));
            e2.addStudent(new Student(80 + cont, Color.RED));

            try {
                this.game.moveStudent(40 + cont, i, e2);
                this.game.moveStudent(80 + cont, i, e2);
            } catch (Exception ex) {
                fail();
            }

            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.WHITE, i.getReport().getOwner());

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(1 + cont);
            } catch (Exception ex) {
                fail();
            }
            e1.addStudent(new Student(60 + cont, Color.BLUE));
            e1.addStudent(new Student(90 + cont, Color.BLUE));

            try {
                this.game.moveStudent(60 + cont, i, e1);
                this.game.moveStudent(90 + cont, i, e1);
            } catch (Exception ex) {
                fail();
            }


            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.BLACK, i.getReport().getOwner());

            assertEquals(11 - (2 * cont) - 1, this.game.getAllIslands().size());


        }

        try {
            i = this.game.getIsland(6);
        } catch (Exception ex) {
            fail();
        }

        e2.addStudent(new Student(50, Color.RED));
        e2.addStudent(new Student(51, Color.RED));

        try {
            this.game.moveStudent(50, i, e2);
            this.game.moveStudent(51, i, e2);
        } catch (Exception ex) {
            fail();
        }

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(true, game.getFinishedGame());
        //System.out.println("The winner is"+ game.getWinner());
        assertEquals(3, this.game.getAllIslands().size());

    }

    /**
     * This method tests the update of the professors owning, when is moved a student to the canteen
     */
    @Test
    void updateProfessorsMoveToCanteen(){
        Entrance e = null;
        Canteen c = null;
        try {
            e = this.game.getPlayer("player1").getDashboard().getEntrance();
            c = this.game.getPlayer("player1").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        try {
            this.game.moveStudent(2, c, e);
        }catch (Exception ex){
            fail();
        }
        Map<Color, Player> professors = this.game.getProfessors();

        try {
            assertEquals(this.game.getPlayer("player1"), professors.get(Color.BLUE));
        }catch (Exception ex){
            fail();
        }
    }

    /**
     * This method tests the update professors when a student is not move to the canteen
     */
    @Test
    void updateProfessorsNotMoveToCanteen(){
        Entrance e = null;
        try{
            e = this.game.getPlayer("player1").getDashboard().getEntrance();
        }catch (Exception ex){
            fail();
        }
        Island i = null;
        try{
            i = this.game.getIsland(0);
        }catch(Exception ex){
            fail();
        }
        try {
            this.game.moveStudent(2, i, e);
        }catch (Exception ex){
            fail();
        }
        Map<Color, Player> professors = this.game.getProfessors();

        assertNull(professors.get(Color.BLUE));
    }

    /**
     * This method tests the change of ownership of a professor
     */
    @Test
    void updateProfessorsChangeOwnership(){
        Entrance e = null;
        Canteen c = null;
        try{
            e = this.game.getPlayer("player1").getDashboard().getEntrance();
            c = this.game.getPlayer("player1").getDashboard().getCanteen();
        }catch (Exception ex){
            fail();
        }

        try {
            this.game.moveStudent(2, c, e);
        }catch (Exception ex){
            fail();
        }
        Map<Color, Player> professors = this.game.getProfessors();

        try {
            assertEquals(this.game.getPlayer("player1"), professors.get(Color.BLUE));
        }catch (Exception ex){
            fail();
        }

        try {
            e = this.game.getPlayer("player2").getDashboard().getEntrance();
            c = this.game.getPlayer("player2").getDashboard().getCanteen();
        }catch (Exception ex){
            fail();
        }

        try {
            this.game.moveStudent(9, c, e);
            this.game.moveStudent(14, c, e);
        }catch (Exception ex){
            fail();
        }
        professors = this.game.getProfessors();

        try {
            assertEquals(this.game.getPlayer("player2"), professors.get(Color.BLUE));
        }catch (Exception ex){
            fail();
        }
    }

    /**
     * This method tests all the getters of the class game
     */
    @Test
    void getterTest(){
        assertEquals(2, game.getNumPlayers());
        assertEquals(2, game.getRegisteredNumPlayers());
        assertEquals("00", game.getMotherNaturePosition().getId());
        assertEquals(2, game.getAllPlayers().size());
        assertEquals(2, game.getAllClouds().length);
        //Try to get islands or players that don't exist:
        assertThrows(NoIslandException.class, () -> game.getIsland(13));

        assertThrows(NoPlayerException.class, () -> game.getPlayer("ThisPlayerDoesn'tExist"));
        game.resetPlayedCards();
    }

    /**
     * This method tests some possible cases in the first part of the game
     */
    @Test
    public void otherInitTest(){
        try {
            game.selectCloud("player1", game.getAllClouds()[0]);
            game.selectCloud("player2", game.getAllClouds()[1]);
            game.refillClouds();
        }catch (Exception e){
            fail();
        }

        game = new Game(false, 5);

        game = new Game(false, 2);
        for(int i=0; i<17; i++) game.addPlayer("bro1", ColorTower.WHITE);

        assertThrows(IllegalMoveException.class, () -> game.moveStudent(3045, game.getIsland(0), game.getIsland(1)));
    }

    /**
     * This method tests other cases of moveMotherNature method
     */
    @RepeatedTest(100)
    public void moveMotherNatureTest() {
        try {
            for(int i=1; i<8; i++)
                game.moveStudent(i, game.getPlayer("player1").getDashboard().getCanteen(), game.getPlayer("player1").getDashboard().getEntrance());
            game.moveMotherNature(game.getIsland(1), true);
            game.moveMotherNature(game.getIsland(2), true);
            game.moveMotherNature(game.getIsland(5), true);
            game.moveMotherNature(game.getIsland(4), true);
            game.moveMotherNature(game.getIsland(7), true);
            game.moveMotherNature(game.getIsland(9), true);
            game.moveMotherNature(game.getIsland(11), true);
            for(int i=8; i<13; i++)
                game.moveStudent(i, game.getPlayer("player2").getDashboard().getCanteen(), game.getPlayer("player2").getDashboard().getEntrance());
            game.moveStudent(13, game.getIsland(7), game.getPlayer("player2").getDashboard().getEntrance());
            game.moveStudent(14, game.getPlayer("player2").getDashboard().getCanteen(), game.getPlayer("player2").getDashboard().getEntrance());
            game.moveMotherNature(game.getIsland(1), true);
            game.moveMotherNature(game.getIsland(7), true);
            game.moveMotherNature(game.getIsland(9), true);
            game.moveMotherNature(game.getIsland(11), true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method tests the setter and getter of the phase
     */
    @Test
    public void setGetPlayerPhase(){
        game.setCurrentPlayer("player2", false);
        assertEquals(game.getCurrentPlayer(), "player2");
        for(Phase p : Phase.values()){
            game.setCurrentPhase(p);
            assertEquals(game.getCurrentPhase(), p);
        }
    }

    /**
     * This method tests the exception AlreadyPlayedCardException is thrown when the player plays a card already
     * played in that turn
     * @throws Exception
     */
    @Test
    public void playCardAlreadyPlayed() throws Exception{
        Player p1 = game.getPlayer("player1");
        Player p2 = game.getPlayer("player2");

        this.game.playCard(p2, 1);
        assertEquals(1, game.getMaximumMNMovement(p2));

        assertThrows(IllegalMoveException.class, ()->this.game.playCard(p1, 1));
    }

    /**
     * This method tests the rules regarding the use of the assistant cards
     * @throws Exception
     */
    @Test
    public void playCardAlreadyPlayedButOnlyCardPlayable() throws Exception{
        Player p1 = game.getPlayer("player1");
        Player p2 = game.getPlayer("player2");

        this.game.playCard(p2, 1);
        assertEquals(1, game.getMaximumMNMovement(p2));

        //Play cards from 2 to 10
        for(int i=1; i<10; i++)
            p1.getHand().playCard(i+1);

        assertDoesNotThrow(()->this.game.playCard(p1, 1));
        assertEquals(1, game.getMaximumMNMovement(p1));
    }

    /**
     * This method test no active card exception when an action can be executed only with a card active
     * @throws Exception
     */
    @Test
    void testNoActiveCard() throws Exception{
        Player p1 = game.getPlayer("player1");

        assertThrows(IllegalMoveException.class, () -> game.usePower(p1, 0));
        assertThrows(NoActiveCardException.class, () -> game.getRequestedAction());
        assertThrows(NoActiveCardException.class, () -> game.getAllowedDepartures());
        assertThrows(NoActiveCardException.class, () -> game.getAllowedDepartures());

        assertThrows(NoActiveCardException.class, () -> game.disableIsland(null));
        assertThrows(NoActiveCardException.class, () -> game.disableColor(p1, Color.RED));
        assertThrows(NoActiveCardException.class, () -> game.moveMotherNature(null, false));
        assertThrows(NoActiveCardException.class, () -> game.putBackInBag(Color.RED));
        assertFalse(game.needsRefill());
        assertThrows(NoActiveCardException.class, () -> game.refillActiveCard());
    }

    /**
     * This method tests that when the assistant card are finished the flag isLastTurn is true
     * @throws Exception
     */
    @Test
    void EndGameByFinishedAssistentCard() throws Exception {
        create4PlayerGame();
        Player p1 = game.getPlayer("player1");
        Player p2 = game.getPlayer("player2");
        Player p3 = game.getPlayer("player3");
        Player p4 = game.getPlayer("player4");

        //Play cards from 1 to 10
        for(int i=0; i<10; i++) {
            this.game.playCard(p1, i+1);
        }

        assertEquals(true, game.getIsLastTurn());

        //Play cards from 1 to 10
        for(int i=0; i<10; i++){
            this.game.playCard(p2, i+1);
        }


        assertEquals(true, game.getIsLastTurn());

        //Play cards from 1 to 10
        for(int i=0; i<10; i++) {
            this.game.playCard(p3, i + 1);
        }

        assertEquals(true, game.getIsLastTurn());

        //Play cards from 1 to 10
        for(int i=0; i<10; i++) {
            this.game.playCard(p4, i + 1);
        }

        assertEquals(true, game.getIsLastTurn());
    }

    /**
     * This method tests the end of the game in 4 player game, when the two team have the same amount of islands conquered
     * but the Black team has more professor
     * @throws Exception
     */
    @Test
    void EndGame4PlayerTieMoreProfessorBlack() throws Exception{
        create4PlayerGame();
        Entrance e1 = null;
        Canteen c1 = null;
        Entrance e2 = null;
        Canteen c2 = null;
        try {
            e1 = this.game.getPlayer("player1").getDashboard().getEntrance();
            c1 = this.game.getPlayer("player1").getDashboard().getCanteen();
            e2 = this.game.getPlayer("player2").getDashboard().getEntrance();
            c2 = this.game.getPlayer("player2").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;

        //assign the island to let the player2 conquest, the first
        try {
            i = this.game.getIsland(0);
        } catch (Exception ex) {
            fail();
        }

        // the player1 obtain the blue professor and the
        try {
            this.game.moveStudent(3, c1, e1);
            this.game.moveStudent(6, c1, e1);
            this.game.moveStudent(2, i, e1);
            this.game.moveStudent(7, i, e1);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.BLACK, i.getReport().getOwner());

        //assign the island to let the player2 conquest, the last
        try {
            i = this.game.getIsland(11);
        } catch (Exception ex) {
            fail();
        }

        // the player2 obtain the red professor
        try {
            this.game.moveStudent(8, c2, e2);
            this.game.moveStudent(10, i, e2);
            this.game.moveStudent(11, i, e2);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.WHITE, i.getReport().getOwner());

        //printStudentsInIslands();

        assertEquals(1, i.getReport().getTowerNumbers());

        for(int cont = 0; cont < 4; cont++) {

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(game.getAllIslands().size() - 2 +cont);
            } catch (Exception ex) {
                fail();
            }
            e2.addStudent(new Student(40 + cont, Color.RED));
            e2.addStudent(new Student(80 + cont, Color.RED));

            try {
                this.game.moveStudent(40 + cont, i, e2);
                this.game.moveStudent(80 + cont, i, e2);
            } catch (Exception ex) {
                fail();
            }

            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.WHITE, i.getReport().getOwner());

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(1 + cont);
            } catch (Exception ex) {
                fail();
            }
            e1.addStudent(new Student(60 + cont, Color.BLUE));
            e1.addStudent(new Student(90 + cont, Color.BLUE));

            try {
                this.game.moveStudent(60 + cont, i, e1);
                this.game.moveStudent(90 + cont, i, e1);
            } catch (Exception ex) {
                fail();
            }


            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.BLACK, i.getReport().getOwner());

            assertEquals(11 - (2 * cont) - 1, this.game.getAllIslands().size());


        }


        game.endGame();

        assertEquals(true, game.getFinishedGame());
        //System.out.println("The winner is"+ game.getWinner());
        assertEquals(4, this.game.getAllIslands().size());

    }

    /**
     * This method tests the end of the game in 4 players game, when the two team have the same amount of islands conquered
     * and the same amount of professors
     * @throws Exception
     */
    @Test
    void EndGame4PlayerCompletelyTie() throws Exception{
        create4PlayerGame();
        Entrance e1 = null;
        Canteen c1 = null;
        Entrance e2 = null;
        Canteen c2 = null;
        try {
            e1 = this.game.getPlayer("player1").getDashboard().getEntrance();
            c1 = this.game.getPlayer("player1").getDashboard().getCanteen();
            e2 = this.game.getPlayer("player2").getDashboard().getEntrance();
            c2 = this.game.getPlayer("player2").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;

        //assign the island to let the player2 conquest, the first
        try {
            i = this.game.getIsland(0);
        } catch (Exception ex) {
            fail();
        }

        // the player1 obtain the blue professor
        try {
            this.game.moveStudent(6, c1, e1);
            this.game.moveStudent(2, i, e1);
            this.game.moveStudent(7, i, e1);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.BLACK, i.getReport().getOwner());

        //assign the island to let the player2 conquest, the last
        try {
            i = this.game.getIsland(11);
        } catch (Exception ex) {
            fail();
        }

        // the player2 obtain the red professor
        try {
            this.game.moveStudent(8, c2, e2);
            this.game.moveStudent(10, i, e2);
            this.game.moveStudent(11, i, e2);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.WHITE, i.getReport().getOwner());

        //printStudentsInIslands();

        assertEquals(1, i.getReport().getTowerNumbers());

        for(int cont = 0; cont < 4; cont++) {

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(game.getAllIslands().size() - 2 +cont);
            } catch (Exception ex) {
                fail();
            }
            e2.addStudent(new Student(40 + cont, Color.RED));
            e2.addStudent(new Student(80 + cont, Color.RED));

            try {
                this.game.moveStudent(40 + cont, i, e2);
                this.game.moveStudent(80 + cont, i, e2);
            } catch (Exception ex) {
                fail();
            }

            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.WHITE, i.getReport().getOwner());

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(1 + cont);
            } catch (Exception ex) {
                fail();
            }
            e1.addStudent(new Student(60 + cont, Color.BLUE));
            e1.addStudent(new Student(90 + cont, Color.BLUE));

            try {
                this.game.moveStudent(60 + cont, i, e1);
                this.game.moveStudent(90 + cont, i, e1);
            } catch (Exception ex) {
                fail();
            }


            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.BLACK, i.getReport().getOwner());

            assertEquals(11 - (2 * cont) - 1, this.game.getAllIslands().size());


        }
        //printStudentsInIslands();

        game.endGame();

        assertEquals(true, game.getFinishedGame());
        //System.out.println("The winner is"+ game.getWinner());
        assertEquals(4, this.game.getAllIslands().size());

    }

    /**
     * This method tests the end of the game in 2 players game, when the players have the same amount of island conquered and
     * the same amount of professors
     * @throws Exception
     */
    @Test
    void EndGame2playerCompletelyTie() throws Exception{
        Entrance e1 = null;
        Canteen c1 = null;
        Entrance e2 = null;
        Canteen c2 = null;
        try {
            e1 = this.game.getPlayer("player1").getDashboard().getEntrance();
            c1 = this.game.getPlayer("player1").getDashboard().getCanteen();
            e2 = this.game.getPlayer("player2").getDashboard().getEntrance();
            c2 = this.game.getPlayer("player2").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;

        //assign the island to let the player2 conquest, the first
        try {
            i = this.game.getIsland(0);
        } catch (Exception ex) {
            fail();
        }

        // the player1 obtain the blue professor
        try {
            this.game.moveStudent(6, c1, e1);
            this.game.moveStudent(2, i, e1);
            this.game.moveStudent(7, i, e1);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.BLACK, i.getReport().getOwner());

        //assign the island to let the player2 conquest, the last
        try {
            i = this.game.getIsland(11);
        } catch (Exception ex) {
            fail();
        }

        // the player2 obtain the red professor
        try {
            this.game.moveStudent(8, c2, e2);
            this.game.moveStudent(10, i, e2);
            this.game.moveStudent(11, i, e2);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.WHITE, i.getReport().getOwner());

        //printStudentsInIslands();

        assertEquals(1, i.getReport().getTowerNumbers());

        for(int cont = 0; cont < 4; cont++) {

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(game.getAllIslands().size() - 2 +cont);
            } catch (Exception ex) {
                fail();
            }
            e2.addStudent(new Student(40 + cont, Color.RED));
            e2.addStudent(new Student(80 + cont, Color.RED));

            try {
                this.game.moveStudent(40 + cont, i, e2);
                this.game.moveStudent(80 + cont, i, e2);
            } catch (Exception ex) {
                fail();
            }

            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.WHITE, i.getReport().getOwner());

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(1 + cont);
            } catch (Exception ex) {
                fail();
            }
            e1.addStudent(new Student(60 + cont, Color.BLUE));
            e1.addStudent(new Student(90 + cont, Color.BLUE));

            try {
                this.game.moveStudent(60 + cont, i, e1);
                this.game.moveStudent(90 + cont, i, e1);
            } catch (Exception ex) {
                fail();
            }


            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.BLACK, i.getReport().getOwner());

            assertEquals(11 - (2 * cont) - 1, this.game.getAllIslands().size());


        }

        game.endGame();

        assertEquals(true, game.getFinishedGame());
        //System.out.println("The winner is"+ game.getWinner());
        assertEquals(4, this.game.getAllIslands().size());

    }

    /**
     * This method tests the end of the game in 2 players game, when the players have the same amount of island conquered and
     * the same amount of professors
     * @throws Exception
     */
    @Test
    void EndGame2playerTieMoreProfessorBlack() throws Exception{
        Entrance e1 = null;
        Canteen c1 = null;
        Entrance e2 = null;
        Canteen c2 = null;
        try {
            e1 = this.game.getPlayer("player1").getDashboard().getEntrance();
            c1 = this.game.getPlayer("player1").getDashboard().getCanteen();
            e2 = this.game.getPlayer("player2").getDashboard().getEntrance();
            c2 = this.game.getPlayer("player2").getDashboard().getCanteen();
        }catch(Exception ex){
            fail();
        }
        Island i = null;

        //assign the island to let the player2 conquest, the first
        try {
            i = this.game.getIsland(0);
        } catch (Exception ex) {
            fail();
        }

        // the player1 obtain the blue professor
        try {
            this.game.moveStudent(3, c1, e1);
            this.game.moveStudent(6, c1, e1);
            this.game.moveStudent(2, i, e1);
            this.game.moveStudent(7, i, e1);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.BLACK, i.getReport().getOwner());

        //assign the island to let the player2 conquest, the last
        try {
            i = this.game.getIsland(11);
        } catch (Exception ex) {
            fail();
        }

        // the player2 obtain the red professor
        try {
            this.game.moveStudent(8, c2, e2);
            this.game.moveStudent(10, i, e2);
            this.game.moveStudent(11, i, e2);
        } catch (Exception ex) {
            fail();
        }

        //conquest the island by moving mother nature
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }
        //Check if the island were conquered
        assertEquals(ColorTower.WHITE, i.getReport().getOwner());

        //printStudentsInIslands();

        assertEquals(1, i.getReport().getTowerNumbers());

        for(int cont = 0; cont < 4; cont++) {

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(game.getAllIslands().size() - 2 +cont);
            } catch (Exception ex) {
                fail();
            }
            e2.addStudent(new Student(40 + cont, Color.RED));
            e2.addStudent(new Student(80 + cont, Color.RED));

            try {
                this.game.moveStudent(40 + cont, i, e2);
                this.game.moveStudent(80 + cont, i, e2);
            } catch (Exception ex) {
                fail();
            }

            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.WHITE, i.getReport().getOwner());

            //assign the island to let the player2 conquest
            try {
                i = this.game.getIsland(1 + cont);
            } catch (Exception ex) {
                fail();
            }
            e1.addStudent(new Student(60 + cont, Color.BLUE));
            e1.addStudent(new Student(90 + cont, Color.BLUE));

            try {
                this.game.moveStudent(60 + cont, i, e1);
                this.game.moveStudent(90 + cont, i, e1);
            } catch (Exception ex) {
                fail();
            }


            try {
                this.game.moveMotherNature(i, true);
            } catch (NoActiveCardException ex) {
                fail();
            }

            assertEquals(ColorTower.BLACK, i.getReport().getOwner());

            assertEquals(11 - (2 * cont) - 1, this.game.getAllIslands().size());


        }

        game.endGame();

        assertEquals(true, game.getFinishedGame());
        System.out.println("The winner is"+ game.getWinner());
        assertEquals(4, this.game.getAllIslands().size());

    }

}