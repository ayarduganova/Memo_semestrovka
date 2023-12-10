package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.server.Room;

import java.util.HashSet;
import java.util.Set;

public class DataHolder {
    public static Room room;
    public static Client client;

    public static Room getRoom() {
        return room;
    }

    public static void setRoom(Room room) {
        DataHolder.room = room;
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        DataHolder.client = client;
        room.setClient(client);
    }
}
