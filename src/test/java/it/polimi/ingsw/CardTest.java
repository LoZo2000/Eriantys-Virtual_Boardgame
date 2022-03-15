package it.polimi.ingsw;

import it.polimi.ingsw.model.Card;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest{

    @Test
    public void checkGetter(){
        for(int i=1; i<10; i++){
            for(int j=1; j<5; j++){
                Card c = new Card(i,j);
                assertEquals(i, c.getInitiative());
                assertEquals(j, c.getMovement());
            }
        }
    }
}
