package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class ExchangeRule
 */
class ExchangeRuleTest {

    private ExchangeRule rule;
    private Report rep;
    private Map<Color, ColorTower> professors;


    /**
     * This method is called before each test, it creates a hashmap of students, a hashmap of professor and ColorTower,
     * a Report and an ExchangeRule
     */
    @BeforeEach
    void init() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 1);
        students.put(Color.YELLOW, 4);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 3);

        this.professors = new HashMap<>();
        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, null);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        this.rep = new Report(ColorTower.BLACK, 2, students, null, 0);
        //BLACK: 4 + 2 (Towers)
        //WHITE: 5

        this.rule = new ExchangeRule(3);
    }

    /**
     * This method tests the calculateInfluence method with the ExchangeRule, the winner is ColorTower black like it should be
     */
    @Test
    void calculateInfluence() {
        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);
        assertEquals(ColorTower.BLACK, winner);
    }

    /**
     * This method tests the update professor in a two players game
     */
    @Test
    void updateProfessor2Players(){
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 3);
        counterPerColor.put("player2", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player2", winner);
    }

    /**
     * This method tests the update of the professor in a two player game with a tie
     */
    @Test
    void updateProfessor2PlayersTie(){
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 6);
        counterPerColor.put("player2", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player1", winner);
    }

    /**
     * This method tests the update of the professor in a three player game with a tie
     */
    @Test
    void updateProfessor3PlayersTie(){
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 6);
        counterPerColor.put("player2", 6);
        counterPerColor.put("player3", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player1", winner);
    }

    /**
     * This method tests the value of the variable isActionNeeded in the ExchangeRule
     */
    @Test
    void isActionNeededInfluence(){
        assertTrue(this.rule.isActionNeeded());
    }

    /**
     * This method tests the MotherNatureExtraMovement getter
     */
    @Test
    void getMotherNatureExtraMovement(){
        assertEquals(0, this.rule.getMotherNatureExtraMovement());
    }

    /**
     * This method tests the MaximumExchangeMoves getter
     */
    @Test
    void getMaximumExchangeMoves(){
        assertEquals(3, this.rule.getMaximumExchangeMoves());
    }
}