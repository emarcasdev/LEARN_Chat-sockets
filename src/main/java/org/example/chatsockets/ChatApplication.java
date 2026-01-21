package org.example.chatsockets;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 812);
        stage.setTitle("Chat - Sockets + Theards");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
