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
        students.put(Color.YELLOW, 0);
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
        this.rule = new InfluenceRule(ColorTower.WHITE, 0, true);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.WHITE, winner);
    }

    @Test
    void calculateInfluenceExtraPoints() {
        this.rule = new InfluenceRule(ColorTower.WHITE, 2, false);

        ColorTower winner = this.rule.calculateInfluence(this.rep, this.professors);

        assertEquals(ColorTower.WHITE, winner);
    }

    @Test
    void updateProfessor() {
    }

    @Test
    void isActionNeeded() {
    }
}