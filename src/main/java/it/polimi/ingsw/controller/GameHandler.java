package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.messages.Message;

import java.util.*;

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

    public Phase getPhase(){
        return currentPhase;
    }
    public int getActivePlayers(){ //return the number of players in the game
        return numPlayers;
    }

    public List<String> getNicknames(){
        return new ArrayList<>(this.originalOrderPlayer);
    }

    public GameHandler(Game game){ //First instruction of all
        maxPlayers = game.getNumPlayers();

        this.game = game;

        players = new ArrayList<>();
        originalOrderPlayer = new ArrayList<>();
        oldPhase = currentPhase;

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
            numFinishedTurn=0;
            game.resetRemainingMoves();
            currentPhase=Phase.MIDDLETURN;
            game.setCurrentPhase(currentPhase);
        }
        else if(currentPhase==Phase.MIDDLETURN) {
            currentPhase = Phase.MOVEMNTURN;
            //System.out.println("CAMBIO fase"+currentPhase);
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

    //Check if the player og the actio is legit
    private boolean isLegitPlayer(String player){
        //if(!message.getSender().equals(orderPlayers.get(0))) throw new NotYourTurnException("This is not your turn");
        /*if(currentPlayer==-1) return true;
        else {
            return (player.equals(players.get(currentPlayer)));
        }*/
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

    public void execute(Message message) throws NotYourTurnException, IllegalActionException, IllegalMoveException, NoActiveCardException, NoIslandException, NoPlayerException, EndGameException, NoMoreTokensException, NotEnoughMoneyException, NoCharacterSelectedException, NoSuchStudentException, CannotAddStudentException {
        if(!isLegitPlayer(message.getSender())) {
            throw new NotYourTurnException();
        }
        if(!isLegitAction(message.getAction())) {
            throw new IllegalActionException();
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

    public List<String> getPlayers(){
        return (List<String>) players.clone();
    }

    private void addPlayers(){
        this.numPlayers++;
        if (numPlayers == maxPlayers){
            game.getAllPlayers().forEach((player -> {
                this.players.add(player.getNickname());
                this.originalOrderPlayer.add(player.getNickname());
            }));
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
                game.setLastTurn(true);
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