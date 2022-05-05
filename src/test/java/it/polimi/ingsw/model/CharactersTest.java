package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.characters.*;

import it.polimi.ingsw.model.exceptions.CannotAddStudentException;
import it.polimi.ingsw.model.exceptions.NoMoreTokensException;
import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import static org.fusesource.jansi.Ansi.*;

class CharactersTest {

    @Test
    void actionCharacterWithTokensTest(){
        JSONCharacter jc = new JSONCharacter(5, CharacterType.ACTION, "Block island", 2, Action.BLOCK_ISLAND, 4, null, false, null, null, false, 0, 0);
        ActionCharacter character = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());

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

        assertThrows(NoMoreTokensException.class, character::addToken, "It should return an exception because you can't add more tokens than the maximum on it");
        for(int i = 0; i<4; i++){
            assertEquals(4-i, character.getNumTokens());
            assertDoesNotThrow(character::removeToken, "You can remove up to "+ character.getNumTokens()+" tokens from the card");
        }
        assertThrows(NoMoreTokensException.class, character::removeToken, "It should return an exception because you can't remove tokens when the card is empty");
    }

    @Test
    void actionCharacterWithoutTokensTest(){
        JSONCharacter jc = new JSONCharacter(3, CharacterType.ACTION, "Calculate influence without moving Mother Nature", 3, Action.ISLAND_INFLUENCE, 0, null, false, null, null, false, 0, 0);
        ActionCharacter character = new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());

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

        assertThrows(NoMoreTokensException.class, character::addToken, "It should return an exception because you can't add more tokens than the maximum on it");
        assertThrows(NoMoreTokensException.class, character::removeToken, "It should return an exception because you can't remove tokens when the card is empty");
    }

    @Test
    void influenceCharacterDisableTowersTest(){
        JSONCharacter jc = new JSONCharacter(6, CharacterType.INFLUENCE, "Disable towers Influence", 2, null, 0, null, false, null, null, true, 0, 0);
        InfluenceCharacter character = new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 6"), "The ID is 6");
        assertEquals(6, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.INFLUENCE), "It's an Influence Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 2"), "It's costs 2 Coins");

        //Influence Parameters
        assertTrue(character.toString().contains("\u001B[1mTowerDisabled:\u001B[0m true"), "It disables towers during influence calculation");
        assertTrue(character.toString().contains("\u001B[1mExtraPoints:\u001B[0m 0"), "It doesn't give extra influence points");
    }

    @Test
    void influenceCharacterExtraPointsTest(){
        JSONCharacter jc = new JSONCharacter(8, CharacterType.INFLUENCE, "Influence + 2", 2, null, 0, null, false, null, null, false, 2, 0);
        InfluenceCharacter character = new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 8"), "The ID is 8");
        assertEquals(8, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.INFLUENCE), "It's an Influence Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 2"), "It's costs 2 Coins");

        //Influence Parameters
        assertTrue(character.toString().contains("\u001B[1mTowerDisabled:\u001B[0m false"), "It doesn't disable towers during influence calculation");
        assertTrue(character.toString().contains("\u001B[1mExtraPoints:\u001B[0m 2"), "It gives 2 extra influence points");
    }

    @Test
    void motherNatureCharacterTest(){
        JSONCharacter jc = new JSONCharacter(4, CharacterType.MOTHERNATURE, "Maximum movement + 2", 1, null, 0, null, false, null, null, false, 0, 2);
        MotherNatureCharacter character = new MotherNatureCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 4"), "The ID is 4");
        assertEquals(4, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.MOTHERNATURE), "It's an Mother Nature Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 1"), "It's costs 1 Coins");

        //MotherNature Parameters
        assertTrue(character.toString().contains("\u001B[1mExtraMovement:\u001B[0m 2"), "The Extra Movement is 2");
    }

    @Test
    void professorCharacterTest(){
        JSONCharacter jc = new JSONCharacter(2, CharacterType.PROFESSOR, "Professor change with tie", 2, null, 0, null, false, null, null, false, 0, 0);
        ProfessorCharacter character = new ProfessorCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost());

        //Check if the parameters of the character are correct
        assertTrue(character.toString().contains("\u001B[1mID:\u001B[0m 2"), "The ID is 2");
        assertEquals(2, character.getId());
        assertTrue(character.toString().contains("\u001B[1mType:\u001B[0m " + CharacterType.PROFESSOR), "It's an Professor Character");
        assertTrue(character.toString().contains("\u001B[1mCost:\u001B[0m 2"), "It's costs 2 Coins");
    }

    @Test
    void movementCharacterTest() throws NoSuchStudentException, CannotAddStudentException {
        Set<Location> allowedDepartures = Set.of(Location.CARD_ISLAND);
        Set<Location> allowedArrivals = Set.of(Location.ISLAND);

        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(1, Color.RED));
        students.add(new Student(2, Color.BLUE));
        students.add(new Student(3, Color.GREEN));
        students.add(new Student(4, Color.PINK));
        JSONCharacter jc = new JSONCharacter(1, CharacterType.MOVEMENT, "Move students from card to island", 1, Action.MOVESTUDENT, 4, Location.CARD_ISLAND, true, allowedDepartures, allowedArrivals, false, 0, 0);
        MovementCharacter character = new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), students, jc.getParams());

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
        assertThrows(NoMoreTokensException.class, character::addToken, "The only way to add a token on the card is through the students' movement");
        assertThrows(NoMoreTokensException.class, character::removeToken, "The only way to remove a token on the card is through the students' movement");

        assertThrows(CannotAddStudentException.class, ()->character.addStudent(new Student(5, Color.RED)));

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

        assertThrows(NoSuchStudentException.class, () -> character.removeStudent(15));
    }
}
