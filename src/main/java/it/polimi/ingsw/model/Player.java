package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotEnoughMoneyException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class player represent the player in the game, so its attribute are an hand, a dashboard, a nickname and a coin
 * the methods in the class are getter to access to its attribute and methods to modify the number of coins of the player
 */
public class Player implements Comparable<Player> {
    final private Hand hand;
    final private Dashboard dashboard;
    final private String nickname;
    private int coins;

    /**
     * Creator of the class Player
     * @param nickname is the nickname of the Player
     * @param numPlayers is the number of the Players in the match
     * @param color is the Player's towers'color
     * @param entranceStudents is a List containing all the Students to be placed in the Player's
     *                         Entrance
     */
    public Player(String nickname, int numPlayers, ColorTower color, ArrayList<Student> entranceStudents) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.dashboard = new Dashboard(numPlayers, entranceStudents, color);
        this.coins = 1;
    }

    /**
     * Method to return the Hand (an Object containing all Player's Cards)
     * @return the Player's Hand
     */
    public Hand getHand(){
        return hand;
    }

    /**
     * Method to return the Player's towers'color
     * @return the Player's towers'color
     */
    public ColorTower getColor(){
        return this.dashboard.getColor();
    }

    /**
     * Method to return the Player's Dashboard
     * @return the Player's Dashboard
     */
    public Dashboard getDashboard(){
        return dashboard;
    }

    /**
     * Method to return the Player's nickname
     * @return the Player's Nickname
     */
    public String getNickname(){
        return new String(nickname);
    }

    /**
     * Method to increase the Player's coins by one
     */
    public void giveCoin(){
        this.coins += 1;
    }

    /**
     * Method to decrease Player's coins after a card activation
     * @param usedCoins is the number of coins spent
     * @throws NotEnoughMoneyException if the Player hasn't enough money
     */
    public void useCoins(int usedCoins) throws NotEnoughMoneyException{
        if(this.coins < usedCoins){
            throw new NotEnoughMoneyException("You don't have enough money!");
        }
        this.coins -= usedCoins;
    }

    //TODO Test Method

    /**
     * Method to return the number of Player's coins
     * @return the number of Player's coins
     */
    public int getCoins(){
        return this.coins;
    }

    /**
     * Overriding method to compare two Players
     * @param obj is the other Student to be compared with this
     * @return true if the Players have the same nickname, false elsewhere
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }

        if(!(obj instanceof Player p)){
            return false;
        }

        return this.nickname.equals(p.nickname);
    }

    /**
     * Overriding method to print the Player's nickname
     * @return
     */
    @Override
    public String toString(){
        return nickname;
    }

    /**
     * Method to check which player has played the card with the lowest priority
     * @param p the object to be compared.
     * @return 1 if c2 has the initiative, -1 otherwise
     */
    @Override
    public int compareTo(Player p) {
        Card c1 = this.getDashboard().getGraveyard();
        Card c2 = p.getDashboard().getGraveyard();

        if(c1.getPriority() < c2.getPriority()){
            return -1;
        } else{
            return 1;
        }
    }
}