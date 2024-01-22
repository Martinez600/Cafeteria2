package com.example.cafeteria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

// STARTUP OF APPLICATION, NOTHING TO SEE HERE ;)
// PROGRAM WORKS SLOW DUE TO FREE AND SLOW DATABASE HOST
// CAN'T RESET OR SAVE TO FILE, DUE TO LACK OF PERMISSION OUTSIDE OF MAIN DOMAIN


public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Cafeteria!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}

