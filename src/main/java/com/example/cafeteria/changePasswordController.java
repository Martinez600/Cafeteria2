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

public class changePasswordController {

    @FXML
    TextField usernameText;
    @FXML
    TextField newPassword1TF;
    @FXML
    TextField newPassword2TF;
    @FXML
    Button backButton;


    private Stage stage;
    private Scene scene;




    //CHANGING PASSWORD IN DATABASE FOR EXISTING USER
    public void changePassword() throws SQLException {
//CHECKING CONDITION IF TEXT FIELDS ARE NOT EMPTY
        if (usernameText.getText() != null && newPassword1TF.getText() != null && newPassword2TF.getText() != null){
//GETTING USERNAME AND NEW PASSWORD FROM TEXT FIELDS
        String userNameToChange = usernameText.getText();
        String password1 = newPassword1TF.getText();
        String password2 = newPassword2TF.getText();
//GETTING DATA FROM DATABASE FROM USERS TABLE
            String url = "jdbc:mysql://db4free.net:3306/cafeteria";
            String username = "martinez600";
            Connection connection = DriverManager.getConnection(url, username, "9a18aede");

        PreparedStatement checkUserStatement = connection.prepareStatement("SELECT * FROM workers WHERE Full_name = ?");
        checkUserStatement.setString(1, userNameToChange);
        ResultSet checkUserResultSet = checkUserStatement.executeQuery();


// CHECKING IS THERE A USER IN DATABASE WITH PARTICULAR USERNAME
        if (checkUserResultSet.next()){
            // CKECKING IF PASSWORDS FROM BOTH TEXT FIELDS MATCH
            if(password1.equals(password2) && password1 != null && password2 != null) {
//CHANGING PASSWORD IN DATABASE
                PreparedStatement changePasswordSTMT = connection.prepareStatement("Update workers SET password = ? where Full_name = ?");
                changePasswordSTMT.setString(1, password2);
                changePasswordSTMT.setString(2, userNameToChange);
                changePasswordSTMT.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("password changed");
                alert.show();
                usernameText.setText("");
                newPassword1TF.setText("");
                newPassword2TF.setText("");
//ALERT IF PASSWORDS FROM TEXT FIELDS DOESN'T MATCH
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("passwords don't match");
                alert.show();

            }
            //ALERT WHEN USER DOESN'T EXIST IN DB
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("user not found");
            alert.show();

        }
//ALERT WHEN TEXT FIELDS ARE EMPTY
    }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("fill text fields");
            alert.show();
        }

    }


    public void initialize(){
        // GOING BACK TO ADMIN VIEW
        backButton.setOnAction(event -> {
            ConnectionController.changeScene(event,"admin-page-view.fxml");
        });

    }


}
