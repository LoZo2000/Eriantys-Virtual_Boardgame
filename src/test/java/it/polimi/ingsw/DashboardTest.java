package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardTest {
    Dashboard dashboard;

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
        dashboard = new Dashboard(students);
    }

    @Test
    public void towersTest(){
        assertEquals(8, dashboard.getTowers());
        try{
            dashboard.removeTowers(1);
        }catch (Exception e){
            fail();
        }
        assertEquals(7, dashboard.getTowers());
        try{
            dashboard.addTowers(1);
        }catch(Exception e){
            fail();
        }
        assertEquals(8, dashboard.getTowers());

        try{
            dashboard.removeTowers(9);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            dashboard.addTowers(1);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void graveyardTest(){
        assertNull(dashboard.getGraveyard());
        dashboard.setGraveyard(new Card(1,1));
        assertEquals(new Card(1,1), dashboard.getGraveyard());
    }

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
