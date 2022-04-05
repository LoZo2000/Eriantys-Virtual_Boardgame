package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;
import it.polimi.ingsw.model.exceptions.NoCharacterSelectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class GameHandler {
    private ArrayList<String> players;
    private int[] priority = {0,0,0,0};
    private ArrayList<String> orderPlayers;
    private int maxPlayers = 0;
    private int numPlayers = 0;
    private int currentPlayer = -1;
    private int firstPlayer = -1;
    private int maxMoveStudent = 3;
    private int actualMoveStudent = 0;
    private boolean completeRules = false;

    private Action currentPhase = Action.ADDME;
    private Action oldPhase;

    private Translator translator;



    public GameHandler(){ //First instruction of all
        players = new ArrayList<>();
        orderPlayers = new ArrayList<>();
    }


    //IOException because it can be thrown by Game if the characters.json file isn't in the FileSystem
    public void execute(Message message) throws IOException, IllegalMoveException, NotYourTurnException, UnrecognizedPlayerOrActionException, CannotJoinException, EndGameException, NoActiveCardException, NoCharacterSelectedException {
        switch (message.getAction()){

            case ADDME:
                if(numPlayers==0){
                    players.add(message.getSender());
                    if(message.getCompleteRules()) completeRules = true;
                    maxPlayers = message.getNumPlayers();
                    if(maxPlayers==3) maxMoveStudent = 4;
                    numPlayers = 1;
                    firstPlayer = 0;
                    translator = new Translator(completeRules, maxPlayers);
                    translator.translateThis(message);
                }
                else if(numPlayers==maxPlayers) throw new CannotJoinException("The room is already full!");
                else if(completeRules!=message.getCompleteRules() || maxPlayers!=message.getNumPlayers()) throw new CannotJoinException("This match doesn't fit your requirements");
                else if(players.contains(message.getSender())) throw new CannotJoinException("Nickname already taken!");
                else{
                    players.add(message.getSender());
                    orderPlayers.add(message.getSender());
                    numPlayers++;
                    translator.translateThis(message);
                    if(numPlayers==maxPlayers){
                        currentPhase = Action.PLAYCARD;
                        currentPlayer = 0;
                        firstPlayer = 0;
                    }
                }
                break;

            case PLAYCARD:
                if(!message.getSender().equals(players.get(currentPlayer))) throw new NotYourTurnException("This is not your turn");
                else if(currentPhase!=Action.PLAYCARD) throw new IllegalMoveException("Wrong move: you should not play a card now!");
                else{
                    priority[currentPlayer] = message.getPriority();
                    translator.translateThis(message);
                    currentPlayer = (currentPlayer+1)%maxPlayers;
                    if(currentPlayer==firstPlayer){
                        orderPlayers = sort();
                        firstPlayer = players.indexOf(orderPlayers.get(0));
                        actualMoveStudent = 0;
                        currentPhase = Action.MOVESTUDENT;
                        //TODO: remember to disable the previously active rule!!!
                    }
                }
                break;

            case MOVESTUDENT:
                if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
                else if(currentPhase!=Action.MOVESTUDENT) throw new IllegalMoveException("Wrong move: you should not move students now!");
                else if(message.getDepartureType()!=Location.ENTRANCE || (message.getArrivalType()!=Location.ISLAND && message.getArrivalType()!=Location.CANTEEN) ) throw new IllegalMoveException("Illegal movement!");
                else{
                    translator.translateThis(message);
                    actualMoveStudent++;
                    if(actualMoveStudent==maxMoveStudent){
                        currentPhase = Action.MOVEMOTHERNATURE;
                        actualMoveStudent = 0;
                    }
                }
                break;

            case USEPOWER:
                if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
                if(currentPhase!=Action.MOVESTUDENT && currentPhase!=Action.MOVEMOTHERNATURE) throw new IllegalMoveException("Wrong move: can't activate powers now!");

                boolean isActive = translator.translateThis(message);

                if(isActive){
                    this.oldPhase = this.currentPhase;
                    this.currentPhase = Action.ACTIVECARD;
                }

                break;

            case EXCHANGESTUDENT:
                if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
                if(currentPhase != Action.ACTIVECARD)
                    throw new IllegalMoveException("Wrong move: you have to use the effect on the active card!");

                Action requestedActiveAction = translator.getRequestedAction();
                if(requestedActiveAction != Action.EXCHANGESTUDENT)
                    throw new IllegalMoveException("Wrong move: you should not exchange students now!");

                if(!checkMovementLocations(message))
                    throw new IllegalMoveException("You can't move students from these locations");

                translator.translateThis(message);
                try{
                    translator.getRequestedAction();
                } catch(Exception e){
                    this.currentPhase = this.oldPhase;
                }

                break;

            case MOVEMOTHERNATURE:
                if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
                else if(currentPhase!=Action.MOVEMOTHERNATURE) throw new IllegalMoveException("Wrong move: you should not move Mother Nature now!");
                else{
                    translator.translateThis(message);
                    currentPhase = Action.SELECTCLOUD;
                }
                break;

            case SELECTCLOUD:
                if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
                else if(currentPhase!=Action.SELECTCLOUD) throw new IllegalMoveException("Wrong move: you should not select a cloud now!");
                else{
                    translator.translateThis(message);
                    orderPlayers.remove(0);
                    if(orderPlayers.size()>0){
                        currentPhase = Action.MOVESTUDENT;
                    }
                    else{
                        currentPhase = Action.PLAYCARD;
                        currentPlayer = firstPlayer;
                        translator.endTurn();
                    }
                }
                break;

            case SHOWME:
                if(numPlayers==0 || numPlayers!=maxPlayers) throw new IllegalMoveException("Wait until all the players join the match...");
                else translator.translateThis(message);
                break;

            default:
                throw new UnrecognizedPlayerOrActionException("This kind of action doesn't exist!");
        }
    }



    private ArrayList<String> sort(){
        ArrayList<String> newOrder = new ArrayList<>();
        for(int i=0; i<maxPlayers; i++){
            int min=0;
            for(int j=1; j<maxPlayers; j++) if(priority[j]<priority[min]) min=j;
            newOrder.add(players.get(min));
            priority[min]=11;
        }
        return newOrder;
    }

    private boolean checkMovementLocations(Message m){
        try {
            Set<Location> allowedLocations = this.translator.getAllowedDepartures();
            if(!allowedLocations.contains(m.getDepartureType()))
                return false;

            allowedLocations = this.translator.getAllowedArrivals();
            if(!(allowedLocations.contains(m.getArrivalType())))
                return false;

            if(m.getArrivalType() == m.getDepartureType())
                return false;

        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
