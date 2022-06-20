package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.NoSuchStudentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class Island
 */
public class IslandTest {
    private Island island;

    /**
     * This method is called before each test, it creates a new island
     */
    @BeforeEach
    public void init(){
        island = new Island(0);
    }

    /**
     * This method tests the getters of the class Island
     */
    @Test
    public void getterTest(){
        assertEquals("00", island.getId());
        assertEquals(0, island.getReport().getTowerNumbers());
        assertEquals(0, island.getAllStudents().size());
        assertNull(island.getReport().getOwner());
        island.conquest(ColorTower.BLACK);
        assertEquals(ColorTower.BLACK, island.getOwner());
        assertEquals(1, island.getReport().getTowerNumbers());

        for(Color c : Color.values()) assertEquals(0, island.getReport().getColorStudents(c));
    }

    /**
     * This method tests the add student method
     */
    @Test
    public void addingTest(){
        island.addStudent(new Student(0, Color.BLUE));
        island.addStudent(new Student(1, Color.YELLOW));
        island.addStudent(new Student(2, Color.RED));
        island.addStudent(new Student(3, Color.GREEN));
        island.addStudent(new Student(4, Color.PINK));
        island.conquest(ColorTower.BLACK);
        assertEquals(5, island.getAllStudents().size());
        assertEquals(0, island.getAllStudents().get(0).getId());
        assertEquals(1, island.getReport().getTowerNumbers());
        assertEquals(ColorTower.BLACK, island.getReport().getOwner());

        for(Color c : Color.values()) assertEquals(1, island.getReport().getColorStudents(c));

        try{
            Student s = island.removeStudent(0);
        }catch(Exception e){
            fail();
        }
        assertThrows(NoSuchStudentException.class, () -> island.removeStudent(0));
    }

    /**
     * This method tests the creation of a new island given two existing islands
     */
    @Test
    public void createFromMerging(){
        island.addStudent(new Student(2, Color.BLUE));
        island.conquest(ColorTower.BLACK);
        Island i2 = new Island(1);
        i2.addStudent(new Student(3, Color.YELLOW));
        i2.conquest(ColorTower.BLACK);

        Island newIsland = new Island(island, i2);
        assertEquals("00, 01", newIsland.getId());
        assertEquals(2, newIsland.getAllStudents().size());
        assertEquals(2, newIsland.getAllStudents().get(0).getId());
        assertEquals(3, newIsland.getAllStudents().get(1).getId());
        assertEquals(2, newIsland.getReport().getTowerNumbers());
        assertEquals(ColorTower.BLACK, island.getReport().getOwner());
    }

    /**
     * This method test the setting of the prohibition token
     */
    @Test
    public void prohibitionTest(){
        assertFalse(island.getProhibition());
        island.setProhibition(true);
        assertTrue(island.getProhibition());
    }

    /**
     * This method tests the equal method for the class Island
     */
    @Test
    public void equalsTest(){
        Island i2 = new Island(0);
        Island i3 = new Island(1);
        assertEquals(island, i2);
        assertEquals(i2, island);
        assertNotEquals(island, i3);
        assertNotEquals(i3, island);

        assertNotEquals(i2, new Object());
    }

    /**
     * This method tests the toString method for the class Island
     */
    @Test
    public void toStringTest(){
        assertEquals("00", island.toString());
    }
}