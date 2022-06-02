package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.messages.Message;

import java.util.*;

/**
 * The class Gamehandler is the core of the controller, its role is to manage the turn in the game, the current player, the legit action for each player and
 * phase, the game is divided in phases, every phase has a set of permitted action, if the action is permitted and the sender of the action is the current player
 * the method execute is call, execute is a method inside every message that modify the game, execute return an object called update that permit the gamehandler to
 * refresh the state of the GameHandler (turn, phase)
 */
public class GameHandler {
    private final ArrayList<String> players;
    private final ArrayList<String> originalOrderPlayer;
    private final int maxPlayers; //max players in the game
    private int numPlayers = 0; //current num of players
    private String currentPlayer = null; //the current player in the array players
    private Phase currentPhase = Phase.PREGAME;
    private Phase oldPhase;
    private final Game game;
    private Map<Phase, ArrayList<Action>> legitAction;
    private int numFinishedTurn=0; //number of players that finished their turn

    private boolean finishedGame=false;

    private boolean isLastTurn= false;
    private boolean nextLastTurn=false;
    private boolean started;

    /**
     * Method to return the current phase of the game
     * @return a phase object that represent the current phase in the turn
     */
    public Phase getPhase(){
        return currentPhase;
    }

    /**
     * Method that return the num of player that joined the game
     * @return an int that represent the number of players that joined the game
     */
    public int getActivePlayers(){ //return the number of players in the game
        return numPlayers;
    }

    /**
     * Method that return the nicknames of the players of the game
     * @return a list of string that contain the nicknames of the players of the game
     */
    public List<String> getNicknames(){
        return new ArrayList<>(this.originalOrderPlayer);
    }

    /**
     * Method that report if the game is started
     * @return a boolean that report if the game is started
     */
    public boolean isStarted(){
        return this.started;
    }

    /**
     * Is the constructor of GameHandler
     * @param game is given to the constructor to associate the GameHandler with the Game
     */
    public GameHandler(Game game){ //First instruction of all
        maxPlayers = game.getNumPlayers();

        this.game = game;

        players = new ArrayList<>();
        originalOrderPlayer = new ArrayList<>();
        oldPhase = currentPhase;

        started = false;

        initLegitActions();
    }



    private void nextPhase(){
        if(currentPhase==Phase.PREGAME){
            currentPlayer=players.get(0);
            currentPhase=Phase.PRETURN;
            game.setCurrentPlayer(players.get(0), false);
            game.setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.PRETURN) {
            if(nextLastTurn){
                isLastTurn=true;
                game.setLastTurn(true);
            }
            numFinishedTurn=0;
            game.resetRemainingMoves();
            currentPhase=Phase.MIDDLETURN;
            game.setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.MIDDLETURN) {
            currentPhase = Phase.MOVEMNTURN;
            game.setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.MOVEMNTURN){
            currentPhase=Phase.ENDTURN;
            game.setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.ENDTURN){
            game.setUsedCard(false);
            if(numFinishedTurn==maxPlayers){
                game.resetPlayedCards();
                endTurn();
                clockwiseOrder();
                currentPhase=Phase.PRETURN;
                if(isLastTurn) endGame();
                numFinishedTurn=0;
                game.setCurrentPhase(currentPhase);
            }
            else {
                currentPhase=Phase.MIDDLETURN;
                game.resetRemainingMoves();
                game.setCurrentPhase(currentPhase);
            }
        }
        else if(currentPhase == Phase.ACTIVECARD) {
            currentPhase = oldPhase;
            game.setCurrentPhase(currentPhase);
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
        temp.add(Action.EXCHANGESTUDENT);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.MIDDLETURN, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.MOVEMOTHERNATURE);
        temp.add(Action.USEPOWER);
        temp.add(Action.EXCHANGESTUDENT);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.MOVEMNTURN, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.SELECTCLOUD);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.ENDTURN, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.MOVESTUDENT);
        temp.add(Action.ISLAND_INFLUENCE);
        temp.add(Action.BLOCK_ISLAND);
        temp.add(Action.BLOCK_COLOR);
        temp.add(Action.PUT_BACK);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.ACTIVECARD, (ArrayList<Action>) temp.clone());

        temp.clear();
        temp.add(Action.ENDGAME);
        temp.add(Action.SHOWME);
        legitAction.put(Phase.ENDGAME, (ArrayList<Action>) temp.clone());

    }

    //Check if the action is legit in that phase
    private boolean isLegitAction(Action action){
        return legitAction.get(currentPhase).contains(action);
    }

    //Check if the player og the action is legit
    private boolean isLegitPlayer(String player){
        if(currentPlayer == null)
            return true;

        return player.equals(currentPlayer);

    }

    private void endGame(){
        currentPhase=Phase.ENDGAME;
        this.finishedGame=true;
        game.setCurrentPhase(currentPhase);
        game.endGame();
    }

    private void changeCurrentPlayer(boolean sendNotify){
        Collections.rotate(players, -1);
        currentPlayer = players.get(0);
        game.setCurrentPlayer(currentPlayer, sendNotify);
    }

    /**
     * The method execute take the message arrived from the users, check if the sender of the action is the current player, check if the action is legit in that phase
     * of the turn and then call the method execute of message, every message has an override of the method execute of the super class Message, in every
     * message the execute method modify tha game in a different way and return an update object, the update object id analyzed by the execute method of
     * gamehandler to refresh the state of the turn and phase
     * @param message is the message that will be checked and eventually executed
     * @throws NotYourTurnException is the exception thrown when a player send a message and is not the current player
     * @throws IllegalActionException is the exception thrown when the current player try to do an action not permitted in the current phase
     * @throws IllegalMoveException is the exception thrown when is not possible to use a card
     * @throws NoActiveCardException is the exception thrown when there is no an active card and someone try to use it
     * @throws NoIslandException is the exception thrown when is requested an island that doesn't exist
     * @throws NoPlayerException is the exception thrown when is requested a player that doesn't exist
     * @throws NoMoreTokensException is the exception thrown when the tokens are finished and someone try to use them
     * @throws NotEnoughMoneyException is the exception thrown when the money are finished and someone try to use them
     * @throws NoCharacterSelectedException is the exception thrown when in the method usePower in game someone try to use a character with
     * a wrong index
     * @throws NoSuchStudentException is the exception thrown when are trying to move a student that doesn't
     * exist in that location (by studentid)
     * @throws CannotAddStudentException is the exception thrown when is not possible to add a student
     */
    public void execute(Message message) throws NotYourTurnException, IllegalActionException, IllegalMoveException, NoActiveCardException, NoIslandException, NoPlayerException, NoMoreTokensException, NotEnoughMoneyException, NoCharacterSelectedException, NoSuchStudentException, CannotAddStudentException {
        if(!isLegitPlayer(message.getSender())) {
            throw new NotYourTurnException("This is not your turn: please wait...");
        }
        if(!isLegitAction(message.getAction())) {
            throw new IllegalActionException("Illegal move!");
        }

        Update update = message.execute(game);

        update.getIsLastTurn().ifPresent(last -> this.isLastTurn=last);

        update.getFinishedGame().ifPresent((finish) ->{
            finishedGame=finish;
            if(finishedGame){
                currentPhase=Phase.ENDGAME;
                game.setCurrentPhase(currentPhase);
            }
        } );

        update.getChangedNumPlayer().ifPresent((numPlayer) -> {
            this.addPlayers();
        });

        update.getPlayedCard().ifPresent((played) -> this.playedCard());

        update.getNumMovedStudents().ifPresent((remainingMoves) -> {
            if(remainingMoves == 0){
                endMovementPhase();
            }
        });

        update.getActivatedCard().ifPresent((activatedCard) -> {
            if(activatedCard){
                try {
                    if(game.getRequestedAction() != Action.EXCHANGESTUDENT) {
                        this.oldPhase = this.currentPhase;
                        this.currentPhase = Phase.ACTIVECARD;
                        game.setCurrentPhase(currentPhase);
                    }
                } catch (NoActiveCardException e) {
                    //Can't get here
                }
            } else{
                nextPhase();
            }
        });

        update.getMovedMotherNature().ifPresent((moved) -> nextPhase());

        update.getRemainingNumClouds().ifPresent((remainingClouds) -> {
            numFinishedTurn++;
            changeCurrentPlayer(false);
            nextPhase();
        });

    }

    //TODO Debug method used for testing
    Game getGame(){
        return game;
    }

    /**
     * Method to return the players in the fame
     * @return a list of String with the nicknames of the players
     */
    public List<String> getPlayers(){
        return (List<String>) players.clone();
    }

    private void addPlayers(){
        this.numPlayers++;

        //Each time a player is added to the Game the list of nicknames of players is updated
        this.players.clear();
        this.originalOrderPlayer.clear();
        game.getAllPlayers().forEach((player -> {
            this.players.add(player.getNickname());
            this.originalOrderPlayer.add(player.getNickname());
        }));

        if (numPlayers == maxPlayers){
            this.started = true;
            nextPhase();
        }
    }

    private void playedCard(){
        this.numFinishedTurn++;
        if (numFinishedTurn == numPlayers) {
            //Now the player List is ordered by the card played during the PLAYCARD Phase
            players.clear();
            game.getAllPlayers().stream().sorted().forEach((player -> players.add(player.getNickname())));

            currentPlayer = players.get(0);
            game.setCurrentPlayer(currentPlayer, false);

            nextPhase();
        } else{
            changeCurrentPlayer(true);
        }
    }

    private void endMovementPhase() {
        game.resetRemainingMoves();
        nextPhase();
    }

    private void endTurn(){
        try {
            game.refillClouds();
        }catch(Exception e){
            if(e instanceof NoMoreStudentsException){
                game.setNextLastTurn(true);
                this.nextLastTurn=true;
            }
            e.printStackTrace();
        }
    }

    private void clockwiseOrder(){
        int pos = originalOrderPlayer.indexOf(this.players.get(0));
        this.players.clear();
        for(int i = 0; i<this.numPlayers; i++){
            this.players.add(originalOrderPlayer.get(pos));
            pos = (pos + 1) % numPlayers;
        }
    }

    //Todo eliminate this commented code
    /*private ColorTower getWinner(){
        int black=0;
        int white=0;
        int grey=0;
        LinkedList<Island> islands = game.getAllIslands();
        for(Island i : islands){
            if(i.getOwner()!=null){
                switch (i.getOwner()){
                    case BLACK -> black += i.getReport().getTowerNumbers();
                    case WHITE -> white += i.getReport().getTowerNumbers();
                    case GREY -> grey += i.getReport().getTowerNumbers();
                }
            }
        }
        if(black>=white && black>=grey) return ColorTower.BLACK;
        if(white>=black && white>=grey) return ColorTower.WHITE;
        return ColorTower.GREY;
    }*/
}