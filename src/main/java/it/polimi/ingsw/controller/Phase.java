package it.polimi.ingsw.controller;

/**
 * The enum class Phase contains the phase in which every turn is divided, in the gamehandler are defined all the
 * actions legit in each phase
 */
public enum Phase {
    PREGAME, PRETURN, MIDDLETURN, MOVEMNTURN, ACTIVECARD, ENDTURN, ENDGAME
}