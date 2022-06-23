package it.polimi.ingsw.client.cli;


import java.util.Scanner;

/**
 * This is the entry point to play a CLI-based game. Its only purpose is to ask the player the address and
 * the port of the server. We suppose the player knows the address and the port of the server.
 */
public class ClientCliApp {

    /**
     * This is the entry point to play a CLI-based game.
     */
    public static void main(String[] args){
        Scanner stdin = new Scanner(System.in);
        Client client;

        System.out.println("Is the server running on the this pc? (0=N0, 1=YES)");
        int samePC = Integer.parseInt(stdin.nextLine());
        if(samePC==0){
            System.out.println("What is the server's ip?");
            String ip = stdin.nextLine();
            System.out.println("What is the server's port?");
            int port;
            do{
                try {
                    port = Integer.parseInt(stdin.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("The inserted value isn't a valid number: insert the port again please...");
                    port = -1;
                }
            }while(port == -1);
            client = new Client(ip, port);
        }
        else{
            client = new Client("127.0.0.1", 12346);
        }

        try{
            client.startClient();
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }
}