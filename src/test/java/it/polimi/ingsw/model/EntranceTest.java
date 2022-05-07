package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Entrance;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.exceptions.NoSuchStudentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class EntranceTest {
    Entrance entrance;

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
        assertThrows(NoSuchStudentException.class, () -> entrance.removeStudent(0));
        assertEquals(7, entrance.getAllStudents().size());
    }
}
