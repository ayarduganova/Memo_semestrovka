package itis.semestrovka.memo.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Runnable {

    public static List<Connection> connections = new ArrayList<>();
    public static List<String> rooms = new ArrayList<>();
    private Thread thread;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket client;

    public Connection(Socket client) throws IOException {
        this.client = client;
        this.writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.thread = new Thread(this);
        connections.add(this);
        thread.start();
    }

    @Override
    public void run() {
        String message;
        String message1;
        try {
            sendMessages(rooms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(client.isConnected()){
            try {
                message = reader.readLine();
                rooms.add(message);
                message1 = reader.readLine();
                rooms.add(message1);

                sendMessage(message);
                sendMessage(message1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessages(List<String> messages) throws IOException {
        for(Connection c : connections){
                for(String s : messages){
                    c.writer.write(s);
                    c.writer.newLine();
                    c.writer.flush();
                }
        }

    }

    private void sendMessage(String message) throws IOException {
        for(Connection c : connections){
            c.writer.write(message);
            c.writer.newLine();
            c.writer.flush();

        }
    }
}

