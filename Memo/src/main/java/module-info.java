module itis.semestrovka.memo {
    requires javafx.controls;
    requires javafx.fxml;


    opens itis.semestrovka.memo to javafx.fxml;
    exports itis.semestrovka.memo;
    exports itis.semestrovka.memo.client;
    opens itis.semestrovka.memo.client to javafx.fxml;
    exports itis.semestrovka.memo.server;
    opens itis.semestrovka.memo.server to javafx.fxml;
}