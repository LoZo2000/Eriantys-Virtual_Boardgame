package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;

import junit.framework.TestCase;
import org.junit.jupiter.api.*;



public class StudentTest extends TestCase {

    //Checks if 'getColor' works correctly
    @Test
    public void getColor(){
        for(Color c : Color.values()){
            Student s = new Student(c);
            assertEquals(c, s.getColor());
        }
    }
}
