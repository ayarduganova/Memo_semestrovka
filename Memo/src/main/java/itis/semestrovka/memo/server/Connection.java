package itis.semestrovka.memo.server;

import itis.semestrovka.memo.client.MemoController;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection {

    public static List<Connection> connections = new ArrayList<>();
    private Thread thread;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket client;
    private String clentName;
    private Room room;

    public Connection(Socket client, BufferedReader reader, Room r) throws IOException {
        this.client = client;
        this.room = r;
        this.writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        this.reader = reader;
        this.clentName = this.reader.readLine();
        connections.add(this);
    }

    public void receiveMessage(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while(client.isConnected()){
                    try {
                        message = reader.readLine();
                        MemoController.addLabel(message, vBox);
                        System.out.println(message);
                        sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void sendMessage(String message) throws IOException {
        for(Connection c : connections ){
            c.writer.write(message);
            c.writer.newLine();
            c.writer.flush();
        }

    }
}
