package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NoPlayerException;

import java.util.Arrays;

public class SelectCloudMessage extends  Message{
    final private int cloudPosition;

    public SelectCloudMessage(String sender, int cloudPosition){
        super(sender, Action.SELECTCLOUD);
        this.cloudPosition = cloudPosition;
    }

    @Override
    public Update execute(Game game) throws IllegalMoveException, NoPlayerException {
        Cloud[] clouds = game.getAllClouds();
        if(cloudPosition < 0 || cloudPosition > game.getNumPlayers()){
            throw new IllegalMoveException("There isn't any cloud with that index!");
        }
        if(!clouds[cloudPosition].isFull()){
            throw new IllegalMoveException("This cloud has been already selected by another player");
        }

        game.selectCloud(sender, clouds[cloudPosition]);

        int numberFullClouds = (int) Arrays.stream(clouds).filter((Cloud::isFull)).count();

        return new Update(null, null, null, numberFullClouds, null, null);
    }

    public int getCloudPosition(){
        return cloudPosition;
    }
}