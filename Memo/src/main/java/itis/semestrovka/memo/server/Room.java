package itis.semestrovka.memo.server;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.controllers.GameController;
import itis.semestrovka.memo.game.Player;
import itis.semestrovka.memo.protocol.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Room implements Runnable{

    private String name;
    private Integer maxSize;
    public static Set<Room> rooms = new HashSet<>();
    private List<Connection> connections = new ArrayList<>();
    private Thread thread;
    private GameController gameController;

    public Room(String name, Integer maxSize) {
        this.name = name;
        this.maxSize = maxSize;

        rooms.add(this);

        this.thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
//        boolean flag = true;
//        System.out.println(1114576);
//        while (flag){
//            if(connections.size() != maxSize) {
//                try {
//                    System.out.println(5465767);
//                    String message = Message.createMessage("info",
//                            "Недостаточное количество участников : " + connections.size() + " / " + maxSize);
//
//                    sendMessage(message);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            else {
//                flag = false;
//            }
//        }
    }

    private String receiveMessage(){
        return "";
    }
    private void sendMessage(String s) throws IOException {
        System.out.println(connections);
        System.out.println(s);
        for(Connection c : connections){
            c.writer.write(s);
            c.writer.newLine();
            c.writer.flush();
            System.out.println(43543566);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        Room room = (Room) obj;

        return getName().equals(room.getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public static Set<Room> getRooms() {
        return rooms;
    }
    public static void getRoomByName(String name){
        for(Room r : rooms){
            if(r.getName().equals(name)){
                System.out.println("ehdfvefsv");;
            }
        }
        System.out.println("dfvbdv");
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnection(Connection connection) throws IOException {
        this.connections.add(connection);
        if(connections.size() != maxSize) {
            System.out.println(5465767);
            String message = Message.createMessage("info",
                    "Недостаточное количество участников : " + connections.size() + " / " + maxSize);
            sendMessage(message);
        }
        else {
            String message = Message.createMessage("info",
                    "Начинаем");
            sendMessage(message);
        }
    }
}
