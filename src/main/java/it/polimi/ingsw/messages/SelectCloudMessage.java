package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;

import java.util.Arrays;

/**
 * The class SelectCloudMessage represent the action that select a cloud and give to the player the students in that cloud, extends
 * the class Message and implement the method execute that apply changes to the game
 */
public class SelectCloudMessage extends  Message{
    final private int cloudPosition;

    /**
     * This method is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param cloudPosition is the position of the cloud chosen
     */
    public SelectCloudMessage(String sender, int cloudPosition){
        super(sender, Action.SELECTCLOUD);
        this.cloudPosition = cloudPosition;
    }

    /**
     * The method execute is an override of the class Message, apply all the changes needed to the model through the game to choose a
     * cloud
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws IllegalMoveException is the exception thrown if the action is not permitted
     * @throws NoPlayerException is the exception thrown if a player isn't found
     */
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

        return new Update(null, null, null, numberFullClouds, null, null, game.getFinishedGame(), game.getIsLastTurn());
    }

    /**
     * This method return the position of the cloud
     * @return an int representing the position of the cloud
     */
    public int getCloudPosition(){
        return cloudPosition;
    }
}