package itis.semestrovka.memo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NewServer {

    private ServerSocket server;

    public NewServer(ServerSocket server) {
        this.server = server;
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        NewServer server = new NewServer(serverSocket);

        while(!serverSocket.isClosed()){
            Socket client = serverSocket.accept();
            new Connection(client);
        }
    }


}
