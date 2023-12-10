package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.server.Room;

import java.util.HashSet;
import java.util.Set;

public class Game {
    public static Set<Client> clients = new HashSet<>();
    private Room room;

    public Game(Room room) {
        this.room = room;
    }

    public static Set<Client> getClients() {
        return clients;
    }

    public static void setClient(Client client) {
        clients.add(client);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
