package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.game.Board;
import itis.semestrovka.memo.game.Cell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
//    private Room room;
//    private Client client;
//    @FXML
//    private VBox vbox;
    @FXML
    private GridPane gameMatrix;

    private Board board = new Board();
    private Cell firstCard = null;
    private Cell secondCard = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            board.populateMatrix();

            for(int i = 0; i < 6; i++){
                for(int j = 0; j < 6; j++){
                    FileInputStream inputStream = new FileInputStream("flower.png");
                    Image image = new Image(inputStream);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(90);
                    imageView.setFitWidth(90);
                    imageView.setUserData(i + "," + j);
                    imageView.setOnMouseClicked(event -> {
                        try {
                            cardListener(event);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    gameMatrix.add(imageView, i, j);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        this.room = DataHolder.getRoom();
//        System.out.println(room.getName() + "  " + room.getClients());
//        this.client = DataHolder.getClient();
//
//        boolean flag = false;
//
//        Set<Client> clients = room.getClients();
//        if(clients != null){
//            Platform.runLater(() -> {
//                for (Client cl : clients) {
//                    vbox.getChildren().add(new Text(cl.getPlayerName()));
//                }
//            });
//        }
    }

    public void cardListener(MouseEvent event) throws FileNotFoundException {
        Node sourceComponent = (Node) event.getSource();
        String rowAndColomn = (String) sourceComponent.getUserData();

        int rowSelected = Integer.parseInt(rowAndColomn.split(",")[0]);
        int colSelected = Integer.parseInt(rowAndColomn.split(",")[1]);

        String image = board.board[rowSelected][colSelected].value;

        FileInputStream inputStream = new FileInputStream(image + ".png");
        Image selectedImage = new Image(inputStream);
        ((ImageView)sourceComponent).setImage(selectedImage);
        checkOfMatchingPair(rowSelected, colSelected);
    }

    public void checkOfMatchingPair(int row, int col) throws FileNotFoundException {
        if (firstCard == null){
            firstCard = board.board[row][col];
        } else if (secondCard == null) {
            secondCard = board.board[row][col];
        } else {
            if(firstCard.value.equals(secondCard.value)){
                board.board[firstCard.row][firstCard.col].wasGuessed = true;
                board.board[secondCard.row][secondCard.col].wasGuessed = true;
            } else {
                int indexFirstCardInList = (firstCard.row * 6) + firstCard.col;
                ((ImageView)gameMatrix.getChildren().get(indexFirstCardInList)).
                        setImage(new Image(new FileInputStream("flower.png")));
                int indexSecondCardInList = (secondCard.row * 6) + secondCard.col;
                ((ImageView)gameMatrix.getChildren().get(indexSecondCardInList)).
                        setImage(new Image(new FileInputStream("flower.png")));
            }
            firstCard = board.board[row][col];
            secondCard = null;
        }
    }
}
