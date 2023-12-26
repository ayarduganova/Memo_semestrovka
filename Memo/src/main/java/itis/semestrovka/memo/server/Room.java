package itis.semestrovka.memo.server;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.controllers.GameController;
import itis.semestrovka.memo.game.Player;
import itis.semestrovka.memo.listener.RoomListener;
import itis.semestrovka.memo.protocol.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Room{

    private String name;
    private Integer maxSize;
    public static Set<Room> rooms = new HashSet<>();
    private List<Connection> connections = new ArrayList<>();
    private HashMap<String, Integer> marks = new HashMap<>();
    private int count = 0;
    private int maxCount;

    public HashMap<String, Integer> getMarks() {
        return marks;
    }

    public void setMarks(String s, Integer k) {
        if(marks.containsKey(s)){
            int x = marks.get(s);
            marks.remove(s);
            marks.put(s, (k + x));
        }
        else{
            this.marks.put(s, k);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count += count;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    private List<String> messages = new ArrayList<>();
    private GameController gameController;

    public Room(String name, Integer maxSize) {
        this.name = name;
        this.maxSize = maxSize;

        if(maxSize == 2){
            this.maxCount = 18;
        } else if (maxSize == 3) {
            this.maxCount = 42;
        }
        else {
            this.maxCount = 52;
        }

        rooms.add(this);

    }

    public void createMessages(){
        for(Connection c : connections){
            messages.add(c.getPlayerName() + ";" + c.getPlayerName() + " делает ход");
        }
    }
    private String receiveMessage(){
        return "";
    }
    public void sendMessage(String s) throws IOException {
        for(Connection c : connections){
            c.writer.write(s);
            c.writer.newLine();
            c.writer.flush();
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

    public int getMaxCount() {
        return maxCount;
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
            sendMessage(Message.createMessageToRoom("info", name,
                    "state"));
            String message = Message.createMessageToRoom("info", name,
                    " Недостаточное количество участников : " + connections.size() + " / " + maxSize);
            sendMessage(message);
        }
        else {
            String message = Message.createMessageToRoom("info", name,
                    " Начинаем");
            Connection.sendMessageToDelete(message);
            createMessages();
            System.out.println(messages);
            sendMessage(Message.createMessage("priority", messages.get(0)));
        }
    }
}
