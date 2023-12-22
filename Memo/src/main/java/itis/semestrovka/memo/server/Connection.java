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
                System.out.println(message);
                if(message.contains("type=room")){

                    Room room = new Room(strings[1], Integer.parseInt(strings[2]));

                    rooms.add(room);

                    setPlayerName(strings[3]);

                    room.setConnection(this);

                    sendMessage(strings[1] + ";" + strings[2]);

                }
                else if (message.contains("type=enter")) {

                    for(Room r : rooms){
                        if(r.getName().equals(strings[1])){

//                            roomsWithConnections.get(r).add(this);
                            setPlayerName(strings[2]);

                            r.setConnection(this);
                            System.out.println(r.getConnections());
                        }
                    }

                }
                else if (message.contains("type=mark")) {

                    writer.write(Message.createMessage("mark", strings[3]));
                    writer.newLine();
                    writer.flush();

                    for(Room r : rooms){
                        if(r.getName().equals(strings[1].substring(5))){
                            r.setCount(Integer.parseInt(strings[3]));
                            r.setMarks(strings[2], Integer.parseInt(strings[3]));
                            if(r.getCount() == r.getMaxCount()){
                                List<String> keys = new ArrayList<String>(r.getMarks().keySet());
                                int maxValue = 0;
                                String winner = "";
                                for(int i = 0; i < keys.size(); i++) {
                                    String key = keys.get(i);
                                    Integer value = r.getMarks().get(key);
                                    if(value > maxValue){
                                        winner = key;
                                    }
                                    if (value == maxValue){
                                        winner += ";" + key;
                                    }
                                }
                                    r.sendMessage(Message.createMessage("final", winner));

                            }
                        }
                    }

                }else if (message.contains("type=openCards")) {

                    for(Room r : rooms){
                        if(r.getName().equals(strings[1].substring(5))){
                            boolean flag = true;
                            while(flag){
                                    r.sendMessage(Message.createMessage("openCards", strings[2] + ";" + strings[3]));
                                    flag = false;
                            }
                        }
                    }

                } else if (message.contains("type=priority")) {

                    for (Room r : rooms) {
                        if (r.getName().equals(strings[1].substring(5))) {
                            int i = 0;
                            for(String s : r.getMessages()){
                                if(s.split(";")[0].equals(strings[2])){
                                    if(i + 1 == r.getMessages().size()){
                                        i = -1;
                                    }
                                    System.out.println("Этто я" + i);
                                    r.sendMessage(Message.createMessage("priority", r.getMessages().get(i + 1)));
                                }
                                i++;
                            }
                        }
                    }
                }   else {
                    System.out.println("ураураурауар");
                    String m = "";
                    for(int i = 2; i < strings.length; i++){
                        m += (strings[i] + ";");
                    }
                    for(Room r : rooms){
                        if(r.getName().equals(strings[1].substring(5))){
                            boolean flag = true;
                            while(flag){
                                if(r.getMaxSize() == r.getConnections().size()){
                                    r.sendMessage(Message.createMessage("board", m));
                                    flag = false;
                                }
                            }
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

