package it.polimi.ingsw.view;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent the columns in which is divided the terminal to print things, it implements some methods to help
 * the programmer print text divided in column
 */
public class Columns {

    private final int terminalDim;
    private final List<String> col1;
    private final List<String> col2;

    /**
     * This method is the constructor of the class
     */
    public Columns(){
        col1 = new ArrayList<>();
        col2 = new ArrayList<>();
        terminalDim = AnsiConsole.getTerminalWidth() != 0 ? AnsiConsole.getTerminalWidth() : 178;
    }

    /**
     * This method is called to insert something that will be printed in a column
     * @param numberColumn is the id of the column in which print the text
     * @param stringToInsert is the String to insert in the column
     */
    public void addColumn(int numberColumn, String stringToInsert){
        int maxLength = terminalDim / 2-3;

        List<String> strings = removeNewLines(stringToInsert);

        for(String string : strings) {
            int length = getStringLengthWithoutANSI(string);
            if (numberColumn == 1) {
                if (length > maxLength) {
                    List<String> l = new ArrayList<>();
                    int i = 0;
                    int previousEnd = 0;
                    do {
                        String toAdd;
                        int endString;
                        if((i + 1) * maxLength < length){
                            endString = getPreviousSpacePosition(string, (i + 1) * maxLength);
                        } else{
                            endString = string.length();
                        }
                        toAdd = string.substring(previousEnd, endString).strip();
                        if(i != 0){
                            toAdd = "> " + toAdd;
                        }
                        l.add(toAdd);
                        previousEnd = endString+1;
                        i++;
                    } while (i * maxLength < length);
                    col1.addAll(l);
                } else {
                    col1.add(string);
                }
            } else if (numberColumn == 2) {
                if (length > maxLength) {
                    List<String> l = new ArrayList<>();
                    int i = 0;
                    int previousEnd = 0;
                    do {
                        int endString;
                        String toAdd;
                        if((i + 1) * maxLength < length){
                            endString = getPreviousSpacePosition(string, (i + 1) * maxLength);
                        } else{
                            endString = string.length();
                        }
                        toAdd = string.substring(previousEnd, endString).strip();
                        if(i != 0){
                            toAdd = "> " + toAdd;
                        }
                        l.add(toAdd);
                        previousEnd = endString;
                        i++;
                    } while (i * maxLength < length);
                    col2.addAll(l);
                } else {
                    col2.add(string);
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * This method is called to print the columns and everything they contain
     */
    public void printAll(){
        System.out.println(Ansi.ansi().eraseScreen().toString());
        int maxLength = terminalDim / 2 - 3;
        int maxSize = Math.max(col1.size(), col2.size());

        for(int i=0; i<maxSize; i++){
            if(col1.size() != 0){
                String s = col1.remove(0);
                int len = getStringLengthWithoutANSI(s);
                System.out.print(s);
                for(int j=0; j<(maxLength-len); j++){
                    System.out.print(" ");
                }
                System.out.print(" | ");
            } else{
                for(int j=0; j<maxLength; j++){
                    System.out.print(" ");
                }
                System.out.print(" | ");
            }

            if(col2.size() != 0){
                String s = col2.remove(0);
                int len = getStringLengthWithoutANSI(s);
                System.out.print(s);
                for(int j=0; j<(maxLength-len); j++){
                    System.out.print(" ");
                }
                System.out.println();
            } else{
                System.out.println();
            }
            //System.out.println(col1.remove(0).length() + "-" + col2.remove(0).length());
        }
    }

    private List<String> removeNewLines(String s){
        String[] strings = s.split("\\n", -1);
        return List.of(strings);
    }

    private int getStringLengthWithoutANSI(String str) {
        return str.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length();
    }

    private int getPreviousSpacePosition(String s, int endPosition){
        for(int i=endPosition-1; i>=0; i--){
            if(s.charAt(i) == ' '){
                return i;
            }
        }
        return 0;
    }

}
