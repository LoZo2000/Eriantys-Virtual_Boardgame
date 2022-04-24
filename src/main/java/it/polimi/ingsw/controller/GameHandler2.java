package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.messages.Message;

import java.util.*;

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
    private Phase oldPhase;
    private Translator translator;
    //private ArrayList<Action>[] legitAction;
    private Map<Phase, ArrayList<Action>> legitAction;
    private int numFinishedTurn=0; //number of players that finished their turn
    private boolean usedCard;

    public Game getGame(){
        return translator.getGame();
    }

    public Phase getPhase(){
        return currentPhase;
    }

    public int getActivePlayers(){ //return the number of players in the game
        return numPlayers;
    }



    public GameHandler2(Message message) throws IllegalMessageException { //First instruction of all
        //System.out.println("sono qui lala"+message.getAction());
        if(message.getAction()!=Action.CREATEMATCH) throw new IllegalMessageException();
        //System.out.println("sono qui handler");
        completeRules = message.getCompleteRules();
        maxPlayers = message.getNumPlayers();
        if(maxPlayers==3) maxMoveStudent = 4;
        else maxMoveStudent=3;
        translator = new Translator(completeRules, maxPlayers);
        players = new ArrayList<>();
        orderPlayers = new ArrayList<>();
        firstPlayer=0;

        oldPhase = currentPhase;
        usedCard = false;

        initLegitActions();
    }

    private void nextPhase() throws EndGameException {
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
            usedCard = false;
            if(numFinishedTurn==maxPlayers){
                currentPhase=Phase.PRETURN;
                numFinishedTurn=0;
                currentPlayer=firstPlayer;
                translator.getGame().setCurrentPhase(currentPhase);
                translator.endTurn();
            }
            else {
                currentPhase=Phase.MIDDLETURN;
                translator.getGame().setCurrentPhase(currentPhase);
            }
        }
        else if(currentPhase == Phase.ACTIVECARD) {
            currentPhase = oldPhase;
        }

    }

    private void initLegitActions(){
        legitAction = new Hashtable<>();

        ArrayList<Action> temp = new ArrayList<>();
        temp.add(Action.ADDME);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.PREGAME, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.PLAYCARD);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.PRETURN, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.MOVESTUDENT);
        temp.add(Action.USEPOWER);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.MIDDLETURN, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.MOVEMOTHERNATURE);
        temp.add(Action.USEPOWER);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.MOVEMNTURN, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.SELECTCLOUD);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.ENDTURN, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.MOVESTUDENT);
        temp.add(Action.EXCHANGESTUDENT);
        temp.add(Action.ISLAND_INFLUENCE);
        temp.add(Action.BLOCK_ISLAND);
        temp.add(Action.BLOCK_COLOR);
        temp.add(Action.PUT_BACK);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.ACTIVECARD, (ArrayList<Action>) temp.clone());

    }

    //Check if the action is legit in that phase
    private boolean isLegitAction(Action action){
        return legitAction.get(currentPhase).contains(action);
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
        currentPlayer = players.indexOf(orderPlayers.get(numFinishedTurn%maxPlayers));
        translator.getGame().setCurrentPlayer(orderPlayers.get(numFinishedTurn%maxPlayers));
    }


    public void execute(Message message) throws IllegalMoveException, NotYourTurnException, UnrecognizedPlayerOrActionException, CannotJoinException, EndGameException, IllegalActionException, NoCharacterSelectedException, NoActiveCardException, NotEnoughMoneyException, NoMoreTokensException {
        if(isLegitPlayer(message.getSender())==false) throw new NotYourTurnException();
        if(isLegitAction(message.getAction())==false) throw new IllegalActionException();


        switch (message.getAction()){

            case ADDME:

                players.add(message.getSender());
                numPlayers++;
                translator.translateThis(message);
                if(numPlayers==maxPlayers) nextPhase();

                break;

            case PLAYCARD: //need to check if the player owns the card through an exception in translate
                translator.translateThis(message);
                priority[currentPlayer] = message.getPriority();
                currentPlayer = (currentPlayer+1)%maxPlayers;
                translator.getGame().setCurrentPlayer(players.get(currentPlayer));
                if(currentPlayer==firstPlayer){ //first player in the first turn is 0 initialized in nextphase
                    orderPlayers = sort();
                    firstPlayer = players.indexOf(orderPlayers.get(0));
                    currentPlayer=firstPlayer;
                    translator.getGame().setCurrentPlayer(orderPlayers.get(0));
                    nextPhase();
                }
                break;

            case MOVESTUDENT:
                if(currentPhase == Phase.ACTIVECARD){
                    Action requestedActiveAction = translator.getRequestedAction();
                    if(requestedActiveAction != Action.MOVESTUDENT)
                        throw new IllegalMoveException("Wrong move: you should not move students now!");

                    if(!checkMovementLocations(message))
                        throw new IllegalMoveException("You can't move students from these locations");
                }
                else if(message.getDepartureType()!=Location.ENTRANCE || (message.getArrivalType()!=Location.ISLAND && message.getArrivalType()!=Location.CANTEEN) )
                    throw new IllegalMoveException("Illegal movement!");

                translator.translateThis(message);
                if(currentPhase != Phase.ACTIVECARD)
                    currentMoveStudent++;
                //System.out.println("il numero di current move student è"+currentMoveStudent);
                if(currentMoveStudent==maxMoveStudent || currentPhase == Phase.ACTIVECARD){
                    nextPhase();
                }
                break;
                                   // discuss with others where to save cards of the players
            case MOVEMOTHERNATURE: //add a check if mother nature move of the right number of islands
                translator.translateThis(message);
                nextPhase();
                break;

            case USEPOWER:
                if(usedCard)
                    throw new IllegalMoveException("You can't use characters' powers twice in the same turn");
                boolean isActive = translator.translateThis(message);
                usedCard = true;
                if(isActive){
                    this.oldPhase = this.currentPhase;
                    this.currentPhase = Phase.ACTIVECARD;
                }
                break;

            case EXCHANGESTUDENT:
                if(currentPhase == Phase.ACTIVECARD){
                    Action requestedActiveAction = translator.getRequestedAction();
                    if(requestedActiveAction != Action.EXCHANGESTUDENT)
                        throw new IllegalMoveException("Wrong move: you should not move students now!");

                    if(!checkMovementLocations(message))
                        throw new IllegalMoveException("You can't move students from these locations");
                }

                translator.translateThis(message);

                if(currentPhase == Phase.ACTIVECARD){
                    nextPhase();
                }
                break;

            case BLOCK_ISLAND:
                if(currentPhase == Phase.ACTIVECARD){
                    Action requestedActiveAction = translator.getRequestedAction();
                    if(requestedActiveAction != Action.BLOCK_ISLAND)
                        throw new IllegalMoveException("Wrong move: you cannot block an island now!");
                }

                translator.translateThis(message);

                if(currentPhase == Phase.ACTIVECARD){
                    nextPhase();
                }
                break;

            case ISLAND_INFLUENCE:
                if(currentPhase == Phase.ACTIVECARD){
                    Action requestedActiveAction = translator.getRequestedAction();
                    if(requestedActiveAction != Action.ISLAND_INFLUENCE)
                        throw new IllegalMoveException("Wrong move: you cannot choose an island!");
                }

                translator.translateThis(message);

                if(currentPhase == Phase.ACTIVECARD){
                    nextPhase();
                }
                break;

            case BLOCK_COLOR:
                if(currentPhase == Phase.ACTIVECARD){
                    Action requestedActiveAction = translator.getRequestedAction();
                    if(requestedActiveAction != Action.BLOCK_COLOR)
                        throw new IllegalMoveException("Wrong move: you cannot choose a color to be blocked!");
                }

                translator.translateThis(message);

                if(currentPhase == Phase.ACTIVECARD){
                    nextPhase();
                }
                break;

            case PUT_BACK:
                if(currentPhase == Phase.ACTIVECARD){
                    Action requestedActiveAction = translator.getRequestedAction();
                    if(requestedActiveAction != Action.PUT_BACK)
                        throw new IllegalMoveException("Wrong move: you cannot put back students of a color now!");
                }

                translator.translateThis(message);

                if(currentPhase == Phase.ACTIVECARD){
                    nextPhase();
                }
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
}