package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.*;

/**
 * The class UsePowerMessage represent the action that use a power of on of the character card, extends
 * the class Message and implement the method execute that apply changes to the game
 */
public class UsePowerMessage extends Message{
    private final int characterCard;

    /**
     * This method is the constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param characterCard is the characcter card chosen
     */
    public UsePowerMessage(String sender, int characterCard) {
        super(sender, Action.USEPOWER);
        this.characterCard = characterCard;
    }

    /**
     *  The method execute is an override of the class Message, apply all the changes needed to the model through the game to use
     *  the power of one of the character card in the dashboard
     * @param game is the object that represent the game and is used to modify the model
     * @return an Update object to communicate the state of the game to the controller
     * @throws IllegalMoveException is the exception thrown if the action is not permitted
     * @throws NoPlayerException is the exception thrown if a player isn't found
     * @throws NotEnoughMoneyException is the exception thrown if the player doesn't have enough money to activate the character card
     * @throws NoCharacterSelectedException is the exception thrown if there isn't a character selected to use its power
     */
    @Override
    public Update execute(Game game) throws IllegalMoveException, NoPlayerException, NotEnoughMoneyException, NoCharacterSelectedException {
        if (game.getUsedCard())
            throw new IllegalMoveException("You can't use characters' powers twice in the same turn");
        Player p = game.getPlayer(sender);
        Boolean activeCard = game.usePower(p, characterCard);

        game.setUsedCard(true);

        if(game.canExchange()){
            game.resetRemainingExchanges();
        }

        if(!activeCard)
            activeCard = null;

        return new Update(null, null, null, null, activeCard, null, game.getFinishedGame(), game.getWinner(),game.getIsLastTurn());
    }

    /**
     * This method return the character card chosen
     * @return an int representing the character card chosen
     */
    public int getCharacterCard(){
        return characterCard;
    }
}
