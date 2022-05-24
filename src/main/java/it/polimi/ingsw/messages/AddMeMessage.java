package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Game;

public class AddMeMessage extends Message{
    final private boolean completeRules;
    final private int maxPlayers;

    public AddMeMessage(String sender, boolean completeRules, int maxPlayers){
        super(sender, Action.ADDME);
        this.completeRules = completeRules;
        this.maxPlayers = maxPlayers;
    }

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

    public boolean getCompleteRules(){
        return completeRules;
    }
    public int getNumPlayers(){
        return maxPlayers;
    }

}
