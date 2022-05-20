package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.GameManager;
import it.polimi.ingsw.server.Observer;

import java.util.List;

public class Controller implements Observer<Message> {

    private final Game game;
    private final GameHandler gameHandler;
    private final GameManager gameManager;

    public Controller(Game game, GameManager gameManager){
        this.game = game;
        this.gameHandler = new GameHandler(game);
        this.gameManager = gameManager;
    }

    @Override
    public void update(Message message) {
        try {
            gameHandler.execute(message);
            if(game.getFinishedGame()){
                this.gameManager.removeFinishedGame(this);
            }
        }catch(Exception e){
            game.sendErrorNote(message.getSender(), e.toString(), game.getCurrentPlayer());
        }
    }

    public List<String> getNicknames(){
        return gameHandler.getNicknames();
    }

    public void disconnectedPlayer(String nickname){
        this.gameManager.removeFinishedGame(this);
        this.game.sendDisconnectionAll(nickname);
    }
}