package itis.semestrovka.memo.listener;

import itis.semestrovka.memo.controllers.EnterToRoomController;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;

public class RoomListener extends Thread {
    private VBox vBox;
    private BufferedReader br;

    private boolean state;

    public RoomListener(VBox vBox, BufferedReader br) {
        this.vBox = vBox;
        this.br = br;
        this.state = true;
    }

    @Override
    public void run() {
        String message;
        while (state) {
            try {
//                try {
////                    sleep(8000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                message = this.br.readLine();

                System.out.println("получили : " + message);

                if(message.contains("type=room")){

                    String room = message.substring(10);

                    System.out.println(room);

                    String[] strings = room.split(";");

                    EnterToRoomController.addLabel(strings[0], strings[1], vBox);

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changeState() {
        this.state = false;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}