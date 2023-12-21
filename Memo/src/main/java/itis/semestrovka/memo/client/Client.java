package itis.semestrovka.memo.client;

import itis.semestrovka.memo.server.Room;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String playerName;
    public static Set<Client> clients = new HashSet<>();

    private Room room;

    public Client(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clients.add(this);
        }catch(IOException e){
            System.out.println("Error creating Client!");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try{

            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to the Server!");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room r) {
        this.room = r;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }
}
