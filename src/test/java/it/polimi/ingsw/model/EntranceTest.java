package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class Entrance
 */
public class EntranceTest {
    Entrance entrance;

    /**
     * This method is called before each test, it creates an entrance and put some students in it
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

        entrance = new Entrance(students);
    }

    /**
     * This method tests the getter of the student
     */
    @Test
    public void getStudentsTest(){
        ArrayList<Student> students = entrance.getAllStudents();
        /*for(Student s : students){
            System.out.println(s.getId() + ", " + s.getColor());
        }*/

        for(int i = 0; i<students.size(); i++){
            Student temp = new Student(i, Color.RED);
            assertNotEquals(-1, students.indexOf(temp));
        }

    }

    /**
     * This method test the remove student and the add student
     */
    @Test
    public void setRemoveTest(){
        assertEquals(7, entrance.getAllStudents().size());
        try{
            entrance.removeStudent(0);
        }catch (Exception e){
            fail();
        }
        assertEquals(6, entrance.getAllStudents().size());
        entrance.addStudent(new Student(8, Color.PINK));
        assertEquals(7, entrance.getAllStudents().size());
        assertThrows(IllegalMoveException.class, () -> entrance.removeStudent(0));
        assertEquals(7, entrance.getAllStudents().size());
    }
}
