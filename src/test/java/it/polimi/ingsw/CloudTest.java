package it.polimi.ingsw;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            cloud.addStudent(new Student(0, Color.BLUE));
            cloud.addStudent(new Student(1, Color.BLUE));
            cloud.addStudent(new Student(2, Color.BLUE));
        }
        catch(Exception e){fail();}
        try{
            cloud.addStudent(new Student(3, Color.BLUE));
            fail();
        }
        catch (Exception e){e.printStackTrace();}

        ArrayList<Student> s1 = cloud.getStudents();
        for(int i=0; i<3; i++)
            assertEquals(Color.BLUE, s1.get(i).getColor());

        ArrayList<Student> s2 = cloud.chooseCloud();
        for(int i=0; i<3; i++)
            assertEquals(Color.BLUE, s2.get(i).getColor());

        ArrayList<Student> s3 = cloud.getStudents();
        assertEquals(0, s3.size());

        try{
            cloud.addStudent(new Student(4, Color.BLUE));
        }
        catch(Exception e){fail();}
    }
}
