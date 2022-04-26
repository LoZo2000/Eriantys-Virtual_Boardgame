package it.polimi.ingsw.server;

import java.util.*;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameHandler2;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalMessageException;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.messages.*;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<Connection> addedConnections = new ArrayList<>();
    private GameHandler2[] gameTypes = new GameHandler2[6];
    Hashtable<Connection, GameHandler2> games = new Hashtable<>();



    public synchronized void manageMessage(Connection c, Message message) throws IllegalActionException{
        System.out.println("il tipo di message è"+message.getAction());
        if(message.getAction()== Action.ADDME){
            System.out.println("sono qui");
            if(addedConnections.contains(c)==true) throw new IllegalActionException();
            try {
                manageADD(c, message);
            }catch(Exception e){
                e.printStackTrace();
            }
            showModel(c);
        }
        else{
            if(addedConnections.contains(c)==false) throw new IllegalActionException();
            try {
                games.get(c).execute(message);
            }catch (EndGameException ex){
                showModel(c);
                endGame(c);
                return;
            }catch(Exception e){
                e.printStackTrace();
            }
            showModel(c);
        }
    }

    private void manageADD(Connection c, Message message) throws NoPlayerException, NoIslandException, IllegalMessageException, NoActiveCardException, IllegalActionException, NotYourTurnException, IllegalMoveException, UnrecognizedPlayerOrActionException, CannotJoinException, NoCharacterSelectedException, EndGameException, NotEnoughMoneyException, NoMoreTokensException, NoSuchStudentException {
        int additive=0; // additive became 3 if the rules are complete for the index in the array
        boolean completeRules = message.getCompleteRules();
        if(completeRules) additive=3;
        int numPlayers = message.getNumPlayers();
        int i=numPlayers-2+additive;
        if(gameTypes[i]==null){
            Message createGame = new CreateMatchMessage("gameMaker", Action.CREATEMATCH, completeRules, numPlayers );
            gameTypes[i] = new GameHandler2(createGame);
            gameTypes[i].execute(message);
            addedConnections.add(c);
            games.put(c, gameTypes[i]);
        }
        else{
            gameTypes[i].execute(message);
            addedConnections.add(c);
            games.put(c, gameTypes[i]);
            if(gameTypes[i].getActivePlayers()==numPlayers){
                gameTypes[i]=null;
            }
        }
    }

    private void showModel(Connection c) {
        System.out.println("Sending something through "+c);
        GameHandler2 temp=games.get(c);
        for(Connection a : addedConnections){
            if(games.get(a).equals(temp)) a.send(games.get(a).getGame());
        }
    }

    private void endGame(Connection c){
        GameHandler2 temp=games.get(c);
        for(Connection a : addedConnections){
            if(games.get(a).equals(temp)) a.closeConnection();
        }
    }



}
