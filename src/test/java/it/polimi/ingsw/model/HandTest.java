package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.exceptions.OverflowCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



import java.util.ArrayList;

/**
 * This class contains the test for the class Hand
 */
public class HandTest {
    private Hand hand;

    /**
     * This method is called before every test, it creates a new Hand
     */
    @BeforeEach
    public void init(){
        hand = new Hand();
    }


    /**
     * This method check the correct extraction of the card from the Hand and if the exception is correctly triggered
     */
    @Test
    public void getNumCardTest(){
        for(int i=0; i <10; i++){
            try{
                hand.playCard(i+1);
            }
            catch(Exception e){
                fail();
            }
        }
        assertThrows(OverflowCardException.class, () -> hand.playCard(0));
    }

    /**
     * This method check if the exception OverflowCardException is thrown correctly for every card number
     */
    @Test
    public void playOverflowCard(){
        for(int i = 1; i <= 10; i++){
            int finalI = i;
            assertThrows(OverflowCardException.class, () -> hand.playCard(finalI -1));
            try {
                hand.playCard(i);
            } catch (OverflowCardException e) {e.printStackTrace();}
        }
    }

    /**
     * This method check if is returned the right card
     */
    @RepeatedTest(10)
    public void testExtraction(){
        int rand = (int)(Math.random()*10);
        try {
            Card c = hand.getAllCards().get(rand);
            Card c2 = hand.playCard(rand+1);
            assertEquals(c, c2);
        }
        catch(Exception e){
            fail();
        }
    }


    /**
     * This method check if all cards are created correctly
     */
    @Test
    public void checksCardsCreated(){
        ArrayList<Card> cards = hand.getAllCards();
        assertEquals(1, cards.get(0).getPriority());
        assertEquals(1, cards.get(0).getMovement());
        assertEquals(2, cards.get(1).getPriority());
        assertEquals(1, cards.get(1).getMovement());
        assertEquals(3, cards.get(2).getPriority());
        assertEquals(2, cards.get(2).getMovement());
        assertEquals(4, cards.get(3).getPriority());
        assertEquals(2, cards.get(3).getMovement());
        assertEquals(5, cards.get(4).getPriority());
        assertEquals(3, cards.get(4).getMovement());
        assertEquals(6, cards.get(5).getPriority());
        assertEquals(3, cards.get(5).getMovement());
        assertEquals(7, cards.get(6).getPriority());
        assertEquals(4, cards.get(6).getMovement());
        assertEquals(8, cards.get(7).getPriority());
        assertEquals(4, cards.get(7).getMovement());
        assertEquals(9, cards.get(8).getPriority());
        assertEquals(5, cards.get(8).getMovement());
        assertEquals(10, cards.get(9).getPriority());
        assertEquals(5, cards.get(9).getMovement());
    }
}