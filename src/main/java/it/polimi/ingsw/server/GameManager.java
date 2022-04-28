package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.view.RemoteView;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<Connection> addedConnections = new ArrayList<>();
    private int[] numPlayers = {0,0,0,0,0,0};
    static private int[] maxPlayers = {2,3,4,2,3,4}; //x6 different kinds of matches, according to number of players and rules (yes or no)
    private Controller[] controllerTypes = new Controller[6];
    private Model[] modelTypes = new Model[6];

    public void joinLobby(Connection c, Message message){
        int additive=0; // additive became 3 if the rules are complete for the index in the array
        boolean completeRules = message.getCompleteRules();
        if(completeRules) additive=3;
        int i=message.getNumPlayers()-2+additive;
        if(numPlayers[i]==0){
            modelTypes[i] = new Model();
            controllerTypes[i] = new Controller(modelTypes[i]);
            RemoteView remoteView = new RemoteView(c, message.getSender());
            remoteView.addObserver(controllerTypes[i]);
            modelTypes[i].addObserver(remoteView);
            numPlayers[i]++;
        }
        else{
            RemoteView remoteView = new RemoteView(c, message.getSender());
            remoteView.addObserver(controllerTypes[i]);
            modelTypes[i].addObserver(remoteView);
            numPlayers[i]++;
            if(numPlayers[i]==maxPlayers[i]) numPlayers[i]=0;
        }
    }
}