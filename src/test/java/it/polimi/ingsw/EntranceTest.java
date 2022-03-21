package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Entrance;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class EntranceTest {
    Entrance entrance;

    @BeforeEach
    public void init(){
        entrance = new Entrance();
    }

    @Test
    public void setRemoveTest(){
        entrance.addStudent(new Student(0, Color.BLUE));
        try{
            entrance.removeStudent(0);
        }catch (Exception e){
            fail();
        }
        try{
            entrance.removeStudent(0);
            fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
