package com.example.cafeteria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AdminPageController {

    @FXML
    Button backButton;
    @FXML
    Button changePasswordButton;
    @FXML
    Button newUserButton;
    @FXML
    Button newItemButton;
    @FXML
    Button summaryButton;


    public AdminPageController() throws SQLException {
    }

    // FOR RESET BUTTON, TO ERESE DATA FROM DATABASE IN BALANCE TABLE, ERASING ORDERS (BUT SAVING TO FILE FIRST)
    public void reset() throws SQLException {
    //GETTING CURRENT TIME TO NAME SAVED FILE
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        String formattedDate = myDateObj.format(myFormatObj);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("are you sure to reset all orders?");
        Optional<ButtonType> resetButton = alert.showAndWait();
        if (resetButton.get() == ButtonType.OK){

            String url = "jdbc:mysql://db4free.net:3306/cafeteria";
            String username = "martinez600";
            Connection connection = DriverManager.getConnection(url, username, "9a18aede");

//SAVING DATABASE TO FILE WITH CURRENT TIME NAME
            PreparedStatement fileStatement = connection.prepareStatement("SELECT Full_name, sum(cost) FROM workers, balance WHERE workers.ID = balance.Worker_ID group by Full_name INTO OUTFILE ?");
            fileStatement.setString(1, "C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.2\\\\Uploads\\\\" + formattedDate);
            fileStatement.executeQuery();

// RESETTING
            PreparedStatement resetStatement = connection.prepareStatement("UPDATE balance SET Amount = 0");
            resetStatement.executeUpdate();
            connection.close();

        }else {
        }


    }

@FXML
public void initialize(){
         //FUNCTION TO GO BACK TO LOGIN, FOR BACK BUTTON
    backButton.setOnAction(event -> {
        ConnectionController.changeScene(event, "login-view.fxml");
    });

    changePasswordButton.setOnAction(event -> {
        ConnectionController.changeScene(event, "changePasswordView.fxml");
    });

    newItemButton.setOnAction(event -> {
        ConnectionController.changeScene(event, "newItemView.fxml");
    });


    newUserButton.setOnAction(event -> {
        ConnectionController.changeScene(event, "newUserView.fxml");
    });

    summaryButton.setOnAction(event -> {
        ConnectionController.changeScene(event, "summaryView.fxml");
    });


}

}
