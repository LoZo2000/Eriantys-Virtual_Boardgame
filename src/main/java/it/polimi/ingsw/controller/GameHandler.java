package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.messages.Message;

import java.util.ArrayList;

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

    private Translator translator;



    public GameHandler(){ //First instruction of all
        players = new ArrayList<>();
        orderPlayers = new ArrayList<>();
    }



    public void execute(Message message) throws IllegalMoveException, NotYourTurnException, UnrecognizedPlayerOrActionException, CannotJoinException, EndGameException {
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

    /*private boolean CharacterPlayed;
    private final Game game;
    private final int numPlayers;
    private int currentPlayer;

    public GameHandler(Game game) {
        this.game= game;
        this.numPlayers= game.getNumPlayers();
        CharacterPlayed=false;
    }

    public void Turn(){
        PreTurn();
        MiddleTurn();
        //EndTurn();
    }


    private void PreTurn(){
        int first = game.getLastPlayed();
        for(int i=0; i<numPlayers; i++){
            //tutti i metodi non ritornano nulla in quanto sono da implementare
            ThrowCard(game.getPlayerNum(i));
            for(int j=1; j<(numPlayers); j++) {
                WaitForThrownCard(game.getPlayerNum(i + j));
            }

            WaitForUserCard();

            SaveCard();

            WaitForThrownCard(game.getPlayerNum(i));
            for(int j=1; j<(numPlayers); j++) {
                SendCard(game.getPlayerNum(i+1));
            }

        }
        currentPlayer=WhoPlays();
        game.setLastPlayed(currentPlayer);
    }

    private void MiddleTurn(){
        for(int i=0; i<numPlayers; i++){
            StartTurn(game.getPlayerNum(currentPlayer));
            for(int j=1; j<(numPlayers); j++) {
                WaitTurn(game.getPlayerNum(currentPlayer+j));
            }

            WaitStudents();

            for(int j=1; j<(numPlayers); j++) {
                NotifyStudent(game.getPlayerNum(currentPlayer+j));
            }

            WaitMotherNature();

            for(int j=1; j<(numPlayers); j++) {
                NotifyMotherNature(game.getPlayerNum(currentPlayer+j));
            }

            WaitCloud();

            for(int j=1; j<(numPlayers); j++) {
                NotifyCloud(game.getPlayerNum(currentPlayer+j));
            }
            currentPlayer++;
        }
    }

    //da implementare
    private void ThrowCard(Player player){}

    private void WaitForThrownCard(Player player){}

    private void WaitForUserCard(){}

    private void SendCard(Player player){}

    private void SaveCard(){}

    private int WhoPlays(){}

    private void StartTurn(Player currentPlayer){}

    private void WaitTurn(Player nonCurrentPlayer){}

    private void WaitStudents(){}

    private void NotifyStudent(Player player){}

    private void WaitMotherNature(){}

    private void NotifyMotherNature(Player player){}

    private void WaitCloud(){}

    private void NotifyCloud(Player player){}*/

}
