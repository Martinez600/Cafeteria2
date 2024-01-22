package com.example.cafeteria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class NewUserController {


    @FXML
    Button acceptButton;
    @FXML
    TextField newLoginField;
    @FXML
    TextField newPasswordField;
    @FXML
    Button backButton;


//CREATING NEW USER IN DATABASAE
    public void createuser() throws SQLException {
        //GETTING LOGIN AND PASSWORD FROM TEXT FIELDS
        String newLogin = newLoginField.getText();
        String newPassword = newPasswordField.getText();

        if (newLogin != null) {
// CHECKING IF DATABASE CONTAINS USER WITH CHOSEN LOGIN
            String url = "jdbc:mysql://db4free.net:3306/cafeteria";
            String username = "martinez600";
            Connection connection = DriverManager.getConnection(url, username, "9a18aede");

            PreparedStatement checkUserStatement = connection.prepareStatement("SELECT * FROM workers WHERE Full_name = ?");
            checkUserStatement.setString(1, newLogin);
            ResultSet checkUserResultSet = checkUserStatement.executeQuery();


            if (checkUserResultSet.next() || newLogin.equals("Admin")){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("user already exists");
                alert.show();
            }
            // IF USER DOESN'T EXIST IN DATABASE ADD TO DATABASE
            else {
                PreparedStatement resetStatement = connection.prepareStatement("INSERT INTO workers (Full_name, password) VALUES (?,?)");
                resetStatement.setString(1, newLogin);
                resetStatement.setString(2, newPassword);
                resetStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("User created!");
                alert.show();
                newLoginField.setText("");
                newPasswordField.setText("");

            }




        }
        // ALERT WHEN TRYING TO APPLY WITH EMPTY TEXT FIELD
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("insert login first");
            alert.show();
        }

    }


    public void initialize(){
        // GOING BACK TO ADMIN VIEW
        backButton.setOnAction(event -> {
            ConnectionController.changeScene(event, "admin-page-view.fxml");
        });

    }
}
