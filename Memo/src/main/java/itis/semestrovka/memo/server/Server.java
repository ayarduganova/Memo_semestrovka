package itis.semestrovka.memo.server;

import itis.semestrovka.memo.server.Connection;
import itis.semestrovka.memo.server.Room;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

    private static ServerSocket serverSocket;
    private static Set<Room> rooms = new HashSet<>();

    public Server() {
        serverSocket = getServerSocket();
        try {
            serverLife();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void serverLife() throws IOException {
        while(!serverSocket.isClosed()){
            Socket client = serverSocket.accept();
            System.out.println("опа");
            Connection c = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String room = reader.readLine();
            for(Room r : rooms){
                if(r.getName().equals(room)){
                    System.out.println("кто-то подключился");
                    c = new Connection(client, reader, r);
                }
            }
            if(c == null){
                Room r = new Room(room, rooms.size() + 1);
                rooms.add(r);
                System.out.println("кто-то подключился и создал новую комнату");
                c = new Connection(client, reader, r);
            }
        }
    }

    private ServerSocket getServerSocket(){
        if(serverSocket == null){
            try {
                serverSocket = new ServerSocket(1234);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return serverSocket;
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(socket != null){
                socket.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveMessage(VBox listRooms) {
    }
}
