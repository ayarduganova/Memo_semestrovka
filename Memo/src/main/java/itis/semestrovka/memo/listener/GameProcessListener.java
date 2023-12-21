package itis.semestrovka.memo.listener;

import itis.semestrovka.memo.controllers.EnterToRoomController;
import itis.semestrovka.memo.controllers.GameController;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;

public class GameProcessListener extends Thread{
    private Label info;
    private BufferedReader br;

    public GameProcessListener(Label info, BufferedReader br){
        this.info = info;
        this.br = br;
    }

    @Override
    public void run() {
        String message;
        while (true) {
            try {
                message = this.br.readLine();

                System.out.println("получили в game : " + message);

                if (message.contains("type=info")) {

                    String mes = message.substring(10);

                    System.out.println(mes);

                    GameController.addInfo(mes, info);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
