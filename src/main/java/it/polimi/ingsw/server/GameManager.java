package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.view.RemoteView;
import java.util.ArrayList;
import java.util.List;

/**
 * The class GameManager manages the games of the server, its role is to associate all the classes of the Observer pattern
 * and to create the game with specific number of players and rules and add players to them
 */
public class GameManager {
    private List<Controller> activeGames = new ArrayList<>();
    private int[] numPlayers = {0,0,0,0,0,0};
    static private int[] maxPlayers = {2,3,4,2,3,4}; //x6 different kinds of matches, according to number of players and rules (yes or no)
    private Controller[] controllerTypes = new Controller[6];
    private Game[] gameTypes = new Game[6];

    /**
     * The method JoinLobby take the connection and the AddMeMessage sent by a player and add the player to the right game
     * @param c is the connection of the player that want to join a game
     * @param message is the AddMeMessage sent by a player
     */
    public void joinLobby(Connection c, AddMeMessage message){
        int additive=0; // additive became 3 if the rules are complete for the index in the array
        boolean completeRules = message.getCompleteRules();
        if(completeRules) additive=3;
        int i=message.getNumPlayers()-2+additive;
        if(numPlayers[i]==0){
            gameTypes[i] = new Game(completeRules, message.getNumPlayers());
            controllerTypes[i] = new Controller(gameTypes[i], this);
            activeGames.add(controllerTypes[i]);
            RemoteView remoteView = new RemoteView(c, message.getSender());
            remoteView.addObserver(controllerTypes[i]);
            gameTypes[i].addObserver(remoteView);
            numPlayers[i]++;
        }
        else{
            RemoteView remoteView = new RemoteView(c, message.getSender());
            remoteView.addObserver(controllerTypes[i]);
            gameTypes[i].addObserver(remoteView);
            numPlayers[i]++;
            if(numPlayers[i]==maxPlayers[i]) {
                numPlayers[i]=0;
            }
        }
    }

    /**
     * This method search a game given the nickname of one of the player joined in that game
     * @param nickname is the nickname of the player inside the game that must be searched
     * @return the Controller object of the game
     */
    public Controller searchGameByNickname(String nickname){
        for(Controller c : activeGames){
            if(c.getNicknames().contains(nickname))
                return c;
        }
        return null;
    }

    /**
     * This method remove a game that finished
     * @param controller is the controller to remove from the list of active games
     * @param resetPlayers is a boolean to decide if reset the players
     */
    public void removeFinishedGame(Controller controller, boolean resetPlayers){
        this.activeGames.remove(controller);
        if(resetPlayers){
            int additive = 0;
            if(controller.getCompleteRules())
                additive=3;
            int i = controller.getNumPlayers()-2+additive;
            numPlayers[i]=0;
        }
    }
}