package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest{


    @Test
    public void checkGetter(){
        for(int i=1; i<10; i++){
            for(int j=1; j<5; j++){
                Card c = new Card(i,j);
                assertEquals(i, c.getPriority());
                assertEquals(j, c.getMovement());
            }
        }
    }

    @Test
    public void equalsTest(){
        Card c1 = new Card(10, 5);
        Card c2 = new Card(10, 5);
        Card c3 = new Card(9, 5);
        assertEquals(c1,c2);
        assertEquals(c2,c1);
        assertNotEquals(c1,c3);
        assertNotEquals(c3,c1);
    }

    @Test
    public void toStringTest(){
        Card c1 = new Card(10, 5);
        assertEquals("(p:10, m:5)", c1.toString());
    }
}
