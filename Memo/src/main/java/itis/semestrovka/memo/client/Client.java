package itis.semestrovka.memo.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String name;
    private String roomName;

    public Client(Socket socket, String name, String roomName) throws IOException {
        this.socket = socket;
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.name = name;
        this.roomName = roomName;
    }

    private void sendMessage() throws IOException {

        this.writer.write(roomName);
        this.writer.newLine();
        this.writer.flush();

        this.writer.write(name);
        this.writer.newLine();
        this.writer.flush();

        Scanner sc = new Scanner(System.in);

        while(socket.isConnected()){
            String message = sc.nextLine();
            this.writer.write(name + ":" + message);
            this.writer.newLine();
            this.writer.flush();

        }    }

    private void checkMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;

                while (socket.isConnected()){
                    try {
                        message = reader.readLine();
                        System.out.println(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public String getName() {
        return name;
    }
}

