package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.game.Board;
import itis.semestrovka.memo.game.Cell;
import itis.semestrovka.memo.listener.GameProcessListener;
import itis.semestrovka.memo.server.Room;
import itis.semestrovka.memo.client.Client;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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
    private Room room;
    private Client client;
    private Board board = new Board();
    private int size = 0;
    private Cell firstCard;
    private Cell secondCard;

    public static void addInfo(String newInfo, Label info) {
        System.out.println("trnbfybytbt");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                info.setText(newInfo);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            Platform.runLater(() -> {

                board.setBoardSize(room.getMaxSize());

                board.populateMatrix();

                for(int i = 0; i < board.getBoardRow() ; i++){
                    for(int j = 0; j < board.getBoardCol(); j++){
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
                                cardListener(event);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        gameMatrix.add(imageView, i, j);
                    }
                }
            });
    }

    public void cardListener(MouseEvent event) throws FileNotFoundException, InterruptedException {
        Node sourceComponent = (Node) event.getSource();
        String rowAndColomn = (String) sourceComponent.getUserData();

        int rowSelected = Integer.parseInt(rowAndColomn.split(",")[0]);
        int colSelected = Integer.parseInt(rowAndColomn.split(",")[1]);

        String image = board.board[rowSelected][colSelected].value;

        FileInputStream inputStream = new FileInputStream(image + ".png");
            Image selectedImage = new Image(inputStream);
            ((ImageView)sourceComponent).setImage(selectedImage);
            System.out.println(0);
            checkOfMatchingPair(rowSelected, colSelected);
    }

    public void checkOfMatchingPair(int row, int col) throws FileNotFoundException, InterruptedException {
        if(firstCard == null){
            firstCard = board.board[row][col];
        }else if(secondCard ==null){
            secondCard = board.board[row][col];

            if(firstCard.value.equals(secondCard.value)){
                //matching pair
                board.board[firstCard.row][firstCard.col].wasGuessed = true;
                board.board[secondCard.row][secondCard.col].wasGuessed = true;
            } else {

                board.board[firstCard.row][firstCard.col].wasGuessed = true;
                board.board[secondCard.row][secondCard.col].wasGuessed = true;

                int indexFirstCardInList = (firstCard.row * board.getBoardCol()) + firstCard.col;
                Image questionImage = new Image(new FileInputStream("flower.png"));
                int indexSecondCardInList = (secondCard.row * board.getBoardCol()) + secondCard.col;

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                gameMatrix.setDisable(true);
                pause.setOnFinished(event -> {
                    ((ImageView)gameMatrix.getChildren().get(indexFirstCardInList)).setImage(questionImage);
                    ((ImageView)gameMatrix.getChildren().get(indexSecondCardInList)).setImage(questionImage);
                    gameMatrix.setDisable(false);
                });
                pause.play();
            }
                firstCard = null;
                secondCard = null;
        }
    }


    public void setClient(Client client) {
        this.client = client;
        you.setText("Мой ник : " + client.getPlayerName());

        GameProcessListener gameProcessListener = new GameProcessListener(info, client.getBufferedReader());
        gameProcessListener.start();
    }

    public void setRoom(Room room) {
        this.room = room;
        game.setText(" Комната : " + room.getName());
    }
}
