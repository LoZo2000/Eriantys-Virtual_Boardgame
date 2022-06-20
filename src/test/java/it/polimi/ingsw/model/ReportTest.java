package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class contains the test for the class Report
 */
public class ReportTest {

    private Map<Color, Integer> students;

    /**
     * This method is called before each test, it creates a hashmap with a determined amount of students for each color
     * to pass to the Report constructor
     */
    @BeforeEach
    public void init(){
        students = new HashMap<>();
        Color[] possibleColors = Color.values();
        int[] colorsCont = {1, 2, 3, 4, 5};
        for(int i = 0; i < possibleColors.length; i++){
            students.put(possibleColors[i], colorsCont[i]);
        }
    }

    /**
     * This method test the values inside a Report object
     */
    @Test
    public void reportTest(){
        Report report = new Report(ColorTower.BLACK, 0, students, null, 0);
        assertEquals(ColorTower.BLACK, report.getOwner());
        assertEquals(0, report.getTowerNumbers());
        assertEquals(1, report.getColorStudents(Color.BLUE));
        assertEquals(2, report.getColorStudents(Color.YELLOW));
        assertEquals(3, report.getColorStudents(Color.RED));
        assertEquals(4, report.getColorStudents(Color.GREEN));
        assertEquals(5, report.getColorStudents(Color.PINK));
        assertNull(report.getExtraPointReceiver());
        assertEquals(0, report.getExtraPoint());
    }
}