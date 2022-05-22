package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;

import java.util.Set;

public class JSONCharacter {
    private final int id;
    private final CharacterType typeCharacter;
    private final String desc;
    private final int cost;
    private final JSONParams params;

    public JSONCharacter(int id, CharacterType typeCharacter, String desc, int cost, Action typeAction, Integer numThingOnIt, Location locationType, Boolean refill, Set<Location> allowedDepartures, Set<Location> allowedArrivals, Boolean towersDisabled, Integer extraInfluencePoints, Integer extraMNMovement, Integer maxMoves) {
        this.id = id;
        this.typeCharacter = typeCharacter;
        this.desc = desc;
        this.cost = cost;
        this.params = new JSONParams(typeAction, numThingOnIt, locationType, refill, allowedDepartures, allowedArrivals, towersDisabled, extraInfluencePoints, extraMNMovement, maxMoves);
    }

    public int getId() {
        return id;
    }

    public CharacterType getTypeCharacter() {
        return typeCharacter;
    }

    public String getDesc() {
        return desc;
    }

    public int getCost() {
        return cost;
    }

    public JSONParams getParams(){
        return this.params;
    }
}
