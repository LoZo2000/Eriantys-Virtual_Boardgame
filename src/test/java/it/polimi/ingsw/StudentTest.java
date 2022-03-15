package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest{

    //Checks if 'getColor' works correctly
    @Test
    public void getColor(){
        for(Color c : Color.values()){
            Student s = new Student(c);
            assertEquals(c, s.getColor());
        }
    }
}
