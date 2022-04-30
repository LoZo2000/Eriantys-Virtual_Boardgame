package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.Observer;

public class Controller implements Observer<Message> {

    private Game game;
    private GameHandler2 gameHandler;

    public Controller(Game game){
        this.game = game;
        this.gameHandler = new GameHandler2(game);
    }

    @Override
    public void update(Message message) {
        try {
            gameHandler.execute(message);
        }catch(Exception e){
            game.sendErrorNote(message.getSender(), e.toString(), game.getCurrentPlayer());
        }
    }
}