package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InfluenceRuleTest {

    private InfluenceRule rule;
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
    }

    @Test
    void calculateInfluenceTowersDisabled() {
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 0, true);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.WHITE, winner);
    }

    @Test
    void calculateInfluenceExtraPoints() {
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 2, false);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.WHITE, winner);
    }

    @Test
    void calculateInfluenceBlockedColor() {
        this.rule = new InfluenceRule(ColorTower.WHITE, Color.RED, 2, false);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);
        assertEquals(ColorTower.BLACK, winner);

        this.rule = new InfluenceRule(ColorTower.WHITE, Color.GREEN, 2, false);

        winner = this.rule.calculateInfluence(this.rep, this.professors);
        assertEquals(ColorTower.WHITE, winner);
    }

    @Test
    void updateProfessor2Players(){
        this.rule = new InfluenceRule(ColorTower.WHITE,null,2, false);
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 3);
        counterPerColor.put("player2", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player2", winner);
    }

    @Test
    void updateProfessor2PlayersTie(){
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 2, false);
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 6);
        counterPerColor.put("player2", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player1", winner);
    }

    @Test
    void updateProfessor3PlayersTie(){
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 2, false);
        String owner = "player1";
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 6);
        counterPerColor.put("player2", 6);
        counterPerColor.put("player3", 6);

        String winner = this.rule.updateProfessor(owner, counterPerColor);

        assertEquals("player1", winner);
    }

    @Test
    void updateProfessor3PlayersNoOwner(){
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 2, false);
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 5);
        counterPerColor.put("player2", 2);
        counterPerColor.put("player3", 3);

        String winner = this.rule.updateProfessor(null, counterPerColor);

        assertEquals("player1", winner);
    }

    @Test
    void updateProfessor3PlayersNoOwnerTie(){
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 2, false);
        HashMap<String, Integer> counterPerColor = new HashMap<>();
        counterPerColor.put("player1", 0);
        counterPerColor.put("player2", 0);
        counterPerColor.put("player3", 0);

        String winner = this.rule.updateProfessor(null, counterPerColor);

        assertNull(winner);
    }

    @Test
    void isActionNeededInfluence(){
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 2, false);
        assertFalse(this.rule.isActionNeeded());
    }

    @Test
    void getMotherNatureExtraMovement(){
        this.rule = new InfluenceRule(ColorTower.WHITE, null, 2, false);
        assertEquals(0, this.rule.getMotherNatureExtraMovement());
    }
}