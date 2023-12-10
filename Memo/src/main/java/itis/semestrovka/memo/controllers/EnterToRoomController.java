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

import static itis.semestrovka.memo.controllers.DataHolder.room;

public class EnterToRoomController implements Initializable {
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private TextField roomName;
    @FXML
    private TextField playerName;
    private Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static List<Room> rooms = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            client = new Client(new Socket("localhost", 1234));
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating Client ... ");
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        client.receiveMessageFromServer(vbox_messages);

    }

    public static void addLabel(String messageFromServer, String roomSize, VBox vBox){
        Room room = new Room(messageFromServer, Integer.parseInt(roomSize));
        System.out.println(rooms);
        if(!rooms.contains(room)){
            rooms.add(room);
            System.out.println(rooms);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(10, 10, 10, 10));

            Text text = new Text("Название: " + room.getName());
            Text text1 = new Text("  Кол-во человек: " + room.getMaxSize().toString());

            TextFlow textFlow = new TextFlow(text, text1);

            textFlow.setStyle(
                    "-fx-background-color: #b86c88;" +
                    "-fx-font-size: 20px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(textFlow);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                }
            });
        }
    }

    public void switchToGame(ActionEvent event) throws IOException {
        for(Room r : rooms){
            if(roomName.getText().equals(r.getName())){

                client.setPlayerName(playerName.getText());


                DataHolder.setRoom(r);
                DataHolder.setClient(client);

//                Room.setClient(client);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/itis/semestrovka/memo/game.fxml"));
                root = loader.load();
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);

                GameController controller = loader.getController();

                stage.show();
            }
        }
    }

}
