package itis.semestrovka.memo.game;

import itis.semestrovka.memo.server.Connection;

import java.net.Socket;

public class Player {
    String playerName;
    Connection connection;

    public Player(String playerName, Connection connection) {
        this.playerName = playerName;
        this.connection = connection;
    }

    public String getPlayerName() {
        return playerName;
    }
}
