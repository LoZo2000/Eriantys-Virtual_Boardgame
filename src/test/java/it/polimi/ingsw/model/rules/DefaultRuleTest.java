package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;
import it.polimi.ingsw.model.rules.DefaultRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class DefaultRule
 */
class DefaultRuleTest {

    private DefaultRule rule;
    private Report rep;
    private Map<Color, ColorTower> professors;

    /**
     * This method is called before each test, it creates a DefaultRule and a Hashmap
     */
    @BeforeEach
    void init() {
        this.rule = new DefaultRule();
        this.professors = new HashMap<>();
    }

    /**
     * This method tests that the player who has the ColorTower black obtain the influence according to the rules
     */
    @Test
    void calculateInfluenceBlackWinner() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 1);
        students.put(Color.YELLOW, 0);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 3);

        this.rep = new Report(ColorTower.BLACK, 3, students, null, 0);

        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, null);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.BLACK, winner);
    }

    /**
     * This method tests that the player who has the ColorTower white obtain the influence according to the rules
     */
    @Test
    void calculateInfluenceWhiteWinner() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 4);
        students.put(Color.YELLOW, 0);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 3);

        this.rep = new Report(ColorTower.BLACK, 3, students, null, 0);

        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, null);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.WHITE, winner);
    }

    /**
     * This method tests that the player who has the ColorTower black obtain the influence according to the rules, because
     * both players have the same amount of influence but the island was of the black ColorTower
     */
    @Test
    void calculateInfluenceTieWinner() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 3);
        students.put(Color.YELLOW, 0);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 3);

        this.rep = new Report(ColorTower.BLACK, 3, students, null, 0);

        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, null);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.BLACK, winner);
    }

    /**
     * This method tests that the player who has the ColorTower grey obtain the influence according to the rules
     */
    @Test
    void calculateInfluenceNoTowers() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 1);
        students.put(Color.YELLOW, 6);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 3);

        this.rep = new Report(null, 0, students, null ,0);

        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, ColorTower.GREY);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.GREY, winner);
    }

    /**
     * This method tests that no one obtain the influence of the island according to the rules, because in the computation
     * of the influence every player has the same amount and no one previously owned the influence of the island
     */
    @Test
    void calculateInfluenceTieNoInfluence() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 1);
        students.put(Color.YELLOW, 2);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 4);

        this.rep = new Report(null, 0, students, null ,0);

        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, ColorTower.GREY);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertNull(winner);
    }

    /**
     * This method tests that the player who has the ColorTower grey obtain the influence according to the rules, because
     * there is a tie in the computation of the island influence, but the influence of the island was previously owned by
     * the gray ColorTower
     */
    @Test
    void calculateInfluenceTieNoChange() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 1);
        students.put(Color.YELLOW, 2);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 4);

        this.rep = new Report(ColorTower.GREY, 1, students, null, 0);

        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, ColorTower.GREY);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.GREY, winner);
    }

    /**
     * This method tests that the player who has the ColorTower black obtain the influence according to the rules, the black
     * ColorTower obtain the influence of the island due to the extra points given
     */
    @Test
    void calculateInfluenceWithExtraPoints() {
        HashMap<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 4);
        students.put(Color.RED, 1);
        students.put(Color.YELLOW, 2);
        students.put(Color.PINK, 1);
        students.put(Color.GREEN, 4);

        this.rep = new Report(ColorTower.GREY, 1, students, ColorTower.BLACK, 2);

        this.professors.put(Color.BLUE, ColorTower.WHITE);
        this.professors.put(Color.PINK, ColorTower.BLACK);
        this.professors.put(Color.RED, ColorTower.WHITE);
        this.professors.put(Color.YELLOW, ColorTower.GREY);
        this.professors.put(Color.GREEN, ColorTower.BLACK);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.BLACK, winner);
    }

    /**
     * This method tests the update of the professor in a two player game
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
     * This method tests the update of the professor in a three player game with no previously owner of the professor
     */
    @Test
    void updateProfessor3PlayersNoOwner(){
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 5);
        counterPerColor.put("player2", 2);
        counterPerColor.put("player3", 3);

        String winner = this.rule.updateProfessor(null, counterPerColor);

        assertEquals("player1", winner);
    }

    /**
     * This method tests the update of the professor in a three player game with a tie and no previously owner of the professor
     */
    @Test
    void updateProfessor3PlayersNoOwnerTie(){
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 0);
        counterPerColor.put("player2", 0);
        counterPerColor.put("player3", 0);

        String winner = this.rule.updateProfessor(null, counterPerColor);

        assertNull(winner);
    }

    /**
     * This method tests the value of the variable isActionNeeded in the DefaultRule
     */
    @Test
    void isActionNeededDefault(){
        assertFalse(this.rule.isActionNeeded());
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
        assertEquals(0, this.rule.getMaximumExchangeMoves());
    }
}