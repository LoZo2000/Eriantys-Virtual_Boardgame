package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.*;

public class UsePowerMessage extends Message{
    private final int characterCard;

    public UsePowerMessage(String sender, int characterCard) {
        super(sender, Action.USEPOWER);
        this.characterCard = characterCard;
    }

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

    public int getCharacterCard(){
        return characterCard;
    }
}
