package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.MotherNature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class contains the test for the class MotherNature
 */
public class MotherNatureTest {
    private MotherNature motherNature;

    /**
     * This method is called before each test, it creates a new mother nature to use in the test
     */
    @BeforeEach
    public void init(){
        motherNature = new MotherNature();
    }

    /**
     * This method tests the movement of mother nature
     */
    @Test
    public void movementTest(){
        assertNull(motherNature.getPosition());
        Island i0 = new Island(0);
        motherNature.movement(i0);
        assertEquals(i0, motherNature.getPosition());
    }
}