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
}
