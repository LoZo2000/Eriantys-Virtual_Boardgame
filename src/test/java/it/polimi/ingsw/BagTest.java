package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BagTest{
    Bag bag;

    //Creates a bag containing 120 students
    @BeforeEach
    public void init(){
        this.bag = new Bag(24);
    }



    //Checks if 'Bag' creates a list containing 120 students (24 for each colors)
    @Test
    public void checkTotalStudents(){
        int[] amount = {0,0,0,0,0};
        for(int i=0; i<120; i++){
            try {
                Student s = bag.getRandomStudent();
                switch (s.getColor()) {
                    case BLUE:
                        amount[0]++;
                        break;
                    case YELLOW:
                        amount[1]++;
                        break;
                    case RED:
                        amount[2]++;
                        break;
                    case GREEN:
                        amount[3]++;
                        break;
                    case PINK:
                        amount[4]++;
                        break;
                }
            }
            catch(Exception e){
                fail();
            }
        }
        for(int i=0; i<5; i++)
            assertEquals(24, amount[i]);
    }



    //Checks if exception is triggered after 120 students:
    @Test
    public void checkTriggeredException(){
        for(int i=0; i<120; i++){
            try {
                Student s = bag.getRandomStudent();
            }
            catch(Exception e){
                fail();
            }
        }
        try{
            Student s = bag.getRandomStudent();
            fail();
        }
        catch(Exception e){e.printStackTrace();}
    }



    //Checks if 'getStudentsNum' works correctly
    @Test
    public void checkStudentsNum(){
        for(int i=0; i<120; i++){
            try {
                assertEquals(120-i, bag.getStudentsNum());
                Student s = bag.getRandomStudent();
            }
            catch(Exception e){
                fail();
            }
        }
        assertEquals(0, bag.getStudentsNum());
    }
}
