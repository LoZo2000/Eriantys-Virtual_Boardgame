package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;


public class BagTest{
    private Bag bag;

    //Creates a bag containing 120 students
    @BeforeEach
    public void init(){
        Stack<Student> students = new Stack<>();
        students.push(new Student(0, Color.BLUE));
        students.push(new Student(1, Color.YELLOW));
        students.push(new Student(2, Color.RED));
        students.push(new Student(3, Color.GREEN));
        students.push(new Student(4, Color.PINK));
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
        } catch (NoMoreStudentsException e) {
            e.printStackTrace();
        }
        try{
            ArrayList<Student> s= new ArrayList<Student>();
            s = bag.getRandomStudent(2);
            fail();
        } catch (NoMoreStudentsException e) {
            e.printStackTrace();
        }
    }
}