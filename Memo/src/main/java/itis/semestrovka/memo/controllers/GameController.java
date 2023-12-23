package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.client.ClientApplication;
import itis.semestrovka.memo.game.Board;
import itis.semestrovka.memo.game.Cell;
import itis.semestrovka.memo.listener.GameProcessListener;
import itis.semestrovka.memo.protocol.Message;
import itis.semestrovka.memo.server.Room;
import itis.semestrovka.memo.client.Client;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private GridPane gameMatrix;
    @FXML
    private Label you;
    @FXML
    private Label game;
    @FXML
    private  Label info;
    @FXML
    private  Label mark;
    private static Room room;
    private static Client client;
    public static Board board;
    private int size = 0;
    public static Cell firstCard;
    public static boolean flag = true;
    public static boolean state = true;
    public static Cell secondCard;
    private static Parent root;
    private static GameProcessListener gameProcessListener;
    public static VBox vBox;

    public static void addInfo(String newInfo, Label info, GridPane gameMatrix) {
        System.out.println("trnbfybytbt");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] information = newInfo.split(";");

                if(information.length > 1){

                    info.setText("  " + information[1]);

                    if(information[0].equals(client.getPlayerName())){

                        System.out.println(client.getPlayerName());

                        gameMatrix.setDisable(false);
                        state = true;
                    }
                    else {
                        gameMatrix.setDisable(true);
                        state = false;
                    }
                }
                else{
                    info.setText(newInfo);
                }

            }
        });
    }
    public static void addBoard(Board newBoard, GridPane gameMatrix) {
            board = newBoard;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(gameMatrix.getChildren().isEmpty()){
                        for (int i = 0; i < board.getBoardRow(); i++) {
                            for (int j = 0; j < board.getBoardCol(); j++) {
                                FileInputStream inputStream = null;
                                try {
                                    inputStream = new FileInputStream("flower.png");
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                Image image = new Image(inputStream);
                                ImageView imageView = new ImageView(image);
                                imageView.setFitHeight(90);
                                imageView.setFitWidth(90);
                                imageView.setUserData(i + "," + j);
                                imageView.setOnMouseClicked(event -> {
                                    try {
                                        cardListener(event, gameMatrix);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                gameMatrix.add(imageView, i, j);
                            }
                        }
                    }
                }
            });
    }

    public static void addMark(String mess, Label mark) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int x = Integer.parseInt(mess) + Integer.parseInt(mark.getText());
                mark.setText(x + "");
                client.setMarks(x);
            }
        });
    }

    public static void getFinal(String mes, Label info) {

        gameProcessListener.interrupt();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] strings = mes.split(";");

                if(strings.length > 2){
                    if(strings[1].equals(client.getPlayerName()) || strings[2].equals(client.getPlayerName())){
                        info.setText(" Ничья");
                    }
                    else {
                        info.setText(" " + strings[1] + " и " + strings[2] + " выиграли");
                    }
                }
                if(strings.length == 2){
                    if(strings[1].equals(client.getPlayerName())){
                        info.setText(" Вы выиграли");
                    }
                    else{
                        info.setText(" " + strings[1] +  " выиграл");
                    }

                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            Platform.runLater(() -> {
                    if(board != null) {
                        for (int i = 0; i < board.getBoardRow(); i++) {
                            for (int j = 0; j < board.getBoardCol(); j++) {
                                FileInputStream inputStream = null;
                                try {
                                    inputStream = new FileInputStream("flower.png");
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                Image image = new Image(inputStream);
                                ImageView imageView = new ImageView(image);
                                imageView.setFitHeight(90);
                                imageView.setFitWidth(90);
                                imageView.setUserData(i + "," + j);
                                imageView.setOnMouseClicked(event -> {
                                    try {
                                        cardListener(event, gameMatrix);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                gameMatrix.add(imageView, i, j);
                            }
                        }
                        gameMatrix.setDisable(true);
                    }

            });
    }

    public static void cardListener(MouseEvent event, GridPane gameMatrix) throws FileNotFoundException, InterruptedException {
        Node sourceComponent = (Node) event.getSource();
        String rowAndColomn = (String) sourceComponent.getUserData();

        int rowSelected = Integer.parseInt(rowAndColomn.split(",")[0]);
        int colSelected = Integer.parseInt(rowAndColomn.split(",")[1]);

        String image = board.board[rowSelected][colSelected].value;

        FileInputStream inputStream = new FileInputStream(image + ".png");
            Image selectedImage = new Image(inputStream);
            ((ImageView)sourceComponent).setImage(selectedImage);
            System.out.println(0);
            checkOfMatchingPair(rowSelected, colSelected, gameMatrix);
    }

    public static void openCardFromPlayers(String rowAndColomn, GridPane gameMatrix) throws FileNotFoundException, InterruptedException {

        int rowSelected = Integer.parseInt(rowAndColomn.split(",")[0]);
        int colSelected = Integer.parseInt(rowAndColomn.split(",")[1]);

        String image = board.board[rowSelected][colSelected].value;

        FileInputStream inputStream = new FileInputStream(image + ".png");
        Image selectedImage = new Image(inputStream);

        int index = (rowSelected * board.getBoardCol()) + colSelected;
        ((ImageView)gameMatrix.getChildren().get(index)).setImage(selectedImage);
        System.out.println(0);
        checkOfMatchingPair(rowSelected, colSelected, gameMatrix);

    }

    public static void checkOfMatchingPair(int row, int col, GridPane gameMatrix) throws FileNotFoundException, InterruptedException {
        String m = "0";
        if(firstCard == null){
            if(!board.board[row][col].wasGuessed){
                firstCard = board.board[row][col];
            }
        }else if(secondCard ==null){
            if(!board.board[row][col].wasGuessed){

                secondCard = board.board[row][col];


                if(firstCard.value.equals(secondCard.value)){
                    //matching pair
                    board.board[firstCard.row][firstCard.col].wasGuessed = true;
                    board.board[secondCard.row][secondCard.col].wasGuessed = true;

                    m = "1";
                } else {

                    int indexFirstCardInList = (firstCard.row * board.getBoardCol()) + firstCard.col;
                    Image questionImage = new Image(new FileInputStream("flower.png"));int indexSecondCardInList = (secondCard.row * board.getBoardCol()) + secondCard.col;

                    PauseTransition pause1 = new PauseTransition(Duration.seconds(2));

                    gameMatrix.setDisable(true);


                    pause1.setOnFinished(event -> {
                        ((ImageView)gameMatrix.getChildren().get(indexFirstCardInList)).setImage(questionImage);
                        ((ImageView)gameMatrix.getChildren().get(indexSecondCardInList)).setImage(questionImage);

                    });
                    pause1.play();

                }

                if(state){
                    System.out.println(111111111111111111L);
                    client.sendMessage(Message.createMessageToRoom("openCards", room.getName(),
                            firstCard.row + "," + firstCard.col + ";" + secondCard.row + "," + secondCard.col));
                    client.sendMessage(Message.createMessageToRoom("mark", room.getName(), client.getPlayerName() + ";" + m));
                    client.sendMessage(Message.createMessageToRoom("priority", room.getName(), client.getPlayerName()));
                }

                firstCard = null;
                secondCard = null;

            }
        }
    }


    public void setClient(Client client) {
        this.client = client;
        you.setText("Мой ник : " + client.getPlayerName());

        System.out.println("Мой ник : " + client.getPlayerName());

        gameProcessListener = new GameProcessListener(info, client.getBufferedReader(), gameMatrix, mark, vBox);
        gameProcessListener.start();
    }

    public void setRoom(Room room) {
        this.room = room;
        game.setText(" Комната : " + room.getName());
    }

    public static void setBoard(Board newBoard) {
        board = newBoard;
    }


    public void setVbox(VBox vboxMessages) {
        vBox = vboxMessages;
    }
}
