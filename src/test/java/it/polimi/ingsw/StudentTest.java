package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest{

    @Test
    public void getterTest(){
        for(int i=0; i<50; i++){
            for(Color c : Color.values()){
                Student s = new Student(i, c);
                assertEquals(i, s.getId());
                assertEquals(c, s.getColor());
            }
        }
    }

    @Test
    public void equalsTest(){
        Student s1 = new Student(0, Color.BLUE);
        Student s2 = new Student(0, Color.YELLOW);
        Student s3 = new Student(1, Color.BLUE);
        assertEquals(s1,s2);
        assertEquals(s2,s1);
        assertNotEquals(s1,s3);
        assertNotEquals(s3,s1);
    }

    @Test
    public void toStringTest(){
        Student s1 = new Student(0, Color.BLUE);
        Student s2 = new Student(1, Color.YELLOW);
        Student s3 = new Student(2, Color.RED);
        Student s4 = new Student(3, Color.GREEN);
        Student s5 = new Student(4, Color.PINK);
        System.out.println("The student is "+s1);
        System.out.println("The student is "+s2);
        System.out.println("The student is "+s3);
        System.out.println("The student is "+s4);
        System.out.println("The student is "+s5);

    }
}
