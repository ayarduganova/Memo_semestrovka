package itis.semestrovka.memo.controllers;

import itis.semestrovka.memo.client.Client;
import itis.semestrovka.memo.client.ClientApplication;
import itis.semestrovka.memo.game.Board;
import itis.semestrovka.memo.listener.GameProcessListener;
import itis.semestrovka.memo.listener.RoomListener;
import itis.semestrovka.memo.protocol.Message;
import itis.semestrovka.memo.server.Connection;
import itis.semestrovka.memo.server.Room;
import javafx.animation.PauseTransition;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.*;


public class EnterToRoomController implements Initializable {
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private TextField roomName;
    @FXML
    private TextField playerName;
    RoomListener roomListener;
    private Client client;
    private Parent root;
    public static List<Room> rooms = new ArrayList<>();
    public static HashMap<String, HBox> hboxes = new HashMap<>();

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
        roomListener = new RoomListener(this.vbox_messages, this.client.getBufferedReader());
        roomListener.start();


//        client.receiveMessageFromServer(vbox_messages);

    }

    public static void addLabel(String messageFromServer, String roomSize, VBox vBox){
        Room r = new Room(messageFromServer, Integer.parseInt(roomSize));
        if(!rooms.contains(r)){
            rooms.add(r);
            System.out.println(rooms);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(10, 10, 10, 10));

            Text text = new Text("Название: " + messageFromServer);
            Text text1 = new Text("  Кол-во человек: " + roomSize);

            TextFlow textFlow = new TextFlow(text, text1);

            textFlow.setStyle(
                    "-fx-background-color: #b86c88;" +
                    "-fx-font-size: 20px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(textFlow);
            hboxes.put(messageFromServer, hBox);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                }
            });
        }
    }
    public static void deleteRoom(String room, VBox vBox){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                List<String> keys = new ArrayList<String>(hboxes.keySet());
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    HBox value;
                    if(room.equals(key)){
                        value = hboxes.get(key);
                        vBox.getChildren().remove(value);
                    }
                }
            }
        });
    }

    public void switchToGame(ActionEvent event) throws IOException {

        roomListener.changeState();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished( actionEvent -> {


            client.setPlayerName(playerName.getText());
            client.sendMessage(Message.createMessage("enter", roomName.getText() + ";" + client.getPlayerName()));


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/itis/semestrovka/memo/game.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            GameController gameController = loader.getController();
            gameController.setVbox(vbox_messages);
            gameController.setClient(client);
            Room room = null;
            for(Room r : rooms){
                if(r.getName().equals(roomName.getText())){
                    room = r;
                    gameController.setRoom(r);
                }
            }

            Scene scene;
            int x;
            int y;
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
            }

            ClientApplication.setScene(scene);

        });
        pause.play();

    }

}
