package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class BagTest{
    private Bag bag;

    //Creates a bag containing 120 students
    @BeforeEach
    public void init(){
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(0, Color.BLUE));
        students.add(new Student(1, Color.YELLOW));
        students.add(new Student(2, Color.RED));
        students.add(new Student(3, Color.GREEN));
        students.add(new Student(4, Color.PINK));
        this.bag = new Bag(students);
    }



    //Checks if 'getStudentsNum' works correctly
    @Test
    public void checkStudentsNum(){
        for(int i=0; i<5; i++){
            try {
                assertEquals(5-i, bag.getStudentsNum());
                Student s = bag.getRandomStudent();
            }
            catch(Exception e){
                fail();
            }
        }
        assertEquals(0, bag.getStudentsNum());
        try{
            Student s = bag.getRandomStudent();
            fail();
        }
        catch(Exception e){e.printStackTrace();}
    }
}