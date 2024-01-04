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

    private Stage stage;
    private Scene scene;


    public AdminPageController() throws SQLException {
    }

    // FUNCTION TO GO BACK TO LOGIN, FOR BACK BUTTON
    public void switchToLoginView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }



    // FUNCTION TO SWITCH TO CREATING NEW USER VIEW, FOR NEW USER BUTTON
    public void switchToNewUser(ActionEvent event) throws IOException {



        Parent root = FXMLLoader.load(getClass().getResource("newUserView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    // FUNCTION TO SWITCH TO CREATING NEW ITEM VIEW, FOR NEW ITEM BUTTON
    public void switchToNewItem(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("newItemView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // // FUNCTION TO SWITCH TO CHANGING USER PASSWORD VIEW, FOR CHANGE PASSWORD BUTTON
    public void switchToPasswordChange(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("changePasswordView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
            Connection connection = DriverManager.getConnection(url, username, "shelbyGT500#");

//SAVING DATABASE TO FILE WITH CURRENT TIME NAME
            PreparedStatement fileStatement = connection.prepareStatement("SELECT Full_name, sum(cost) FROM workers, balance WHERE workers.ID = balance.Worker_ID group by Full_name INTO OUTFILE ?");
            fileStatement.setString(1, "C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.2\\\\Uploads\\\\" + formattedDate);
            fileStatement.executeQuery();

// RESETTING
            PreparedStatement resetStatement = connection.prepareStatement("UPDATE balance SET Amount = 0");
            resetStatement.executeUpdate();
            connection.close();

        }else {return;}



    }
// SWITCHING TO SUMMARY VIEW FOR SUM OF ORDERS PER CUSTOMER
public void switchToSummaryView(ActionEvent event) throws SQLException, IOException {



    Parent root = FXMLLoader.load(getClass().getResource("summaryView.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();

}

}
