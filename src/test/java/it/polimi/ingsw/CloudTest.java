package it.polimi.ingsw;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.spec.ECField;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CloudTest {
    private Cloud cloud;

    @BeforeEach
    public void init(){
        cloud = new Cloud(3);
    }

    @Test
    public void fillTest(){
        try{
            cloud.updateStudents(new Student(0, Color.BLUE));
            cloud.updateStudents(new Student(1, Color.BLUE));
            cloud.updateStudents(new Student(2, Color.BLUE));
        }
        catch(Exception e){fail();}
        try{
            cloud.updateStudents(new Student(3, Color.BLUE));
            fail();
        }
        catch (Exception e){e.printStackTrace();}

        ArrayList<Student> s = cloud.chooseCloud();
        for(int i=0; i<3; i++)
            assertEquals(Color.BLUE, s.get(i).getColor());

        try{
            cloud.updateStudents(new Student(4, Color.BLUE));
        }
        catch(Exception e){fail();}
    }

    @Test
    public void fillTest2(){
        ArrayList<Student> s = new ArrayList<Student>();
        s.add(new Student(0, Color.BLUE));
        s.add(new Student(1, Color.BLUE));
        s.add(new Student(2, Color.BLUE));
        try {
            cloud.updateStudents(s);
        }catch(Exception e){fail();}
        s = cloud.getStudents();
        for(int i=0; i<3; i++)
            assertEquals(Color.BLUE, s.get(i).getColor());
        s.add(new Student(3, Color.BLUE));
        try {
            cloud.updateStudents(s);
            fail();
        }catch(Exception e){e.printStackTrace();}
    }
}
