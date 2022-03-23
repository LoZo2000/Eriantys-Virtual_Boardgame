package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Report;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportTest {

    @Test
    public void reportTest(){
        Report report = new Report(ColorTower.BLACK, 0, 1, 2, 3, 4, 5);
        assertEquals(ColorTower.BLACK, report.getOwner());
        assertEquals(0, report.getTowerNumbers());
        assertEquals(1, report.getColorStudents(Color.BLUE));
        assertEquals(2, report.getColorStudents(Color.YELLOW));
        assertEquals(3, report.getColorStudents(Color.RED));
        assertEquals(4, report.getColorStudents(Color.GREEN));
        assertEquals(5, report.getColorStudents(Color.PINK));
    }
}