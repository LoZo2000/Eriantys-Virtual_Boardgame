package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRuleTest {

    private ExchangeRule rule;
    private Report rep;
    private Map<Color, ColorTower> professors;

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

    @Test
    void calculateInfluence() {
        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);
        assertEquals(ColorTower.BLACK, winner);
    }

    @Test
    void updateProfessor2Players(){
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 3);
        counterPerColor.put("player2", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player2", winner);
    }

    @Test
    void updateProfessor2PlayersTie(){
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 6);
        counterPerColor.put("player2", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player1", winner);
    }

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

    @Test
    void isActionNeededInfluence(){
        assertTrue(this.rule.isActionNeeded());
    }

    @Test
    void getMotherNatureExtraMovement(){
        assertEquals(0, this.rule.getMotherNatureExtraMovement());
    }

    @Test
    void getMaximumExchangeMoves(){
        assertEquals(3, this.rule.getMaximumExchangeMoves());
    }
}