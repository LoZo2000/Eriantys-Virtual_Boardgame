package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Game;

/**
 * The class AddMeMessage represent the action that add a player in the game, extends the class Message and implement the method
 * execute that apply changes to the game
 */
public class AddMeMessage extends Message{
    final private boolean completeRules;
    final private int maxPlayers;

    /**
     * The constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param completeRules is a boolean that report if the game has complete rules or has the simple rules
     * @param maxPlayers is an int that report the number of players of the game
     */
    public AddMeMessage(String sender, boolean completeRules, int maxPlayers){
        super(sender, Action.ADDME);
        this.completeRules = completeRules;
        this.maxPlayers = maxPlayers;
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to add
     * a new player
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     */
    @Override
    public Update execute(Game game) {
        if(game.getRegisteredNumPlayers() == 1 || game.getRegisteredNumPlayers()==3)
            game.addPlayer(sender, ColorTower.WHITE);
        else if(game.getRegisteredNumPlayers() == 0 && game.getNumPlayers() == 3)
            game.addPlayer(sender, ColorTower.GREY);
        else
            game.addPlayer(sender, ColorTower.BLACK);

        return new Update(game.getRegisteredNumPlayers(), null, null, null, null, null, null, null, null);
    }

    /**
     * This method return if the game has complete rules
     * @return a boolean that report if the game has complete rules
     */
    public boolean getCompleteRules(){
        return completeRules;
    }

    /**
     * This method return the number of players of the game
     * @return an int representing the number of player of the game
     */
    public int getNumPlayers(){
        return maxPlayers;
    }

}
