package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.exceptions.OverflowCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



import java.util.ArrayList;

public class HandTest {
    private Hand hand;

    @BeforeEach
    public void init(){
        hand = new Hand();
    }

    //Checks 'getNumCard' and a correct extraction and if exception is triggered
    @Test
    public void getNumCardTest(){
        for(int i=0; i <10; i++){
            assertEquals(10-i, hand.getNumCards());
            try{
                hand.playCard(i+1);
            }
            catch(Exception e){
                fail();
            }
        }
        assertEquals(0, hand.getNumCards());
        try{
            hand.playCard(0);
            fail();
        }
        catch(Exception e){e.printStackTrace();}
        assertEquals(0, hand.getNumCards());
    }

    //Checks if the exception is thrown if the position requested from playCard is higher than the size of the ArrayList
    @Test
    public void playOverflowCard(){
        for(int i = 0; i < 10; i++){
            assertThrows(OverflowCardException.class, () -> hand.playCard(hand.getNumCards()+1));
            try {
                hand.playCard(0);
            } catch (OverflowCardException e) {e.printStackTrace();}
        }
    }

    //Checks if it returns the right card

    @RepeatedTest(10)
    public void testExtraction(){
        int rand = (int)(Math.random()*10);
        assertEquals(10, hand.getNumCards());
        try {
            Card c = hand.getAllCards().get(rand);
            Card c2 = hand.playCard(rand+1);
            assertEquals(c, c2);
            assertEquals(9, hand.getNumCards());
        }
        catch(Exception e){
            fail();
        }
    }



    //Checks if all cards are created correctly
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