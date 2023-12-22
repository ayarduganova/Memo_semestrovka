package itis.semestrovka.memo.listener;

import itis.semestrovka.memo.controllers.EnterToRoomController;
import itis.semestrovka.memo.controllers.GameController;
import itis.semestrovka.memo.game.Board;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameProcessListener extends Thread{
    private Label info;
    private BufferedReader br;
    private GridPane gameMatrix;
    private Label mark;

    public GameProcessListener(Label info, BufferedReader br, GridPane gameMatrix, Label mark){
        this.info = info;
        this.br = br;
        this.gameMatrix = gameMatrix;
        this.mark = mark;
    }



    @Override
    public void run() {
        String mes;
        while (true) {
            try {
                mes = this.br.readLine();

                System.out.println("получили в game : " + mes);

                if (mes.contains("type=info")) {

                    String mess = mes.substring(10);

                    System.out.println(mess);

                    GameController.addInfo(mess, info, gameMatrix);

                } else if (mes.contains("type=board")) {

                  Board board = new Board(mes.substring(11));
                    System.out.println(mes.substring(11));

                  GameController.addBoard(board, gameMatrix);

                    System.out.println(4356557);

                } else if (mes.contains("type=priority")) {

                    String[] mess = mes.split(";");

                    System.out.println(mess);

                    GameController.addInfo(mess[1] + ";" + mess[2], info, gameMatrix);


                }
                else if (mes.contains("type=mark")) {

                    String[] mess = mes.split(";");

                    GameController.addMark(mess[1], mark);

                }
                else if (mes.contains("type=openCards")) {

                    String[] mess = mes.split(";");
                    System.out.println(mess);

                    try {
                        GameController.state = false;
                        GameController.openCardFromPlayers(mess[1], gameMatrix);
                        GameController.openCardFromPlayers(mess[2], gameMatrix);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                else if (mes.contains("type=final")) {

                    GameController.getFinal(mes, info);
                    try {
                        sleep(10000);
                        interrupt();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
