package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameHandler2;
import it.polimi.ingsw.messages.GameStatus;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.Observable;

public class Model extends Observable<GameStatus>{
    GameHandler2 gameHandler;

    public Model(){
        gameHandler = new GameHandler2();
    }

    public void setPlayerChoice(Message message) throws Exception{
        gameHandler.execute(message);
        //If the move is correct, the model is changed: therefore send an update to everybody
        GameStatus GStoSender = gameHandler.getGameStatus(message.getSender());
        notify(GStoSender);
        for(String enemy : GStoSender.getEnemiesNames()) notify(gameHandler.getGameStatus(enemy));
    }

    public void sendErrorNote(String mistaker, String error, String currentPlayer){
        notify(new GameStatus(mistaker, error, currentPlayer));
    }

    public Game getGame() {
        return gameHandler.getGame();
    }
}