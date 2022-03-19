package it.polimi.ingsw;

import it.polimi.ingsw.model.Canteen;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

        try {
            canteen.removeStudent(Color.BLUE);
            assertEquals(0, canteen.getNumberStudentColor(Color.BLUE));
            canteen.removeStudent(Color.YELLOW);
            assertEquals(0, canteen.getNumberStudentColor(Color.YELLOW));
            canteen.removeStudent(Color.RED);
            assertEquals(0, canteen.getNumberStudentColor(Color.RED));
            canteen.removeStudent(Color.GREEN);
            assertEquals(0, canteen.getNumberStudentColor(Color.GREEN));
            canteen.removeStudent(Color.PINK);
            assertEquals(0, canteen.getNumberStudentColor(Color.PINK));
        }
        catch(Exception e){fail();}
        try{
            canteen.removeStudent(Color.BLUE);
            fail();
        }
        catch(Exception e){e.printStackTrace();}
        try{
            canteen.removeStudent(Color.YELLOW);
            fail();
        }
        catch(Exception e){e.printStackTrace();}
        try{
            canteen.removeStudent(Color.RED);
            fail();
        }
        catch(Exception e){e.printStackTrace();}
        try{
            canteen.removeStudent(Color.GREEN);
            fail();
        }
        catch(Exception e){e.printStackTrace();}
        try{
            canteen.removeStudent(Color.PINK);
            fail();
        }
        catch(Exception e){e.printStackTrace();}
    }
}
