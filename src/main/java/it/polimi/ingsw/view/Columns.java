package it.polimi.ingsw.view;

import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.List;

public class Columns {

    private final int terminalDim;
    private final List<String> col1;
    private final List<String> col2;

    public Columns(){
        col1 = new ArrayList<>();
        col2 = new ArrayList<>();
        terminalDim = AnsiConsole.getTerminalWidth() != 0 ? AnsiConsole.getTerminalWidth() : 178;
    }

    public void addColumn(int numberColumn, String stringToInsert){
        int maxLength = terminalDim / 2-2;

        List<String> strings = removeNewLines(stringToInsert);

        for(String string : strings) {
            int length = getStringLengthWithoutANSI(string);
            if (numberColumn == 1) {
                if (length > maxLength) {
                    List<String> l = new ArrayList<>();
                    int i = 0;
                    int previousEnd = 0;
                    do {
                        int endString;
                        if((i + 1) * maxLength < length){
                            endString = getPreviousSpacePosition(string, (i + 1) * maxLength);
                        } else{
                            endString = string.length();
                        }
                        l.add(string.substring(previousEnd, endString));
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
                        if((i + 1) * maxLength < length){
                            endString = getPreviousSpacePosition(string, (i + 1) * maxLength);
                        } else{
                            endString = string.length();
                        }
                        l.add(string.substring(previousEnd, endString));
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

    public void printAll(){
        int maxLength = terminalDim / 2 - 2;
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
