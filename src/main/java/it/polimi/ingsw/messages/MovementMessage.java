package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Movable;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.characters.CharacterType;
import it.polimi.ingsw.model.characters.MovementCharacter;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;
import it.polimi.ingsw.model.exceptions.NoIslandException;
import it.polimi.ingsw.model.exceptions.NoPlayerException;

import java.util.Set;

public abstract class MovementMessage extends Message{
    final protected Location departureType;
    final protected int departureId;
    final protected Location arrivalType;
    final protected int arrivalId;

    protected MovementMessage(String sender, Action action, Location departureType, int departureId, Location arrivalType, int arrivalId) {
        super(sender, action);
        this.departureType = departureType;
        this.departureId = departureId;
        this.arrivalType = arrivalType;
        this.arrivalId = arrivalId;
    }

    protected Movable getLocation(Game game, Location type, int id) throws NoPlayerException, NoIslandException {
        return switch (type) {
            case ENTRANCE -> game.getPlayer(this.sender).getDashboard().getEntrance();
            case CANTEEN -> game.getPlayer(this.sender).getDashboard().getCanteen();
            case ISLAND -> game.getIsland(id);
            case CARD_ISLAND -> getMovementCharacterCard(game, Location.CARD_ISLAND);
            case CARD_CANTEEN -> getMovementCharacterCard(game, Location.CARD_CANTEEN);
            case CARD_EXCHANGE -> getMovementCharacterCard(game, Location.CARD_EXCHANGE);
        };
    }

    protected boolean checkErrorMovementLocations(Game game) throws NoActiveCardException {
        Set<Location> allowedLocations = game.getAllowedDepartures();
        if(!allowedLocations.contains(departureType))
            return true;

        allowedLocations = game.getAllowedArrivals();
        if(!(allowedLocations.contains(arrivalType)))
            return true;

        if (arrivalType == departureType)
            return true;

        return false;
    }

    protected MovementCharacter getMovementCharacterCard(Game game, Location typeCard){
        Character[] characters = game.getCharactersCards();
        for(Character ch : characters){
            if(ch.getTypeCharacter() == CharacterType.MOVEMENT){
                MovementCharacter mc = (MovementCharacter) ch;
                if(mc.getLocationType() == typeCard)
                    return mc;
            }
        }
        //This line can't be covered
        return null;
    }
}
