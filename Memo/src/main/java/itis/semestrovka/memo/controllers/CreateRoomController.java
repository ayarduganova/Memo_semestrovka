package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.client.Client;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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

public class CreateRoomController implements Initializable {

    @FXML
    private Button create;
    @FXML
    private TextField playerName;
    @FXML
    private ChoiceBox<Integer> choiceNumber;
    @FXML
    private TextField roomName;
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
                String messageToSend = roomName.getText();
                Integer number = choiceNumber.getValue();
                String player = playerName.getText();
                Room room = new Room(messageToSend, number);
                if (!messageToSend.isEmpty() && !player.isEmpty()) {
                    System.out.println("dtbgfbrfhtghgrtgtrgverferrrrrrrrrrrrrrrrrrrg");
                    client.sendRoom(room);
                    client.sendMessageToServer(player);
                    roomName.clear();
                    playerName.clear();
                }
            }
        });
    }

}
