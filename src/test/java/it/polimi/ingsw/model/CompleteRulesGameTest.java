package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.exceptions.IllegalMoveException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;
import it.polimi.ingsw.model.exceptions.NoCharacterSelectedException;
import it.polimi.ingsw.model.exceptions.NoMoreTokensException;
import it.polimi.ingsw.model.rules.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CompleteRulesGameTest {
    private Game game;

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
        students1.add(new Student(17, Color.BLUE));
        students1.add(new Student(18, Color.BLUE));
        students1.add(new Student(19, Color.BLUE));
        students1.add(new Student(20, Color.BLUE));
        students1.add(new Student(21, Color.BLUE));
        students1.add(new Student(22, Color.BLUE));

        Player player1 = new Player("player1",2, ColorTower.BLACK, students1);

        ArrayList<Student> students2 = new ArrayList<>();
        students2.add(new Student(8, Color.RED));
        students2.add(new Student(9, Color.GREEN));
        students2.add(new Student(10, Color.GREEN));
        students2.add(new Student(11, Color.GREEN));
        students2.add(new Student(12, Color.YELLOW));
        students2.add(new Student(13, Color.GREEN));
        students2.add(new Student(14, Color.BLUE));
        students2.add(new Student(15, Color.GREEN));
        students2.add(new Student(16, Color.GREEN));
        students2.add(new Student(23, Color.BLUE));
        students2.add(new Student(24, Color.BLUE));

        Player player2 = new Player("player2", 2, ColorTower.WHITE, students2);

        try {
            this.game = new Game(true, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.game.addPlayer(player1);
        this.game.addPlayer(player2);
    }

    @RepeatedTest(100)
    void useRulesExtraPoints() {
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(8, CharacterType.INFLUENCE, "Influence + 2", 2, null, 0, null, false, null, null, false, 2, 0);
        characters[0] = new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        Entrance e = p1.getDashboard().getEntrance();
        Canteen c = p1.getDashboard().getCanteen();
        Island i = this.game.getIsland(3);

        this.game.moveStudent(6, c, e);
        this.game.moveStudent(2, i, e);
        this.game.moveStudent(7, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        Player p2 = this.game.getPlayer("player2");
        e = p2.getDashboard().getEntrance();
        c = p2.getDashboard().getCanteen();

        this.game.moveStudent(10, c, e);
        this.game.moveStudent(11, i, e);
        this.game.moveStudent(13, i, e);
        this.game.moveStudent(16, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(1, i.getReport().getTowerNumbers());

        try {
            //Card 1: 2 Extra Influence Points
            this.game.usePower(p2, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        assertEquals(InfluenceRule.class, game.getCurrentRule());

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.WHITE, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());
    }

    @RepeatedTest(100)
    void useRulesDisableTower() {
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(6, CharacterType.INFLUENCE, "Influence No Towers", 3, null, 0, null, false, null, null, true, 0, 0);
        //JSONParams params = new JSONParams(null, 0, null, false, null, null, true, 0, 0);
        characters[0] = new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        //Objective: Merge 2 island and then the other player disables the towers to change the influence

        Player p1 = this.game.getPlayer("player1");
        Entrance e = p1.getDashboard().getEntrance();
        Canteen c = p1.getDashboard().getCanteen();
        Island i = this.game.getIsland(3);

        this.game.moveStudent(6, c, e);
        this.game.moveStudent(2, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        i = this.game.getIsland(4);
        this.game.moveStudent(7, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        //THE MERGED ISLAND HAS AN INFLUENCE OF 3 + 2 (Towers) + 2 Random Students

        i = this.game.getIsland(3);
        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(2, i.getReport().getTowerNumbers());

        Player p2 = this.game.getPlayer("player2");
        e = p2.getDashboard().getEntrance();
        c = p2.getDashboard().getCanteen();

        this.game.moveStudent(10, c, e);
        this.game.moveStudent(11, i, e);
        this.game.moveStudent(13, i, e);
        this.game.moveStudent(9, i, e);
        this.game.moveStudent(16, i, e);
        this.game.moveStudent(15, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        try {
            //Card 0 Disable Towers
            this.game.usePower(p2, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        assertEquals(InfluenceRule.class, game.getCurrentRule());

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.WHITE, i.getReport().getOwner());
        assertEquals(2, i.getReport().getTowerNumbers());
    }

    @RepeatedTest(100)
    void updateProfessorsMoveToCanteen(){
        Entrance e = this.game.getPlayer("player1").getDashboard().getEntrance();
        Canteen c = this.game.getPlayer("player1").getDashboard().getCanteen();
        this.game.moveStudent(2, c, e);
        Map<Color, Player> professors = this.game.getProfessors();

        assertEquals(this.game.getPlayer("player1"), professors.get(Color.BLUE));
    }

    @RepeatedTest(100)
    void updateProfessorsNotMoveToCanteen(){
        Entrance e = this.game.getPlayer("player1").getDashboard().getEntrance();
        Island i = this.game.getIsland(0);
        this.game.moveStudent(2, i, e);
        Map<Color, Player> professors = this.game.getProfessors();

        assertNull(professors.get(Color.BLUE));
    }

    @RepeatedTest(100)
    void updateProfessorsChangeOwnershipTieRule(){
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(2, CharacterType.PROFESSOR, "Get the professor also if you have the same number of the others", 2, null, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ProfessorCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost());
        this.game.setCharactersCards(characters);

        Entrance e = this.game.getPlayer("player1").getDashboard().getEntrance();
        Canteen c = this.game.getPlayer("player1").getDashboard().getCanteen();

        this.game.moveStudent(2, c, e);
        Map<Color, Player> professors = this.game.getProfessors();

        assertEquals(this.game.getPlayer("player1"), professors.get(Color.BLUE));

        try {
            //Card 0 Tie Professor
            this.game.usePower(this.game.getPlayer("player2"), 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        e = this.game.getPlayer("player2").getDashboard().getEntrance();
        c = this.game.getPlayer("player2").getDashboard().getCanteen();

        this.game.moveStudent(14, c, e);
        professors = this.game.getProfessors();

        assertEquals(this.game.getPlayer("player2"), professors.get(Color.BLUE));
    }

    @RepeatedTest(100)
    void useRulesDisableIsland() {
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(5, CharacterType.ACTION, "Block island", 2, Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0);
        //JSONParams params = new JSONParams(Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        Entrance e = p1.getDashboard().getEntrance();
        Canteen c = p1.getDashboard().getCanteen();
        Island i = this.game.getIsland(3);

        this.game.moveStudent(6, c, e);
        this.game.moveStudent(2, i, e);
        this.game.moveStudent(7, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        Player p2 = this.game.getPlayer("player2");
        e = p2.getDashboard().getEntrance();
        c = p2.getDashboard().getCanteen();

        this.game.moveStudent(10, c, e);
        this.game.moveStudent(11, i, e);
        this.game.moveStudent(13, i, e);
        this.game.moveStudent(9, i, e);
        this.game.moveStudent(15, i, e);
        this.game.moveStudent(16, i, e);

        assertThrows(NoActiveCardException.class, () -> this.game.disableIsland(i));

        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        try {
            this.game.disableIsland(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ActionCharacter ac = (ActionCharacter) this.game.getCharactersCards()[0];
        assertEquals(3, ac.getNumTokens());

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        ac = (ActionCharacter) this.game.getCharactersCards()[0];
        assertEquals(4, ac.getNumTokens());

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        assertEquals(DefaultRule.class, game.getCurrentRule());

        //Now the Token isn't anymore on the island

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.WHITE, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());
    }

    @RepeatedTest(100)
    void useRulesDisableIslandMerge2Tokens() {
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(5, CharacterType.ACTION, "Block island", 2, Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0);
        //JSONParams params = new JSONParams(Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        Entrance e = p1.getDashboard().getEntrance();
        Canteen c = p1.getDashboard().getCanteen();
        Island i = this.game.getIsland(3);

        this.game.moveStudent(6, c, e);
        this.game.moveStudent(2, i, e);
        this.game.moveStudent(7, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertThrows(NoActiveCardException.class, () -> this.game.disableIsland(null));

        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        assertThrows(NoActiveCardException.class, () -> this.game.disableColor(null, Color.PINK));

        try {
            this.game.disableIsland(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ActionCharacter ac = (ActionCharacter) this.game.getCharactersCards()[0];
        assertEquals(3, ac.getNumTokens());


        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        Player p2 = this.game.getPlayer("player2");
        e = p2.getDashboard().getEntrance();
        c = p2.getDashboard().getCanteen();
        i = this.game.getIsland(4);

        this.game.moveStudent(10, c, e);
        this.game.moveStudent(11, i, e);
        this.game.moveStudent(13, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.WHITE, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        Island finalI = i;
        assertThrows(NoActiveCardException.class, () -> this.game.disableIsland(finalI));

        p1 = this.game.getPlayer("player1");
        e = p1.getDashboard().getEntrance();
        i = this.game.getIsland(5);

        this.game.moveStudent(17, i, e);
        this.game.moveStudent(18, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        try {
            this.game.disableIsland(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        assertEquals(2, ac.getNumTokens());

        i = this.game.getIsland(4);

        this.game.moveStudent(19, i, e);
        this.game.moveStudent(20, i, e);
        this.game.moveStudent(21, i, e);
        this.game.moveStudent(22, i, e);
        this.game.moveStudent(5, i, e);

        //Merge of island 3, 4, 5 (3 and 5 are blocked)
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        //One token when the island is merged is put back on the card
        assertEquals(3, ac.getNumTokens());

        assertEquals(10, this.game.getAllIslands().size());

        assertEquals(DefaultRule.class, game.getCurrentRule());

        //game.getAllIslands().stream().forEach(System.out::println);

        //Now the Token isn't anymore on the island
        i = this.game.getIsland(3);
        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(4, ac.getNumTokens());
    }

    @RepeatedTest(100)
    void usePowerBlockColor(){
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(9, CharacterType.ACTION, "Block color", 3, Action.BLOCK_COLOR, 0, null, false, null, null, false, 0, 0);
        //JSONParams params = new JSONParams(Action.BLOCK_COLOR, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        Entrance e = p1.getDashboard().getEntrance();
        Canteen c = p1.getDashboard().getCanteen();
        Island i = this.game.getIsland(3);

        this.game.moveStudent(6, c, e);
        this.game.moveStudent(2, i, e);
        this.game.moveStudent(7, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        Player p2 = this.game.getPlayer("player2");
        e = p2.getDashboard().getEntrance();
        c = p2.getDashboard().getCanteen();

        this.game.moveStudent(10, c, e);
        this.game.moveStudent(11, i, e);
        this.game.moveStudent(13, i, e);

        assertThrows(NoActiveCardException.class, () -> this.game.disableColor(null, Color.PINK));

        try {
            this.game.usePower(p2, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        assertThrows(NoActiveCardException.class, () -> this.game.disableIsland(null));
        assertThrows(NoActiveCardException.class, () -> this.game.moveMotherNature(null, false));

        try {
            this.game.disableColor(p2, Color.BLUE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.WHITE, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());
    }

    @RepeatedTest(100)
    void usePowerInfluenceIsland(){
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(3, CharacterType.ACTION, "Calculate influence w/o moving MN", 3, Action.ISLAND_INFLUENCE, 0, null, false, null, null, false, 0, 0);
        //JSONParams params = new JSONParams(Action.ISLAND_INFLUENCE, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        Entrance e = p1.getDashboard().getEntrance();
        Canteen c = p1.getDashboard().getCanteen();
        Island i = this.game.getIsland(3);

        this.game.moveStudent(6, c, e);
        this.game.moveStudent(2, i, e);
        this.game.moveStudent(7, i, e);

        try {
            this.game.moveMotherNature(i, true);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        Island i2 = this.game.getIsland(5);

        this.game.moveStudent(17, i2, e);
        this.game.moveStudent(18, i2, e);

        assertThrows(NoActiveCardException.class, () -> this.game.moveMotherNature(i2, false));

        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        assertThrows(NoActiveCardException.class, () -> this.game.disableIsland(null));

        try {
            this.game.moveMotherNature(i2, false);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(ColorTower.BLACK, i2.getReport().getOwner());
        assertEquals(1, i2.getReport().getTowerNumbers());
        assertEquals(i, game.getMotherNaturePosition());
    }

    @RepeatedTest(100)
    void usePowerPutBack(){
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(12, CharacterType.ACTION, "Put Back students", 3, Action.PUT_BACK, 0, null, false, null, null, false, 0, 0);
        //JSONParams params = new JSONParams(Action.PUT_BACK, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        Entrance e1 = p1.getDashboard().getEntrance();
        Canteen c1 = p1.getDashboard().getCanteen();
        Island i = this.game.getIsland(3);

        this.game.moveStudent(6, c1, e1);
        this.game.moveStudent(2, c1, e1);
        this.game.moveStudent(7, c1, e1);
        this.game.moveStudent(17, c1, e1);

        assertEquals(4, c1.getNumberStudentColor(Color.BLUE));

        Player p2 = this.game.getPlayer("player2");
        Entrance e2 = p2.getDashboard().getEntrance();
        Canteen c2 = p2.getDashboard().getCanteen();

        this.game.moveStudent(23, c2, e2);
        this.game.moveStudent(24, c2, e2);

        assertEquals(2, c2.getNumberStudentColor(Color.BLUE));

        assertThrows(NoActiveCardException.class, () -> this.game.moveMotherNature(null, false));

        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        assertThrows(NoActiveCardException.class, () -> this.game.disableIsland(null));

        try {
            this.game.putBackInBag(Color.BLUE);
        } catch (NoActiveCardException ex) {
            fail();
        }

        assertEquals(1, c1.getNumberStudentColor(Color.BLUE));
        assertEquals(0, c2.getNumberStudentColor(Color.BLUE));
    }

    @Test
    void motherNatureExtraMovement(){
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(4, CharacterType.MOTHERNATURE, "Extra movement motherNature", 1, null, 0, null, false, null, null, false, 0, 2);
        //JSONParams params = new JSONParams(Action.PUT_BACK, 0, null, false, null, null, false, 0, 0);
        characters[0] = new MotherNatureCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        assertEquals(2, this.game.getMotherNatureExtraMovement());
    }

    @Test
    void exchangeStudent(){
        Character[] characters = new Character[1];
        Set<Location> allowedDepartures = Set.of(Location.ENTRANCE, Location.CARD_EXCHANGE);
        Set<Location> allowedArrivals = Set.of(Location.ENTRANCE, Location.CARD_EXCHANGE);

        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(31, Color.RED));
        students.add(new Student(32, Color.BLUE));
        students.add(new Student(33, Color.GREEN));
        students.add(new Student(34, Color.PINK));
        students.add(new Student(35, Color.BLUE));
        students.add(new Student(36, Color.BLUE));

        JSONCharacter jc = new JSONCharacter(7, CharacterType.MOVEMENT, "Exchange student between entrance and card", 1, Action.EXCHANGESTUDENT, 6, Location.CARD_EXCHANGE, false, allowedDepartures, allowedArrivals, false, 0, 0);
        characters[0] = new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), students, jc.getParams());
        this.game.setCharactersCards(characters);

        Player p1 = this.game.getPlayer("player1");
        Entrance e1 = p1.getDashboard().getEntrance();
        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        Action requestedActiveAction = null;
        Set<Location> departures = null;
        Set<Location> arrivals = null;
        try {
            requestedActiveAction = this.game.getRequestedAction();
            departures = this.game.getAllowedDepartures();
            arrivals = this.game.getAllowedArrivals();
        } catch (NoActiveCardException e) {
            fail();
        }

        if(requestedActiveAction != Action.EXCHANGESTUDENT)
            fail();

        if(!departures.contains(Location.ENTRANCE)){
            fail();
        }

        if(!arrivals.contains(Location.CARD_EXCHANGE)){
            fail();
        }

        MovementCharacter mc = (MovementCharacter) this.game.getCharactersCards()[this.game.getActiveCard()];

        this.game.exchangeStudent(17, 32, mc, e1);

        ArrayList<Student> studentCard = mc.getStudents();
        ArrayList<Student> studentEntrance = e1.getAllStudents();

        assertTrue(studentCard.contains(new Student(17, Color.BLUE)));
        assertTrue(studentEntrance.contains(new Student(32, Color.BLUE)));
    }

    @Test
    void checkRefillMovementCard(){
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
        this.game.setCharactersCards(characters);

        assertThrows(NoActiveCardException.class, () -> this.game.needsRefill());

        Player p1 = this.game.getPlayer("player1");
        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        try {
            assertTrue(this.game.needsRefill());
        } catch (NoActiveCardException e) {
            fail();
        }
    }

    @Test
    void checkRefillNoMovementCard(){
        Character[] characters = new Character[1];
        JSONCharacter jc = new JSONCharacter(12, CharacterType.ACTION, "Put Back students", 3, Action.PUT_BACK, 0, null, false, null, null, false, 0, 0);
        //JSONParams params = new JSONParams(Action.PUT_BACK, 0, null, false, null, null, false, 0, 0);
        characters[0] = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());
        this.game.setCharactersCards(characters);

        assertThrows(NoActiveCardException.class, () -> this.game.needsRefill());

        Player p1 = this.game.getPlayer("player1");
        try {
            this.game.usePower(p1, 0);
        } catch (NoCharacterSelectedException ex) {
            fail();
        }

        try {
            assertFalse(this.game.needsRefill());
        } catch (NoActiveCardException e) {
            fail();
        }
    }

}