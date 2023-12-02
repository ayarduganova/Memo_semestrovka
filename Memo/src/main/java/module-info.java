module itis.semestrovka.memo {
    requires javafx.controls;
    requires javafx.fxml;



    exports itis.semestrovka.memo.client;
    opens itis.semestrovka.memo.client to javafx.fxml;

    exports itis.semestrovka.memo.server;
    opens itis.semestrovka.memo.server to javafx.fxml;

    exports itis.semestrovka.memo.controllers;
    opens itis.semestrovka.memo.controllers to javafx.fxml;
}