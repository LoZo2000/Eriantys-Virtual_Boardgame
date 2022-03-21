package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {
    private Island island;

    @BeforeEach
    public void init(){
        island = new Island(0);
    }

    @Test
    public void getterTest(){
        assertEquals(0, island.getId());
        assertEquals(1, island.getMaxTowers());
        assertEquals(0, island.getAllStudents().size());
        assertEquals(0, island.getAllTowers().size());
        assertNull(island.getCurrentOwner());
        for(Color c : Color.values()) assertEquals(0, island.getNumberStudentColor(c));
    }

    @Test
    //public void addingTest(){
    //    island.addStudent(new Student(0, Color.BLUE));
            //   try {
    ////    }catch(Exception e){
    //        fail();
     //   }
     //   assertEquals(1, island.getAllStudents().size());
     //   assertEquals(0, island.getAllStudents().get(0).getId());
    //    assertEquals(1, island.getAllTowers().size());
     //   assertEquals(0, island.getAllTowers().get(0).getId());
    //    assertEquals(ColorTower.BLACK, island.getCurrentOwner());
    //    try {
   //         island.conquest(new Tower(1, ColorTower.BLACK));
      //      fail();
      //  }catch(Exception e){
    //        e.printStackTrace();
     //   }
     //   try {
     //       island.conquest(new Tower(0, ColorTower.WHITE));
     //   }catch(Exception e){
      //      fail();
    //    }
   // }

    @Test
    public void createFromMerging(){
        island.addStudent(new Student(2, Color.BLUE));
        Island i2 = new Island(1);
        i2.addStudent(new Student(3, Color.YELLOW));
        try {
           // island.conquest(new Tower(4, ColorTower.BLACK));
          //  i2.conquest(new Tower(5, ColorTower.BLACK));
        }catch(Exception e){
            fail();
        }
        Island newIsland = new Island(island, i2);
        assertEquals(0, newIsland.getId());
        assertEquals(2, newIsland.getAllStudents().size());
        assertEquals(2, newIsland.getAllStudents().get(0).getId());
        assertEquals(3, newIsland.getAllStudents().get(1).getId());
     //   assertEquals(2, newIsland.getAllTowers().size());
       // assertEquals(4, newIsland.getAllTowers().get(0).getId());
      //  assertEquals(5, newIsland.getAllTowers().get(1).getId());
        assertEquals(ColorTower.BLACK, island.getCurrentOwner());

        ArrayList<Tower> winnersTowers = new ArrayList<Tower>();
        winnersTowers.add(new Tower(6, ColorTower.WHITE));
        winnersTowers.add(new Tower(7, ColorTower.WHITE));
        try {
            newIsland.conquest(winnersTowers);
        }catch(Exception e){
            fail();
        }

        ArrayList<Tower> winnersTowers2 = new ArrayList<Tower>();
        winnersTowers2.add(new Tower(8, ColorTower.WHITE));
        winnersTowers2.add(new Tower(9, ColorTower.WHITE));
        try {
            newIsland.conquest(winnersTowers2);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }

        ArrayList<Tower> winnersTowers3 = new ArrayList<Tower>();
        winnersTowers3.add(new Tower(8, ColorTower.GREY));
        winnersTowers3.add(new Tower(9, ColorTower.BLACK));
        try {
            newIsland.conquest(winnersTowers3);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }

        ArrayList<Tower> winnersTowers4 = new ArrayList<Tower>();
        winnersTowers4.add(new Tower(8, ColorTower.BLACK));
        try {
            newIsland.conquest(winnersTowers4);
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void prohibitionTest(){
        assertFalse(island.getProhibition());
        island.setProhibition(true);
        assertTrue(island.getProhibition());
    }

    @Test void getNumberStudentColor(){
        int blue = (int)(Math.random()*10);
        for(int i=0; i<blue; i++) island.addStudent(new Student(i, Color.BLUE));
        int yellow = (int)(Math.random()*10);
        for(int i=0; i<yellow; i++) island.addStudent(new Student(i, Color.YELLOW));
        int red = (int)(Math.random()*10);
        for(int i=0; i<red; i++) island.addStudent(new Student(i, Color.RED));
        int green = (int)(Math.random()*10);
        for(int i=0; i<green; i++) island.addStudent(new Student(i, Color.GREEN));
        int pink = (int)(Math.random()*10);
        for(int i=0; i<pink; i++) island.addStudent(new Student(i, Color.PINK));
        assertEquals(blue, island.getNumberStudentColor(Color.BLUE));
        assertEquals(yellow, island.getNumberStudentColor(Color.YELLOW));
        assertEquals(red, island.getNumberStudentColor(Color.RED));
        assertEquals(green, island.getNumberStudentColor(Color.GREEN));
        assertEquals(pink, island.getNumberStudentColor(Color.PINK));
    }
}
