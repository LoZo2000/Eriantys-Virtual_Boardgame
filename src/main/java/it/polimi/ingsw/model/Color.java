package it.polimi.ingsw.model;

import org.fusesource.jansi.Ansi;

/**
 * The enum class Color represent the color of the students
 */
public enum Color {
    BLUE(Ansi.Color.BLUE, "B"),
    YELLOW(Ansi.Color.YELLOW, "Y"),
    RED(Ansi.Color.RED, "R"),
    GREEN(Ansi.Color.GREEN, "G"),
    PINK(Ansi.Color.MAGENTA, "P");
    public final Ansi.Color ansiColor;
    public final String shortName;

    /**
     * Constructor of the enum class
     * @param ansiColor the ansicolor to assign to the color, ansicolor is the color object of the library jansi
     * @param shortName the shortname to assign to the color
     */
    Color(Ansi.Color ansiColor, String shortName) {
        this.ansiColor = ansiColor;
        this.shortName = shortName;
    }
}