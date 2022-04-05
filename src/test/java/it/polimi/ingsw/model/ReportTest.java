package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportTest {

    private Map<Color, Integer> students;

    @BeforeEach
    public void init(){
        students = new HashMap<>();
        Color[] possibleColors = Color.values();
        int[] colorsCont = {1, 2, 3, 4, 5};
        for(int i = 0; i < possibleColors.length; i++){
            students.put(possibleColors[i], colorsCont[i]);
        }
    }

    @Test
    public void reportTest(){
        Report report = new Report(ColorTower.BLACK, 0, students);
        assertEquals(ColorTower.BLACK, report.getOwner());
        assertEquals(0, report.getTowerNumbers());
        assertEquals(1, report.getColorStudents(Color.BLUE));
        assertEquals(2, report.getColorStudents(Color.YELLOW));
        assertEquals(3, report.getColorStudents(Color.RED));
        assertEquals(4, report.getColorStudents(Color.GREEN));
        assertEquals(5, report.getColorStudents(Color.PINK));
    }
}