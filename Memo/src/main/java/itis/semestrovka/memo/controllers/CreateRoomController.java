package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.client.ClientApplication;
import itis.semestrovka.memo.game.Board;
import itis.semestrovka.memo.game.Cell;
import itis.semestrovka.memo.protocol.Message;
import itis.semestrovka.memo.server.Room;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class CreateRoomController implements Initializable {

    @FXML
    private Button create;
    @FXML
    private TextField playerName;
    @FXML
    private ChoiceBox<Integer> choiceNumber;
    @FXML
    private TextField roomName;
    private Parent root;
    private Client client;
    public static List<String> messages = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            client = new Client(new Socket("localhost", 1234));
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating Client ... ");
        }

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String roomNameText = roomName.getText();
                Integer number = choiceNumber.getValue();
                String player = playerName.getText();
                Room room = new Room(roomNameText, number);
                if (!roomNameText.isEmpty() && !player.isEmpty()) {
                    client.sendMessage(Message.createMessage("room", roomNameText + ";" + number + ";" + player));
                    client.setPlayerName(player);

                    System.out.println(roomNameText + ";" + number + ";" + player);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/itis/semestrovka/memo/game.fxml"));

                        root = loader.load();

                        GameController gameController = loader.getController();
                        gameController.setClient(client);
                        gameController.setRoom(room);

                        Board board = new Board();
                        board.setBoardSize(room.getMaxSize());
                        Cell[][] newBoard = board.populateMatrix();
                        String s = "";
                        for(int i = 0; i <= newBoard.length - 1; i++){
                            for(int j = 0; j <= newBoard[0].length - 1; j++){
                                s += (newBoard[i][j].value + ";");
                            }
                        }
                        GameController.setBoard(board);
                        client.sendMessage(Message.createMessageToRoom("board", roomNameText, s));
                        Scene scene;
                        int x;
                        int y;
                        boolean isFullScreen = false;
                        if(room.getMaxSize() == 2){
                            x = 800;
                            y = 800;
                            scene = new Scene(root, x, y);
                        } else if (room.getMaxSize() == 3) {
                            x = 1300;
                            y = 800;
                            scene = new Scene(root, x, y);
                        }
                        else{
                            scene = new Scene(root);
                            isFullScreen = true;
                        }
                        ClientApplication.setScene(scene, isFullScreen);

                        roomName.clear();
                        playerName.clear();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

}
