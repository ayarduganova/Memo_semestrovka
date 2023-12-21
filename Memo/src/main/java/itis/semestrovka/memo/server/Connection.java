package itis.semestrovka.memo.server;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.controllers.DataHolder;
import itis.semestrovka.memo.game.Player;
import itis.semestrovka.memo.protocol.Message;
import javafx.application.Platform;
import javafx.print.PageLayout;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Connection implements Runnable {

    public static List<Connection> connections = new ArrayList<>();
    public HashMap<Room, List<Connection>> roomsWithConnections;
    public static Set<Room> rooms = new HashSet<>();
    private Thread thread;
    public BufferedWriter writer;
    public BufferedReader reader;
    private Socket client;
    private String playerName;

    public Connection(Socket client) throws IOException {
        this.client = client;
        this.writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.thread = new Thread(this);
        connections.add(this);
        sendMessages(rooms, this);
        thread.start();
    }

    @Override
    public void run() {
        String message;
        while(client.isConnected()){
            try {
                Player player = null;
                message = reader.readLine();
                String[] strings = message.split(";");
                if(strings.length == 3){
                    Room room = new Room(strings[0], Integer.parseInt(strings[1]));

                    rooms.add(room);

//                    List<Connection> connections1 = new ArrayList<>();
//                    connections1.add(this);
//                    roomsWithConnections.put(room, connections1);

                    setPlayerName(strings[2]);

                    room.setConnection(this);

                    sendMessage(strings[0] + ";" + strings[1]);

                }
                else {
                    for(Room r : rooms){
                        if(r.getName().equals(strings[0])){

//                            roomsWithConnections.get(r).add(this);
                            setPlayerName(strings[1]);

                            r.setConnection(this);
                            System.out.println(r.getConnections());
                        }
                    }
                }



            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessages(Set<Room> rooms, Connection c) throws IOException {
        for(Room r : rooms){
            c.writer.write(Message.createMessage("room", r.getName() + ";" + r.getMaxSize()));
            c.writer.newLine();
            c.writer.flush();
        }
    }

    private void sendMessage(String message) throws IOException {
        for(Connection c : connections){
            c.writer.write(Message.createMessage("room", message));
            c.writer.newLine();
            c.writer.flush();
        }
    }

    public static void sendMessageInRoom(Room room, String s) throws IOException {
        for(Room r : rooms){
            if(r.equals(room)){
                List<Connection> connections = r.getConnections();
                System.out.println(connections);
                for(Connection c : connections){
                    c.writer.write(s);
                    c.writer.newLine();
                    c.writer.flush();
                    System.out.println(43543566);
                }
            }
        }
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}

