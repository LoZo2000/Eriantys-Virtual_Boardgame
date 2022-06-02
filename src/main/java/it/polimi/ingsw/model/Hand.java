package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.OverflowCardException;
import java.util.ArrayList;

/**
 * The class hand represent the hand of a player, inside this class are stored all the card remaining to the player,
 * the principal methods in the class are a getter of all the cards and a method that permit to play a card
 */
public class Hand{
    private ArrayList<Card> cards;

    /**
     * Creator of the class Hand. This class contains all the player's cards.
     */
    public Hand(){
        cards = new ArrayList<>();

        for(int i=0; i<10; i++)
            cards.add(new Card(i+1,i/2+1));
    }

    /**
     * Method to return a copy of the player's cards
     * @return an ArrayList containing all player's cards
     */
    public ArrayList<Card> getAllCards(){
        return (ArrayList<Card>)cards.clone();
    }

    /**
     * Method to play a card. It removes the card from the Hand and returns it to the caller
     * @param pos is the priority (and consequentely the id) of the card to play
     * @return the card itself
     * @throws OverflowCardException if the card with that priority has been already played
     */
    public Card playCard(int pos) throws OverflowCardException {
        for(Card c : cards){
            if(c.getPriority()==pos){
                cards.remove(c);
                return c;
            }
        }
        throw new OverflowCardException("There is no such card!");
    }
}