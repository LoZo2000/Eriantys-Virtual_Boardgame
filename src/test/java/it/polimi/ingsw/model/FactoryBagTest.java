package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.FactoryBag;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class contains the test for the class FactoryBag
 */
public class FactoryBagTest {
    FactoryBag fb;

    /**
     * This method is called before each test, create a FactoryBag
     */
    @BeforeEach
    public void init(){
        fb = new FactoryBag();
    }

    /**
     * This method check if the method getInitBag in FactoryBag works properly
     */
    @Test
    public void initBagTest(){
        Bag initBag = fb.getInitBag();
        int[] cont = {0,0,0,0,0};
        for(int i=0; i<10; i++){
            try {
                Student s = initBag.getRandomStudent();
                switch (s.getColor()){
                    case BLUE -> cont[0]++;
                    case YELLOW -> cont[1]++;
                    case RED -> cont[2]++;
                    case GREEN -> cont[3]++;
                    case PINK -> cont[4]++;
                }
            }catch(Exception e){
                fail();
            }
        }
        for(int i=0; i<5; i++) assertEquals(2, cont[i]);
    }


    /**
     *  This method check if the method getBag in FactoryBag works properly
     */
    @Test
    public void bagTest(){
        Bag bag = fb.getBag();
        int[] cont = {0,0,0,0,0};
        for(int i=0; i<120; i++){
            try {
                Student s = bag.getRandomStudent();
                switch (s.getColor()){
                    case BLUE -> cont[0]++;
                    case YELLOW -> cont[1]++;
                    case RED -> cont[2]++;
                    case GREEN -> cont[3]++;
                    case PINK -> cont[4]++;
                }
            }catch(Exception e){
                fail();
            }
        }
        for(int i=0; i<5; i++) assertEquals(24, cont[i]);
    }
}
