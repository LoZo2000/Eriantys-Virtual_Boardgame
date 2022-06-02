package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.GameManager;
import it.polimi.ingsw.server.Observer;

import java.util.List;

/**
 * The calss controller represent the controller of the game in the MVC patter and permit the implementation
 * of the Observer pattern
 */
public class Controller implements Observer<Message> {

    private final Game game;
    private final GameHandler gameHandler;
    private final GameManager gameManager;

    /**
     * This method is the constructor of the class controller, assign the attributes to the class
     * and create a gameHandler
     * @param game assign the Game to the class
     * @param gameManager assign the gameManager to the class
     */
    public Controller(Game game, GameManager gameManager){
        this.game = game;
        this.gameHandler = new GameHandler(game);
        this.gameManager = gameManager;
    }

    /**
     * This method is the override of the update method of the Observer pattern when the controller is notified, this method
     * is called
     * @param message is the message that will be executed by the gameHandler
     */
    @Override
    public void update(Message message) {
        try {
            gameHandler.execute(message);
            if(game.getFinishedGame()){
                this.gameManager.removeFinishedGame(this, false);
            }
        }catch(Exception e){
            game.sendErrorNote(message.getSender(), e.getMessage(), game.getCurrentPlayer());
        }
    }

    /**
     * This metod return the nicknames of the players in the game
     * @return a list of string containing the nicknames of the players in the game
     */
    public List<String> getNicknames(){
        return gameHandler.getNicknames();
    }

    /**
     * Method that return the number of players in the game
     * @return an int representing the number of players in the game
     */
    public int getNumPlayers(){
        return game.getNumPlayers();
    }

    /**
     * Method that report if the game is simple or with the complete rules
     * @return a boolean that report if the game is simple or complete
     */
    public boolean getCompleteRules(){
        return game.getCompleteRules();
    }

    /**
     * This method is called when a player leave the game, reports to all the players to disconnect
     * @param nickname is the name of the player that left the game
     */
    public void disconnectedPlayer(String nickname){
        this.gameManager.removeFinishedGame(this, !gameHandler.isStarted());
        this.game.sendDisconnectionAll(nickname);
    }
}