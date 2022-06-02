package it.polimi.ingsw.controller;

import java.util.Optional;

/**
 * The class update id an object returned to the controller after every execution of a command received
 * by the players, it present the state of the game to the controller, is principally a class made of attribute and getter
 * all the getter return an optional value to manage better null values.
 */
public class Update {
    private final Integer changedNumPlayer;
    private final Boolean playedCard;
    private final Integer numMovedStudents;
    private final Integer remainingNumClouds;
    private final Boolean activatedCard;
    private final Boolean movedMotherNature;

    private final Boolean finishedGame;

    private final String winner;

    private final Boolean isLastTurn;

    /**
     * The constructor update create an update object initializing all his attribute
     * @param changedNumPlayer reports the number of player in the game
     * @param playedCard played card notify is a card has been played
     * @param numMovedStudents reports the number of students moved
     * @param remainingClouds reports the number of remaining clouds
     * @param activatedCard reports if a card has been activated
     * @param movedMotherNature reports if mother nature has been moved
     * @param finishedGame reports if the is finished
     * @param winner reports who is the winner (if the game is finished)
     * @param isLastTurn reports if the l<st turn of the game
     */
    public Update(Integer changedNumPlayer, Boolean playedCard, Integer numMovedStudents, Integer remainingClouds, Boolean activatedCard, Boolean movedMotherNature, Boolean finishedGame, String winner, Boolean isLastTurn){
        this.changedNumPlayer = changedNumPlayer;
        this.playedCard = playedCard;
        this.numMovedStudents = numMovedStudents;
        this.remainingNumClouds = remainingClouds;
        this.activatedCard = activatedCard;
        this.movedMotherNature = movedMotherNature;
        this.finishedGame = finishedGame;
        this.winner = winner;
        this.isLastTurn = isLastTurn;
    }

    /**
     * Method to return the number of player in the game if changed
     * @return an integer that represent the number of players in the game
     */
    public Optional<Integer> getChangedNumPlayer() {
        return Optional.ofNullable(changedNumPlayer);
    }

    /**
     * Method to return if a card has been played
     * @return a boolean that report if a card has been played
     */
    public Optional<Boolean> getPlayedCard() {
        return Optional.ofNullable(playedCard);
    }

    /**
     * Method to return the number of moved students
     * @return an integer representing the number of moved student
     */
    public Optional<Integer> getNumMovedStudents() {
        return Optional.ofNullable(numMovedStudents);
    }

    /**
     * Method to return the number of remaining cloud
     * @return an integer representing the number of remaining cloud
     */
    public Optional<Integer> getRemainingNumClouds() {
        return Optional.ofNullable(remainingNumClouds);
    }

    /**
     * Method to report if a card has been activated
     * @return a boolean to report if a card has been activated
     */
    public Optional<Boolean> getActivatedCard() {
        return Optional.ofNullable(activatedCard);
    }

    /**
     * Method to report if mother nature has been moved
     * @return a boolean to report if  mother nature has been moved
     */
    public Optional<Boolean> getMovedMotherNature(){
        return Optional.ofNullable(movedMotherNature);
    }

    /**
     * Method to report if the game is finished
     * @return a boolean to report if the is finished
     */
    public Optional<Boolean> getFinishedGame(){ return Optional.ofNullable(finishedGame);}

    /**
     * Method to return the winner of the game (if the game is finished)
     * @return a String with the nickname of the winner of the game
     */
    public Optional<String> getWinner(){ return Optional.ofNullable(winner); }

    /**
     * Method to return if the current turn is the last
     * @return a boolean to report if the turn is the last of the game
     */
    public Optional<Boolean> getIsLastTurn(){ return Optional.ofNullable(isLastTurn); }
}