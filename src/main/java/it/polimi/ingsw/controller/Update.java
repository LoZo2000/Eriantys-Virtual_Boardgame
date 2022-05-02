package it.polimi.ingsw.controller;

import java.util.Optional;

public class Update {
    private final Integer changedNumPlayer;
    private final Boolean playedCard;
    private final Integer numMovedStudents;
    private final Integer remainingNumClouds;
    private final Boolean activatedCard;
    private final Boolean movedMotherNature;

    public Update(Integer changedNumPlayer, Boolean playedCard, Integer numMovedStudents, Integer remainingClouds, Boolean activatedCard, Boolean movedMotherNature){
        this.changedNumPlayer = changedNumPlayer;
        this.playedCard = playedCard;
        this.numMovedStudents = numMovedStudents;
        this.remainingNumClouds = remainingClouds;
        this.activatedCard = activatedCard;
        this.movedMotherNature = movedMotherNature;
    }

    public Optional<Integer> getChangedNumPlayer() {
        return Optional.ofNullable(changedNumPlayer);
    }

    public Optional<Boolean> getPlayedCard() {
        return Optional.ofNullable(playedCard);
    }

    public Optional<Integer> getNumMovedStudents() {
        return Optional.ofNullable(numMovedStudents);
    }

    public Optional<Integer> getRemainingNumClouds() {
        return Optional.ofNullable(remainingNumClouds);
    }

    public Optional<Boolean> getActivatedCard() {
        return Optional.ofNullable(activatedCard);
    }

    public Optional<Boolean> getMovedMotherNature(){
        return Optional.ofNullable(movedMotherNature);
    }
}
