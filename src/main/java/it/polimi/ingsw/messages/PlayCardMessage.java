package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.AlreadyPlayedCardException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NoPlayerException;
import it.polimi.ingsw.model.exceptions.OverflowCardException;

public class PlayCardMessage extends Message{
    final private int priority;

    public PlayCardMessage(String sender, int priority){
        super(sender, Action.PLAYCARD);
        this.priority = priority;
    }

    @Override
    public Update execute(Game game) throws IllegalMoveException, NoPlayerException {
        try {
            Player p = game.getPlayer(sender);
            game.playCard(p, priority);
        } catch(OverflowCardException e){
            throw new IllegalMoveException("There is no such a card");
        } catch(AlreadyPlayedCardException e){
            throw new IllegalMoveException("Another player has already played that card!");
        }

        return new Update(null, true, null, null, null, null);
    }

    public int getPriority(){
        return priority;
    }
}
