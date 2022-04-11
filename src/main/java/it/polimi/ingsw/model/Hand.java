package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.OverflowCardException;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {
    private ArrayList<Card> cards;

    public Hand(){
        cards = new ArrayList<>();

        for(int i=0; i<10; i++)
            cards.add(new Card(i+1,i/2+1));
    }

    public int getNumCards(){
        return cards.size();
    }

    public ArrayList<Card> getAllCards(){
        return (ArrayList<Card>)cards.clone();
    }

    //Returns the selected card and deletes it
    public Card playCard(int pos) throws OverflowCardException {
        for(Card c : cards){
            if(c.getPriority()==pos){
                cards.remove(c);
                return c;
            }
        }
        throw new OverflowCardException();
    }
}