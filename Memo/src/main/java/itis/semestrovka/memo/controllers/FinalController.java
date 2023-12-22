package itis.semestrovka.memo.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FinalController {
    @FXML
    Label end;


    public void setEnd(String s) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                end.setText(s);
            }
        });
    }
}
