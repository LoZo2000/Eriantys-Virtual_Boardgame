package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import static org.fusesource.jansi.Ansi.*;

/**
 * This class contains the test for the Characters cards
 */
class CharactersTest {

    /**
     * This method tests the action characters with tokens
     */
    @Test
    void actionCharacterWithTokensTest(){
        JSONCharacter jc = new JSONCharacter(5, CharacterType.ACTION, "Block island","Block island", 2, Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0, 0);
        ActionCharacter character = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 5"), "The ID is 5");
        assertEquals(5, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.ACTION), "It's an Action Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 2"), "It costs 2 Coins");

        assertTrue(character.shortString().contains(ansi().bold().a("Cost: ").reset().a(2).toString()), "It costs 2 Coins");

        //Action Parameters
        assertTrue(character.toString().contains("\u001B[1mActionType:\u001B[0m " + Action.BLOCK_ISLAND), "It blocks an island when used");
        assertTrue(character.toString().contains("\u001B[1mMaxNumTokens:\u001B[0m 4"), "It has 4 tokens on it");

        assertTrue(character.shortString().contains(ansi().bold().a("Tokens: ").reset().a(4).toString()), "It has 4 tokens on it");

        //Test Methods
        assertEquals(Action.BLOCK_ISLAND, character.getType());

        assertThrows(IllegalMoveException.class, character::addToken, "It should return an exception because you can't add more tokens than the maximum on it");
        for(int i = 0; i<4; i++){
            assertEquals(4-i, character.getNumTokens());
            assertDoesNotThrow(character::removeToken, "You can remove up to "+ character.getNumTokens()+" tokens from the card");
        }
        assertThrows(IllegalMoveException.class, character::removeToken, "It should return an exception because you can't remove tokens when the card is empty");
    }

    /**
     * This method tests the action characters without tokens
     */
    @Test
    void actionCharacterWithoutTokensTest(){
        JSONCharacter jc = new JSONCharacter(3, CharacterType.ACTION, "Calculate influence without moving Mother Nature", "Calculate influence without moving Mother Nature",3, Action.ISLAND_INFLUENCE, 0, null, false, null, null, false, 0, 0, 0);
        ActionCharacter character = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 3"), "The ID is 3");
        assertEquals(3, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.ACTION), "It's an Action Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 3"), "It's costs 3 Coins");

        //Action Parameters
        assertTrue(character.toString().contains("\u001B[1mActionType:\u001B[0m " + Action.ISLAND_INFLUENCE), "When used it calculates the influence on an island");
        assertTrue(character.toString().contains("\u001B[1mMaxNumTokens:\u001B[0m 0"), "It has 0 tokens on it");

        //Test Methods
        assertEquals(Action.ISLAND_INFLUENCE, character.getType());

        assertThrows(IllegalMoveException.class, character::addToken, "It should return an exception because you can't add more tokens than the maximum on it");
        assertThrows(IllegalMoveException.class, character::removeToken, "It should return an exception because you can't remove tokens when the card is empty");
    }

    /**
     * This method tests the ability to disable tower of the influence character
     */
    @Test
    void influenceCharacterDisableTowersTest(){
        JSONCharacter jc = new JSONCharacter(6, CharacterType.INFLUENCE, "Disable towers Influence","Disable towers Influence",2, null, 0, null, false, null, null, true, 0, 0, 0);
        InfluenceCharacter character = new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 6"), "The ID is 6");
        assertEquals(6, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.INFLUENCE), "It's an Influence Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 2"), "It's costs 2 Coins");

        //Influence Parameters
        assertTrue(character.toString().contains("\u001B[1mTowerDisabled:\u001B[0m true"), "It disables towers during influence calculation");
        assertTrue(character.toString().contains("\u001B[1mExtraPoints:\u001B[0m 0"), "It doesn't give extra influence points");
    }

    /**
     * This method tests the ability to give extra points of the influence character
     */
    @Test
    void influenceCharacterExtraPointsTest(){
        JSONCharacter jc = new JSONCharacter(8, CharacterType.INFLUENCE, "Influence + 2","Influence + 2",2, null, 0, null, false, null, null, false, 2, 0, 0);
        InfluenceCharacter character = new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 8"), "The ID is 8");
        assertEquals(8, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.INFLUENCE), "It's an Influence Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 2"), "It's costs 2 Coins");

        //Influence Parameters
        assertTrue(character.toString().contains("\u001B[1mTowerDisabled:\u001B[0m false"), "It doesn't disable towers during influence calculation");
        assertTrue(character.toString().contains("\u001B[1mExtraPoints:\u001B[0m 2"), "It gives 2 extra influence points");
    }

    /**
     * This method tests the motherNature character
     */
    @Test
    void motherNatureCharacterTest(){
        JSONCharacter jc = new JSONCharacter(4, CharacterType.MOTHERNATURE, "Maximum movement + 2","Maximum movement + 2",1, null, 0, null, false, null, null, false, 0, 2, 0);
        MotherNatureCharacter character = new MotherNatureCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 4"), "The ID is 4");
        assertEquals(4, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.MOTHERNATURE), "It's an Mother Nature Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 1"), "It's costs 1 Coins");

        //MotherNature Parameters
        assertTrue(character.toString().contains("\u001B[1mExtraMovement:\u001B[0m 2"), "The Extra Movement is 2");
    }

    /**
     * This method tests the professor character
     */
    @Test
    void professorCharacterTest(){
        JSONCharacter jc = new JSONCharacter(2, CharacterType.PROFESSOR, "Professor change with tie","Professor change with tie",2, null, 0, null, false, null, null, false, 0, 0, 0);
        ProfessorCharacter character = new ProfessorCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 2"), "The ID is 2");
        assertEquals(2, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.PROFESSOR), "It's an Professor Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 2"), "It's costs 2 Coins");
    }

    /**
     * This method tests the movement Character
     * @throws IllegalMoveException when there is not the student with the id passed in the parameter in the card
     */
    @Test
    void movementCharacterTest() throws IllegalMoveException {
        Set<Location> allowedDepartures = Set.of(Location.CARD_ISLAND);
        Set<Location> allowedArrivals = Set.of(Location.ISLAND);

        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(1, Color.RED));
        students.add(new Student(2, Color.BLUE));
        students.add(new Student(3, Color.GREEN));
        students.add(new Student(4, Color.PINK));
        JSONCharacter jc = new JSONCharacter(1, CharacterType.MOVEMENT, "Move students from card to island","Move students from card to island",1, Action.MOVESTUDENT, 4, Location.CARD_ISLAND, true, allowedDepartures, allowedArrivals, false, 0, 0, 0);
        MovementCharacter character = new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), students, jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 1"), "The ID is 1");
        assertEquals(1, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.MOVEMENT), "It's a Movement Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 1"), "It's costs 1 Coins");

        //Check Movement Params
        assertTrue(character.toString().contains("\u001B[1mActionType:\u001B[0m " + Action.MOVESTUDENT), "It allows the movement of a student");
        assertTrue(character.toString().contains("\u001B[1mMaxNumTokens:\u001B[0m 4"), "It has 4 tokens on it");
        assertTrue(character.toString().contains("\u001B[1mLocation Type:\u001B[0m " + Location.CARD_ISLAND), "Its the Card that moves students to island");
        assertTrue(character.toString().contains("\u001B[1mNeeds Refill?\u001B[0m true"), "The card needs students refill");
        assertTrue(character.toString().contains("\u001B[1mAllowed Departures:\u001B[0m " + allowedDepartures), "The card permits movement only from CARD_ISLAND");
        assertTrue(character.toString().contains("\u001B[1mAllowed Arrivals:\u001B[0m " + allowedArrivals), "The card permits movement only to ISLAND");

        assertTrue(character.shortString().contains(ansi().bold().a("Students: ").reset().a(students).toString()));

        //Check Values
        assertEquals(Action.MOVESTUDENT, character.getType(), "Allows only movement of students");
        assertEquals(4, character.getNumTokens(), "There are 4 students on card");
        assertEquals(Location.CARD_ISLAND, character.getLocationType());
        assertTrue(character.isRefill());
        assertEquals(allowedDepartures, character.getAllowedDepartures());
        assertEquals(allowedArrivals, character.getAllowedArrivals());
        assertEquals(students, character.getStudents());

        //Check Methods
        assertThrows(IllegalMoveException.class, character::addToken, "The only way to add a token on the card is through the students' movement");
        assertThrows(IllegalMoveException.class, character::removeToken, "The only way to remove a token on the card is through the students' movement");

        assertThrows(IllegalMoveException.class, ()->character.addStudent(new Student(5, Color.RED)));

        Student s = character.removeStudent(1);
        assertEquals(new Student(1, Color.RED), s);

        ArrayList<Student> stud = new ArrayList<>();
        stud.add(new Student(2, Color.BLUE));
        stud.add(new Student(3, Color.GREEN));
        stud.add(new Student(4, Color.PINK));

        assertEquals(stud, character.getStudents());

        character.addStudent(new Student(5, Color.RED));

        stud.add(new Student(5, Color.RED));

        assertEquals(stud, character.getStudents());

        assertThrows(IllegalMoveException.class, () -> character.removeStudent(15));
    }

    /**
     * This method tests the exchangeCharacter
     * @throws IllegalMoveException when there is not the student with the id passed in the parameter in the card
     */
    @Test
    void exchangeCharacterTest() throws IllegalMoveException {
        Set<Location> allowedDepartures = Set.of(Location.CARD_EXCHANGE, Location.ENTRANCE);
        Set<Location> allowedArrivals = Set.of(Location.CARD_EXCHANGE, Location.ENTRANCE);

        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(1, Color.RED));
        students.add(new Student(2, Color.BLUE));
        students.add(new Student(3, Color.GREEN));
        students.add(new Student(4, Color.PINK));
        students.add(new Student(5, Color.YELLOW));
        students.add(new Student(6, Color.RED));
        JSONCharacter jc = new JSONCharacter(7, CharacterType.EXCHANGE, "Exchange students between card and entrance","Exchange students between card and entrance",1, Action.EXCHANGESTUDENT, 6, Location.CARD_EXCHANGE, false, allowedDepartures, allowedArrivals, false, 0, 0, 3);
        ExchangeCharacter character = new ExchangeCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), students, jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 7"), "The ID is 7");
        assertEquals(7, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.EXCHANGE), "It's a Exchange Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 1"), "It's costs 1 Coins");

        //Check Movement Params
        assertTrue(character.toString().contains("\u001B[1mActionType:\u001B[0m " + Action.EXCHANGESTUDENT), "It allows the exchange of students");
        assertTrue(character.toString().contains("\u001B[1mMaxNumTokens:\u001B[0m 6"), "It has 6 tokens on it");
        assertTrue(character.toString().contains("\u001B[1mLocation Type:\u001B[0m " + Location.CARD_EXCHANGE), "Its the Card that exchanges students with entrance");
        assertTrue(character.toString().contains("\u001B[1mNeeds Refill?\u001B[0m false"), "The card doesn't students refill");
        assertTrue(character.toString().contains("\u001B[1mAllowed Departures:\u001B[0m " + allowedDepartures), "The card permits movement only between CARD_EXCHANGE and ENTRANCE");
        assertTrue(character.toString().contains("\u001B[1mAllowed Arrivals:\u001B[0m " + allowedArrivals), "The card permits movement only to CARD_EXCHANGE and ENTRANCE");
        assertTrue(character.toString().contains("\u001B[1mMaximum Exchanges:\u001B[0m 3"), "The card permits movement only 3 exchanges");

        assertTrue(character.shortString().contains(ansi().bold().a("Students: ").reset().a(students).toString()));

        //Check Values
        assertEquals(Action.EXCHANGESTUDENT, character.getType(), "Allows only exchange of students");
        assertEquals(6, character.getNumTokens(), "There are 6 students on card");
        assertEquals(Location.CARD_EXCHANGE, character.getLocationType());
        assertFalse(character.isRefill());
        assertEquals(allowedDepartures, character.getAllowedDepartures());
        assertEquals(allowedArrivals, character.getAllowedArrivals());
        assertEquals(students, character.getStudents());
        assertEquals(3, character.getMaxMoves());

        //Check Methods
        assertThrows(IllegalMoveException.class, character::addToken, "The only way to add a token on the card is through the students' movement");
        assertThrows(IllegalMoveException.class, character::removeToken, "The only way to remove a token on the card is through the students' movement");

        assertThrows(IllegalMoveException.class, ()->character.addStudent(new Student(7, Color.RED)));

        Student s = character.removeStudent(1);
        assertEquals(new Student(1, Color.RED), s);

        ArrayList<Student> stud = new ArrayList<>();
        stud.add(new Student(2, Color.BLUE));
        stud.add(new Student(3, Color.GREEN));
        stud.add(new Student(4, Color.PINK));
        stud.add(new Student(5, Color.YELLOW));
        stud.add(new Student(6, Color.RED));

        assertEquals(stud, character.getStudents());

        character.addStudent(new Student(7, Color.RED));

        stud.add(new Student(7, Color.RED));

        assertEquals(stud, character.getStudents());

        assertThrows(IllegalMoveException.class, () -> character.removeStudent(15));
    }

    /**
     * This method tests the getter of the character classes
     */
    @Test
    public void getterTest(){
        JSONCharacter jc = new JSONCharacter(5, CharacterType.ACTION, "Block island","Block island",2, Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0, 0);
        ActionCharacter character = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams());

        assertEquals(2, character.getOriginalCost());
        assertEquals("/Char_5.jpg", character.getSprite());
        assertEquals("Block island", character.getDesc_short());
    }
}