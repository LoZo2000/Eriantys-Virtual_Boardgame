package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;

import java.util.Set;


/**
 * This class represent a character and is used to store the characters attributes after being extracted from the JSON.
 * This class store the information that has each character, like the id or the type, and a reference to JSONParams object
 * that stores the information relative to a particular type of character.
 */
public class JSONCharacter {
    private final int id;
    private final CharacterType typeCharacter;
    private final String desc;
    private final String desc_short;
    private final int cost;
    private final JSONParams params;
    /**
     * This method is the constructor of the class.
     * This constructor is used by the GSON library to generate a Java Object from the file "characters.json".
     * These parameters can be null if the Character in JSON file hasn't a particular parameter.
     * @param id is the id of the Character
     * @param typeCharacter is the type of the Character
     * @param desc is the description of the character
     * @param desc_short is the short version of the description of the character
     * @param cost is the cost to use the power of the character
     * @param typeAction is the type of the action of the character, if it's an Action or Movement character. It can be null
     * @param numThingOnIt is the number of things on the character card. It can be null if the character isn't a Action character or a Movement character
     * @param locationType is the type of the location enabled by some characters card for some actions. It can be null if the character isn't a Movement character
     * @param refill is a boolean that report if the card need to draw a student from the Bag after one of them is taken by a player. It can be null if the character isn't a Movement character
     * @param allowedDepartures are the locations permitted to be a departure point for a movement. This parameter isn't null only if the character type is Movement
     * @param allowedArrivals are the locations permitted to be an arrival point for a movement. This parameter isn't null only if the character type is Movement
     * @param towersDisabled is a boolean reporting if the tower are disabled. This parameter is present only if the character is an Influence one
     * @param extraInfluencePoints ia an int containing the extra points given by some character cards. This parameter is present only if the character is an Influence one
     * @param extraMNMovement is an int containing the amount of extra movement permitted to mother nature by some character cards. This parameter is present only if the character is a MotherNature one
     * @param maxMoves is an int reporting the number of max moves possible. It's used only in Exchange character
     */
    public JSONCharacter(int id, CharacterType typeCharacter, String desc, String desc_short, int cost, Action typeAction, Integer numThingOnIt, Location locationType, Boolean refill, Set<Location> allowedDepartures, Set<Location> allowedArrivals, Boolean towersDisabled, Integer extraInfluencePoints, Integer extraMNMovement, Integer maxMoves) {
        this.id = id;
        this.typeCharacter = typeCharacter;
        this.desc = desc;
        this.desc_short = desc_short;
        this.cost = cost;
        this.params = new JSONParams(typeAction, numThingOnIt, locationType, refill, allowedDepartures, allowedArrivals, towersDisabled, extraInfluencePoints, extraMNMovement, maxMoves);
    }

    /**
     * This method return the id of the character
     * @return an int representing the id of the character
     */
    public int getId() {
        return id;
    }

    /**
     * This method return the character type
     * @return an enum representing the character type of the character
     */
    public CharacterType getTypeCharacter() {
        return typeCharacter;
    }

    /**
     * This method return the description of the character
     * @return a string that contain the description of the character
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method return the short description of the character
     * @return a string that contain the short description of the character
     */
    public String getDesc_short() {
        return desc_short;
    }

    /**
     * This method return the current cost of the character
     * @return an int representing the current cost of the character
     */
    public int getCost() {
        return cost;
    }

    /**
     * This method return the parameters of the character card
     * @return a JSONParams object containing the parameters of the character card
     */
    public JSONParams getParams(){
        return this.params;
    }
}
