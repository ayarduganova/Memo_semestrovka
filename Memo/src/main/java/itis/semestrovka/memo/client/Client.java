package itis.semestrovka.memo.client;

import itis.semestrovka.memo.controllers.CreateRoomController;
import itis.semestrovka.memo.controllers.EnterToRoomController;
import itis.semestrovka.memo.server.Room;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String playerName;

    public Client(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){
            System.out.println("Error creating Client!");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
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
    public void receiveMessageFromServer(VBox vbox_messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try{

                        String roomName = bufferedReader.readLine();
                        String roomSize = bufferedReader.readLine();

                        System.out.println(roomName);
                        System.out.println(roomSize);

                        EnterToRoomController.addLabel(roomName, roomSize, vbox_messages);

                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error receiving message from the Server!");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void sendRoom(Room room) {
        try{
            bufferedWriter.write(room.getName());
            bufferedWriter.newLine();
            bufferedWriter.flush();

            bufferedWriter.write(room.getMaxSize().toString());
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
}
