package it.polimi.ingsw.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class Dashboard
 */
public class DashboardTest {
    Dashboard dashboard;

    /**
     * This method is called before each test, it creates a new dashboard with some students in it
     */
    @BeforeEach
    public void init(){
        ArrayList<Student> students = new ArrayList<>();
        for(int i=0; i<7; i++){
            Color c = null;
            int caseColor = i%5;
            switch (caseColor) {
                case 0 -> c = Color.RED;
                case 1 -> c = Color.BLUE;
                case 2 -> c = Color.GREEN;
                case 3 -> c = Color.PINK;
                case 4 -> c = Color.YELLOW;
            }
            Student s = new Student(i, c);
            students.add(s);
        }
        Collections.shuffle(students);
        dashboard = new Dashboard(2, students, ColorTower.BLACK);
    }

    /**
     * This method tests the correct behaviour of the towers
     */
    @Test
    public void towersTest(){
        assertEquals(8, dashboard.getTowers());
        dashboard.removeTowers(1);
        assertEquals(7, dashboard.getTowers());
        dashboard.addTowers(1);
        assertEquals(8, dashboard.getTowers());
    }

    /**
     * This method tests the correct behaviour of the graveyard
     */
    @Test
    public void graveyardTest(){
        assertNull(dashboard.getGraveyard());
        dashboard.setGraveyard(new Card(1,1));
        assertEquals(new Card(1,1), dashboard.getGraveyard());
    }

    /**
     * This method tests the getter of the dashboard
     */
    @Test
    public void returnTest(){
        Canteen canteen = dashboard.getCanteen();
        Entrance entrance = dashboard.getEntrance();

        for(Color c : Color.values()){
            assertEquals(0, canteen.getNumberStudentColor(c));
        }
        assertEquals(7, entrance.getAllStudents().size());
    }
}