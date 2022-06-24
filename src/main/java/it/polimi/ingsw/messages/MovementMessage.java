package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Movable;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.characters.CharacterType;
import it.polimi.ingsw.model.characters.MovementCharacter;
import it.polimi.ingsw.model.exceptions.*;

import java.util.Set;



/**
 * The class MovementMessage is an abstract class that owns some methods used in the messages where there are movement or exchanges
 * of the pawns
 */
public abstract class MovementMessage extends Message{
    final protected Location departureType;
    final protected int departureId;
    final protected Location arrivalType;
    final protected int arrivalId;

    /**
     * The constructor of the class
     * @param sender is the sender of the Message, is a String that contain the nickname of the player
     * @param action is the action that will be performed
     * @param departureType is the type of the departure location
     * @param departureId is the id of the departure location
     * @param arrivalType  is the type of the arrival location
     * @param arrivalId is the id of the arrival location
     */
    protected MovementMessage(String sender, Action action, Location departureType, int departureId, Location arrivalType, int arrivalId) {
        super(sender, action);
        this.departureType = departureType;
        this.departureId = departureId;
        this.arrivalType = arrivalType;
        this.arrivalId = arrivalId;
    }

    /**
     * This method return a location
     * @param game is the object that represent the game and is used to modify the model
     * @param type is the type of the location to get
     * @param id is the id of the location to get
     * @return a Movable that represent the location
     * @throws NoPlayerException is the exception thrown if a player isn't found
     * @throws NoIslandException is the exception thrown if an island with a specific id isn't found
     */
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

    /**
     * This method check if the movement is allowed
     * @param game is the object that represent the game and is used to modify the model
     * @return a boolean that report if the movement is allowed
     * @throws NoActiveCardException is the exception thrown if there is no character card active
     */
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

    /**
     * This method return a Character that permit movement
     * @param game is the object that represent the game and is used to modify the model
     * @param typeCard is an enum representing the type of the Card
     * @return a Movement Character (a Character that permit movement)
     */
    protected MovementCharacter getMovementCharacterCard(Game game, Location typeCard){
        Character[] characters = game.getCharactersCards();
        for(Character ch : characters){
            if(ch.getTypeCharacter() == CharacterType.MOVEMENT || ch.getId()==7){//ch.getTypeCharacter() == CharacterType.EXCHANGE){
                MovementCharacter mc = (MovementCharacter) ch;
                if(mc.getLocationType() == typeCard)
                    return mc;
            }
        }
        //This line can't be covered
        return null;
    }
}