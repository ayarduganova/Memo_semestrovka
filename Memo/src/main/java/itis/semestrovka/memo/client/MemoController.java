package itis.semestrovka.memo.client;

import itis.semestrovka.memo.server.Server;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MemoController implements Initializable {

    @FXML
    private TextField roomName;
    @FXML
    private TextField playerName;
    @FXML
    private VBox listRooms;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Button createOrAdd;

    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        server = new Server();

        listRooms.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scroll.setVvalue((Double) t1);
            }
        });

        server.receiveMessage(listRooms);

        createOrAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String room = roomName.getText();
                if (!room.isEmpty()){

                    try {
                        Client client = new Client(new Socket("localhost", 6666), playerName.getText(), roomName.getText());

                        HBox hbox = new HBox();
                        hbox.setAlignment(Pos.CENTER);
                        hbox.setPadding(new Insets(10, 10, 10, 10));

                        Text text = new Text("Комната: " + room);
                        TextFlow textFlow = new TextFlow(text);

                        textFlow.setPadding(new Insets(5, 10, 5, 10));
                        hbox.getChildren().add(textFlow);
                        listRooms.getChildren().add(hbox);

                        client.sendMessageToServer(playerName.getText());
                        client.sendMessageToServer(roomName.getText());
                        roomName.clear();
                        playerName.clear();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

    }
    public static void addLabel(String room, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        Text text = new Text("Комната: " + room);
        TextFlow textFlow = new TextFlow(text);

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