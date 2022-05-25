package it.polimi.ingsw.model;

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
                if(s.getColor()==Color.BLUE) assertEquals("/Stud_blue.png", s.getSprite());
                else if(s.getColor()==Color.YELLOW) assertEquals("/Stud_yellow.png", s.getSprite());
                else if(s.getColor()==Color.RED) assertEquals("/Stud_red.png", s.getSprite());
                else if(s.getColor()==Color.GREEN) assertEquals("/Stud_green.png", s.getSprite());
                else assertEquals("/Stud_pink.png", s.getSprite());
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
