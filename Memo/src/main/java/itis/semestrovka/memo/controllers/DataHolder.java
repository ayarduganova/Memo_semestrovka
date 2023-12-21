package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.server.Room;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataHolder {
    public static HashMap<Room, String> room = new HashMap<>();
    public static Client client;

    public static void setRoom(Room room, String nameClient) {
        DataHolder.room.put(room, nameClient);
    }

    public static HashMap<Room, String> getRoom() {
        return room;
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        DataHolder.client = client;
    }
}
