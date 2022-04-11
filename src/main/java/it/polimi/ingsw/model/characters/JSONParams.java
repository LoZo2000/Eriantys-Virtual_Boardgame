package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;

import java.util.Set;

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

    public JSONParams(Action typeAction, Integer numThingOnIt, Location locationType, Boolean refill, Set<Location> allowedDepartures, Set<Location> allowedArrivals, Boolean towersDisabled, Integer extraPoints, Integer extraMNMovement) {
        this.typeAction = typeAction;
        this.numThingsOnIt = numThingOnIt;
        this.locationType = locationType;
        this.refill = refill;
        this.allowedDepartures = allowedDepartures;
        this.allowedArrivals = allowedArrivals;
        this.towersDisabled = towersDisabled;
        this.extraPoints = extraPoints;
        this.extraMNMovement = extraMNMovement;
    }

    public Action getTypeAction() {
        return typeAction;
    }

    public Integer getNumThingOnIt() {
        return numThingsOnIt;
    }

    public Location getLocationType() {
        return locationType;
    }

    public Boolean getRefill() {
        return refill;
    }

    public Set<Location> getAllowedDepartures() {
        return allowedDepartures;
    }

    public Set<Location> getAllowedArrivals() {
        return allowedArrivals;
    }

    public Boolean getTowersDisabled() {
        return towersDisabled;
    }

    public Integer getExtraInfluencePoints() {
        return extraPoints;
    }

    public Integer getExtraMNMovement() {
        return extraMNMovement;
    }
}
