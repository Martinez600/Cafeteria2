package com.example.cafeteria;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class LoginController {


    private Stage stage;
    private Scene scene;


    @FXML
    private TextField loginText;
    @FXML
    private PasswordField passwordText;
    @FXML
    Button logInButton;



    static String login;
    static String password;


    public LoginController() throws SQLException {
    }

    public void initialize() throws SQLException{

        logInButton.setOnAction(event -> {
            try {
                ConnectionController.connecting(event, loginText.getText(), passwordText.getText());
            } catch (SQLException var3) {
                throw new RuntimeException(var3);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        });


    }

}






