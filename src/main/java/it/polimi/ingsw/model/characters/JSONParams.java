package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;

import java.util.Set;

/**
 * This class is used to store the parameters of the different characters cards taken from a JSON file
 */
public class JSONParams {
    private final Action typeAction;
    private final Integer numThingsOnIt;
    private final Location locationType;
    private final Boolean refill;

    private final Set<Location> allowedDepartures;
    private final Set<Location> allowedArrivals;

    private final Boolean towersDisabled;
    private final Integer extraPoints;
    private final Integer extraMNMovement;
    private final Integer maxMoves;
    //Todo Gabriele controlla che non sono sicuro
    /**
     * This method is the constructor of the class
     * @param typeAction is the type of Action the card could enable
     * @param numThingOnIt is the number of things is possible to put over a card
     * @param locationType is the type of the location enabled by some characters card for some actions
     * @param refill is a boolean that report if the card need a refill or not
     * @param allowedDepartures are the locations permitted to be a departure point for a movement
     * @param allowedArrivals are the locations permitted to be an arrival point for a movement
     * @param towersDisabled is a boolean reporting if the tower are disabled
     * @param extraPoints ia an int containing the extra points given by some character cards
     * @param extraMNMovement is an int containing the amount of extra movement permitted to mother nature by some character cards
     * @param maxMoves is an int reporting the number of max moves possible
     */
    public JSONParams(Action typeAction, Integer numThingOnIt, Location locationType, Boolean refill, Set<Location> allowedDepartures, Set<Location> allowedArrivals, Boolean towersDisabled, Integer extraPoints, Integer extraMNMovement, Integer maxMoves) {
        this.typeAction = typeAction;
        this.numThingsOnIt = numThingOnIt;
        this.locationType = locationType;
        this.refill = refill;
        this.allowedDepartures = allowedDepartures;
        this.allowedArrivals = allowedArrivals;
        this.towersDisabled = towersDisabled;
        this.extraPoints = extraPoints;
        this.extraMNMovement = extraMNMovement;
        this.maxMoves = maxMoves;
    }

    /**
     * This method return the Action type of the class
     * @return an enum Action representing the Action type of the class
     */
    public Action getTypeAction() {
        return typeAction;
    }

    /**
     * This method return the number of things on the card
     * @return an Integer representing the number of things over the card
     */
    public Integer getNumThingOnIt() {
        return numThingsOnIt;
    }

    /**
     * This method return the location type of the class
     * @return an enum Location representing the location type of the class
     */
    public Location getLocationType() {
        return locationType;
    }

    /**
     * This method report if the card need a refill
     * @return a boolean reporting if the card need a refill
     */
    public Boolean getRefill() {
        return refill;
    }

    /**
     * This method return the locations allowed to be a departure point
     * @return a set of locations allowed to be a departure point
     */
    public Set<Location> getAllowedDepartures() {
        return allowedDepartures;
    }

    /**
     * This method return the locations allowed to be an arrival point
     * @return a set of locations allowed to be an arrival point
     */
    public Set<Location> getAllowedArrivals() {
        return allowedArrivals;
    }

    /**
     * This method report if the towers are disabled
     * @return a boolean reporting if the towers are disabled
     */
    public Boolean getTowersDisabled() {
        return towersDisabled;
    }

    /**
     * This method return the amount of extra influence point given by the character card
     * @return an Integer representing the amount of extra influence point given by the character card
     */
    public Integer getExtraInfluencePoints() {
        return extraPoints;
    }

    /**
     * This method return the amount of extra movement allowed to mother nature
     * @return an Integer representing the amount of extra movement allowed to mother nature
     */
    public Integer getExtraMNMovement() {
        return extraMNMovement;
    }

    /**
     * this method return the number of max moves allowed by the character card
     * @return an integer representing the number of max moves allowed by the character card
     */
    public Integer getMaxMoves(){
        return maxMoves;
    }
}
