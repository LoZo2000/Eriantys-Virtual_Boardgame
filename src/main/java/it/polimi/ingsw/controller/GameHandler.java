package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.Message;

import java.util.ArrayList;

public class GameHandler {
    private ArrayList<String> players;          //List of player in the game (it's "final": after all the player have joined, this list will not be modified). It is necessary to remember the order of the players during the PLAYCARD phase
    private int[] priority = {0,0,0,0};         //Priority of the card played by the player with the same index in "players"
    private ArrayList<String> orderPlayers;     //This list will be created every turn after the players have played their cards. The game will accept only moves made by the player in position 0. After he/she has selected a cloud, the player in postion 0 is removed
    private int maxPlayers = 0;                 //Number of players expected to join the match (choosen by the first player)
    private int numPlayers = 0;                 //Number of player currently enrolled in our match
    private int currentPlayer = -1;             //Player who is expected to play now
    private int firstPlayer = -1;               //Player who played the card with the lowest priority (who will have to play a card by first the next turn)
    private int maxMoveStudent = 3;             //Number of students a player is allowed to move every turn
    private int actualMoveStudent = 0;          //Number of students the current player has already moved in this turn
    private boolean completeRules = false;
    private Action currentPhase = Action.ADDME; //First phase is ADDME: we wait for some players to join

    private Translator translator;



    public GameHandler(){ //First instruction of all
        players = new ArrayList<>();
        orderPlayers = new ArrayList<>();
    }



    public void execute(Message message) throws IllegalMoveException, NotYourTurnException, UnrecognizedPlayerOrActionException, CannotJoinException, EndGameException {
        switch (message.getAction()){

            case ADDME:     //To add a new player
                if(numPlayers==0){      //If he/she is the first player, he/she has the right to decide the rules of the match
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
                    if(numPlayers==maxPlayers){     //If it is ture, it means that all the players have joined: time to change phase
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
                    if(currentPlayer==firstPlayer){     //If it is true, this means that all the players have already played their cards: time to change phase
                        orderPlayers = sort();          //New order according to the cards played
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
                    if(actualMoveStudent==maxMoveStudent){      //After 3 or 4 students have been moved, it is time to move MotherNature
                        currentPhase = Action.MOVEMOTHERNATURE;
                        actualMoveStudent = 0;
                    }
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
                    if(orderPlayers.size()>0) {     //It is time for the following player to move students!
                        currentPhase = Action.MOVESTUDENT;
                    }
                    else{                           //Else, if orderPlayers is empty: all the players have played their turns: it is time to play a card!
                        currentPhase = Action.PLAYCARD;
                        currentPlayer = firstPlayer;
                        translator.endTurn();
                    }
                }
                break;

            case SHOWME:    //Every player in every moment of the match (after all the players have joined) can call SHOWME to give a look to the board
                if(numPlayers==0 || numPlayers!=maxPlayers) throw new IllegalMoveException("Wait until all the players join the match...");
                else translator.translateThis(message);
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
}