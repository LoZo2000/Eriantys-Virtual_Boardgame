package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.messages.Message;

import java.util.ArrayList;

public class GameHandler2 {
    private ArrayList<String> players;
    private int[] priority = {0,0,0,0};
    private ArrayList<String> orderPlayers;
    private int maxPlayers; //max players in the game
    private int numPlayers = 0; //current num of players
    private int currentPlayer = -1; //the current player in the array players
    private int firstPlayer = -1; //the first player that played a card
    private int maxMoveStudent = 3;
    private int currentMoveStudent = 0;
    private boolean completeRules;
    private Phase currentPhase = Phase.PREGAME;
    private Translator translator;
    private ArrayList<Action>[] legitAction;
    private int numFinishedTurn=0; //number of players that finished their turn


    public Game getGame(){
        return translator.getGame();
    }

    public Phase getPhase(){
        return currentPhase;
    }

    public int getActivePlayers(){ //return the number of players in the game
        return numPlayers;
    }



    //TODO: Exception added
    public GameHandler2(Message message) throws IllegalMessageException { //First instruction of all
        System.out.println("sono qui lala"+message.getAction());
        if(message.getAction()!=Action.CREATEMATCH) throw new IllegalMessageException();
        System.out.println("sono qui handler");
        completeRules = message.getCompleteRules();
        maxPlayers = message.getNumPlayers();
        if(maxPlayers==3) maxMoveStudent = 4;
        else maxMoveStudent=3;
        translator = new Translator(completeRules, maxPlayers);
        players = new ArrayList<>();
        orderPlayers = new ArrayList<>();
        firstPlayer=0;
        initLegitActions();
    }

    private void nextPhase(){
        if(currentPhase==Phase.PREGAME){
            currentPlayer=0;
            firstPlayer=0;
            currentPhase=Phase.PRETURN;
            translator.getGame().setCurrentPlayer(players.get(0));
            translator.getGame().setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.PRETURN) {
            currentPhase=Phase.MIDDLETURN;
            translator.getGame().setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.MIDDLETURN) {
            currentPhase = Phase.MOVEMNTURN;
            //System.out.println("CAMBIO fase"+currentPhase);
            currentMoveStudent = 0;
            translator.getGame().setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.MOVEMNTURN){
            currentPhase=Phase.ENDTURN;
            translator.getGame().setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.ENDTURN){
            if(numFinishedTurn==maxPlayers){
                currentPhase=Phase.PRETURN;
                numFinishedTurn=0;
                currentPlayer=firstPlayer;
                translator.getGame().setCurrentPhase(currentPhase);
            }
            else {
                currentPhase=Phase.MIDDLETURN;
                translator.getGame().setCurrentPhase(currentPhase);
            }
        }

    }

    private void initLegitActions(){
        legitAction= new ArrayList[5];

        legitAction[0] = new ArrayList<Action>(); //PREGAME TURN
        legitAction[0].add(Action.ADDME);
        legitAction[0].add(Action.SHOWME);

        legitAction[1] = new ArrayList<Action>(); //PRETURN
        legitAction[1].add(Action.PLAYCARD);
        legitAction[1].add(Action.SHOWME);

        legitAction[2] = new ArrayList<Action>(); //MIDDLE TURN
        legitAction[2].add(Action.MOVESTUDENT);
        legitAction[2].add(Action.SHOWME);

        legitAction[3] = new ArrayList<Action>(); //MOVEMNTURN
        legitAction[3].add(Action.MOVEMOTHERNATURE);
        legitAction[3].add(Action.SHOWME);

        legitAction[4] = new ArrayList<Action>(); //ENDTURN
        legitAction[4].add(Action.SELECTCLOUD);
        legitAction[4].add(Action.SHOWME);
    }

    //Check if the action is legit in that phase
    private boolean isLegitAction(Action action){
        return legitAction[currentPhase.ordinal()].contains(action);
    }

    //Check if the player og the actio is legit
    private boolean isLegitPlayer(String player){
        //if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
        if(currentPlayer==-1) return true;
        else {
            return (player.equals(players.get(currentPlayer)));
        }
    }

    //refresh the current player
    private void refreshCurrentPlayer() {
        currentPlayer = players.indexOf(orderPlayers.get(numFinishedTurn));
        translator.getGame().setCurrentPlayer(orderPlayers.get(numFinishedTurn));
        //TODO Decide how to manage the current player in game
    }


    public void execute(Message message) throws IllegalMoveException, NotYourTurnException, UnrecognizedPlayerOrActionException, CannotJoinException, EndGameException, IllegalActionException, NoCharacterSelectedException, NoActiveCardException {
        if(isLegitPlayer(message.getSender())==false) throw new NotYourTurnException();
        if(isLegitAction(message.getAction())==false) throw new IllegalActionException();


        switch (message.getAction()){

            case ADDME:

                players.add(message.getSender());
                numPlayers++;
                translator.translateThis(message);
                if(numPlayers==maxPlayers) nextPhase();
                System.out.println(translator.getGame().getCurrentPlayer());

                break;

            case PLAYCARD: //need to check if the player owns the card through an exception in translate
                priority[currentPlayer] = message.getPriority();
                translator.translateThis(message);
                currentPlayer = (currentPlayer+1)%maxPlayers;
                translator.getGame().setCurrentPlayer(players.get(currentPlayer));
                if(currentPlayer==firstPlayer){ //first player in the first turn is 0 initialized in nextphase
                    orderPlayers = sort();
                    firstPlayer = players.indexOf(orderPlayers.get(0));
                    currentPlayer=firstPlayer;
                    translator.getGame().setCurrentPlayer(orderPlayers.get(0));
                    nextPhase();
                    //TODO: remember to disable the previously active rule!!!
                }
                break;

            case MOVESTUDENT:
                if(message.getDepartureType()!=Location.ENTRANCE || (message.getArrivalType()!=Location.ISLAND && message.getArrivalType()!=Location.CANTEEN) ) throw new IllegalMoveException("Illegal movement!");

                translator.translateThis(message);
                currentMoveStudent++;
                //System.out.println("il numero di current move student è"+currentMoveStudent);
                if(currentMoveStudent==maxMoveStudent){
                    nextPhase();
                }
                break;
                                   // discuss with others where to save cards of the players
            case MOVEMOTHERNATURE: //add a check if mother nature move of the right number of islands
                translator.translateThis(message);
                nextPhase();
                break;

            case SELECTCLOUD:
                translator.translateThis(message);
                numFinishedTurn++;
                refreshCurrentPlayer();
                nextPhase();
                break;

            case SHOWME:
                if(numPlayers==0 || numPlayers!=maxPlayers) throw new IllegalMoveException("Wait until all the players join the match...");
                else translator.translateThis(message);
                break;

            default:
                throw new UnrecognizedPlayerOrActionException("This kind of action doesn't exist!");
        }
    }



    private ArrayList<String> sort(){ //to check beacuse i don't understand if it is right
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