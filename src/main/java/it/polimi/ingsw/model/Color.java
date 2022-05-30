package it.polimi.ingsw.model;

import org.fusesource.jansi.Ansi;

public enum Color {
    BLUE(Ansi.Color.BLUE, "B"),
    YELLOW(Ansi.Color.YELLOW, "Y"),
    RED(Ansi.Color.RED, "R"),
    GREEN(Ansi.Color.GREEN, "G"),
    PINK(Ansi.Color.MAGENTA, "P");

    public final Ansi.Color ansiColor;
    public final String shortName;

    Color(Ansi.Color ansiColor, String shortName) {
        this.ansiColor = ansiColor;
        this.shortName = shortName;
    }
}