package it.polimi.ingsw;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.MotherNature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MotherNatureTest {
    private MotherNature motherNature;

    @BeforeEach
    public void init(){
        motherNature = new MotherNature();
    }

    @Test
    public void createTest(){
        Island i0 = new Island(0);
        motherNature = new MotherNature(i0);
        assertEquals(i0, motherNature.getPosition());
    }

    @Test
    public void movementTest(){
        assertNull(motherNature.getPosition());
        Island i0 = new Island(0);
        motherNature.movement(i0);
        assertEquals(i0, motherNature.getPosition());
    }

    @Test
    public void createMNTest(){
        Island i1 = new Island(1);
        MotherNature mn1 = new MotherNature(i1);
        assertEquals(i1, mn1.getPosition());
    }
}