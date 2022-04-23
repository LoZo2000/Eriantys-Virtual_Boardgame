package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;
import it.polimi.ingsw.model.exceptions.NoCharacterSelectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class GameHandler {
    private ArrayList<String> players;          //List of player in the game (it's "final": after all the player have joined, this list will not be modified). It is necessary to remember the order of the players during the PLAYCARD phase
    private int[] priority = {0,0,0,0};         //Priority of the card played by the player with the same index in "players"
    private int[] maxMTmovement = {0,0,0,0};    //Movement of the card played by the player with the same index in "players"
    private ArrayList<String> orderPlayers;     //This list will be created every turn after the players have played their cards. The game will accept only moves made by the player in position 0. After he/she has selected a cloud, the player in postion 0 is removed
    private int maxPlayers = 0;                 //Number of players expected to join the match (choosen by the first player)
    private int numPlayers = 0;                 //Number of player currently enrolled in our match
    private int currentPlayer = -1;             //Player who is expected to play now
    private int firstPlayer = -1;               //Player who played the card with the lowest priority (who will have to play a card by first the next turn)
    private int maxMoveStudent = 3;             //Number of students a player is allowed to move every turn
    private int actualMoveStudent = 0;          //Number of students the current player has already moved in this turn
    private boolean completeRules = false;

    private Action oldPhase;
    private Action currentPhase = Action.CREATEMATCH; //First phase is ADDME: we wait for some players to join

    private Translator translator;



    public GameHandler(){ //First instruction of all
        players = new ArrayList<>();
        orderPlayers = new ArrayList<>();
    }


    public void execute(Message message) throws IllegalMoveException, NotYourTurnException, UnrecognizedPlayerOrActionException, CannotJoinException, EndGameException, NoActiveCardException, NoCharacterSelectedException, NotEnoughMoneyException, NoMoreTokensException {
        System.out.println("Message to exe: "+message.getAction());
        switch (message.getAction()){

            case CREATEMATCH:   //The first player creates the game
                players.add(message.getSender());
                if(message.getCompleteRules()) completeRules = true;
                maxPlayers = message.getNumPlayers();
                if(maxPlayers==3) maxMoveStudent = 4;
                numPlayers = 1;
                firstPlayer = 0;
                translator = new Translator(completeRules, maxPlayers);
                translator.translateThis(message);
                currentPhase = Action.ADDME;
                break;

            case ADDME:     //To add a new player
                if(numPlayers==maxPlayers) throw new CannotJoinException("The room is already full!");
                else if(players.contains(message.getSender())) throw new CannotJoinException("Nickname already taken!");
                else{
                    players.add(message.getSender());
                    orderPlayers.add(message.getSender());
                    numPlayers++;
                    translator.translateThis(message);
                    if(numPlayers==maxPlayers){     //If it is ture, it means that all the players have joined: time to change phase
                        currentPhase = Action.PLAYCARD;
                        //translator.getGame().setCurrentAction(Action.PLAYCARD);
                        currentPlayer = 0;
                        translator.getGame().setCurrentPlayer(players.get(0));
                        firstPlayer = 0;
                    }
                }
                break;

            case PLAYCARD:
                if(!message.getSender().equals(players.get(currentPlayer))) throw new NotYourTurnException("This is not your turn");
                else if(currentPhase!=Action.PLAYCARD) throw new IllegalMoveException("Wrong move: you should not play a card now!");
                else{
                    priority[currentPlayer] = message.getPriority();
                    maxMTmovement[currentPlayer] = (message.getPriority()+1)/2;
                    translator.translateThis(message);
                    currentPlayer = (currentPlayer+1)%maxPlayers;
                    translator.getGame().setCurrentPlayer(players.get(currentPlayer));
                    if(currentPlayer==firstPlayer){     //If it is true, this means that all the players have already played their cards: time to change phase
                        orderPlayers = sort();          //New order according to the cards played
                        firstPlayer = players.indexOf(orderPlayers.get(0));
                        actualMoveStudent = 0;
                        currentPhase = Action.MOVESTUDENT;
                        //translator.getGame().setCurrentAction(Action.MOVESTUDENT);
                        translator.getGame().setCurrentPlayer(orderPlayers.get(0));
                        //TODO: remember to disable the previously active rule!!!
                    }
                }
                break;

            case MOVESTUDENT:
                if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
                else if(currentPhase!=Action.MOVESTUDENT) throw new IllegalMoveException("Wrong move: you should not move students now!");
                else if(message.getDepartureType() != Location.ENTRANCE || (message.getArrivalType()!=Location.ISLAND && message.getArrivalType()!=Location.CANTEEN) ) throw new IllegalMoveException("Illegal movement!");
                else{
                    translator.translateThis(message);
                    actualMoveStudent++;
                    if(actualMoveStudent==maxMoveStudent){      //After 3 or 4 students have been moved, it is time to move MotherNature
                        currentPhase = Action.MOVEMOTHERNATURE;
                        //translator.getGame().setCurrentAction(Action.MOVEMOTHERNATURE);
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
                    int index=0;
                    for(int i=0; i<players.size(); i++) if(players.get(i).equals(message.getSender())) index=i;
                    if(message.getMovement()<1 || message.getMovement()>maxMTmovement[index]) throw new IllegalMoveException("You cannot move Mother Nature so far");
                    translator.translateThis(message);
                    currentPhase = Action.SELECTCLOUD;
                    //translator.getGame().setCurrentAction(Action.SELECTCLOUD);
                }
                break;

            case SELECTCLOUD:
                if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
                else if(currentPhase!=Action.SELECTCLOUD) throw new IllegalMoveException("Wrong move: you should not select a cloud now!");
                else{
                    translator.translateThis(message);
                    orderPlayers.remove(0);
                    if(orderPlayers.size()>0) {     //It is time for the following player to move students!
                        currentPhase = Action.MOVESTUDENT;
                        //translator.getGame().setCurrentAction(Action.MOVESTUDENT);
                        translator.getGame().setCurrentPlayer(orderPlayers.get(0));
                    }
                    else{                           //Else, if orderPlayers is empty: all the players have played their turns: it is time to play a card!
                        currentPhase = Action.PLAYCARD;
                        //translator.getGame().setCurrentAction(Action.PLAYCARD);
                        currentPlayer = firstPlayer;
                        translator.getGame().setCurrentPlayer(players.get(currentPlayer));
                        translator.endTurn();
                    }
                }
                break;

            case SHOWME:    //Every player in every moment of the match (after all the players have joined) can call SHOWME to give a look to the board
                if(numPlayers==0 || numPlayers!=maxPlayers) throw new IllegalMoveException("Wait until all the players join the match...");
                else {
                    //System.out.println(this.currentPhase);
                    translator.translateThis(message);
                }
                break;

            default:
                throw new UnrecognizedPlayerOrActionException("This kind of action doesn't exist!");
        }
    }



    //Return playerOrder after all the players have played their cards
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

    private boolean checkMovementLocations(Message m) {
        try {
            Set<Location> allowedLocations = this.translator.getAllowedDepartures();
            if (!allowedLocations.contains(m.getDepartureType()))
                return false;

            allowedLocations = this.translator.getAllowedArrivals();
            if (!(allowedLocations.contains(m.getArrivalType())))
                return false;

            if (m.getArrivalType() == m.getDepartureType())
                return false;

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Game getGame(){
        return translator.getGame();
    }

}