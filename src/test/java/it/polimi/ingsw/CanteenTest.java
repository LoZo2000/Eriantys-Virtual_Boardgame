package it.polimi.ingsw;

import it.polimi.ingsw.model.Canteen;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.exceptions.NoSuchStudentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CanteenTest {
    private Canteen canteen;

    @BeforeEach
    void init(){
        canteen = new Canteen();
    }

    @Test
    public void addGetRemoveTest(){
        canteen.addStudent(new Student(0, Color.BLUE));
        canteen.addStudent(new Student(1, Color.YELLOW));
        canteen.addStudent(new Student(2, Color.RED));
        canteen.addStudent(new Student(3, Color.GREEN));
        canteen.addStudent(new Student(4, Color.PINK));
        assertEquals(1, canteen.getNumberStudentColor(Color.BLUE));
        assertEquals(1, canteen.getNumberStudentColor(Color.YELLOW));
        assertEquals(1, canteen.getNumberStudentColor(Color.RED));
        assertEquals(1, canteen.getNumberStudentColor(Color.GREEN));
        assertEquals(1, canteen.getNumberStudentColor(Color.PINK));

        ArrayList<Student> students = canteen.getStudents(Color.BLUE);
        assertEquals(0, students.get(0).getId());
        students = canteen.getStudents(Color.YELLOW);
        assertEquals(1, students.get(0).getId());
        students = canteen.getStudents(Color.RED);
        assertEquals(2, students.get(0).getId());
        students = canteen.getStudents(Color.GREEN);
        assertEquals(3, students.get(0).getId());
        students = canteen.getStudents(Color.PINK);
        assertEquals(4, students.get(0).getId());

        try {
            canteen.removeStudent(0);
            assertEquals(0, canteen.getNumberStudentColor(Color.BLUE));
            canteen.removeStudent(1);
            assertEquals(0, canteen.getNumberStudentColor(Color.YELLOW));
            canteen.removeStudent(2);
            assertEquals(0, canteen.getNumberStudentColor(Color.RED));
            canteen.removeStudent(3);
            assertEquals(0, canteen.getNumberStudentColor(Color.GREEN));
            canteen.removeStudent(4);
            assertEquals(0, canteen.getNumberStudentColor(Color.PINK));
        } catch(Exception e){
            fail();
        }

        assertThrows(NoSuchStudentException.class, () -> canteen.removeStudent(0));
    }
}
