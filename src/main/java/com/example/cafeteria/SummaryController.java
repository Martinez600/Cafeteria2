package com.example.cafeteria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SummaryController {

    private Stage stage;
    private Scene scene;

    @FXML
    TextArea SummaryArea;
    @FXML
    public void initialize() throws SQLException {
        getSummary();
    }

    public void save() throws SQLException {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        String formattedDate = myDateObj.format(myFormatObj);

        String url = "jdbc:mysql://db4free.net:3306/cafeteria";
        String username = "martinez600";
        Connection connection = DriverManager.getConnection(url, username, "shelbyGT500#");

//SAVING DATABASE TO FILE WITH CURRENT TIME NAME
        PreparedStatement fileStatement = connection.prepareStatement("SELECT Full_name, sum(cost) FROM workers, balance WHERE workers.ID = balance.Worker_ID group by Full_name INTO OUTFILE ?");
        fileStatement.setString(1, "C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.2\\\\Uploads\\\\" + formattedDate);
        fileStatement.executeQuery();

    }


    // GETTING SUMS OF ORDERS FOR PARTICULAR USER FROM DATABASE
    public void getSummary() throws SQLException {

        String url = "jdbc:mysql://db4free.net:3306/cafeteria";
        String username = "martinez600";
        Connection connection = DriverManager.getConnection(url, username, "shelbyGT500#");

        PreparedStatement getSummaryStatement = connection.prepareStatement("SELECT Full_name, sum(cost) FROM workers, balance where workers.ID = balance.Worker_ID group by Full_name");

        ResultSet summarySet = getSummaryStatement.executeQuery();
        String sum = "0";
        String name = "0";

        while (summarySet.next()){
            sum = summarySet.getString("sum(cost)");
            name = summarySet.getString("Full_name");
            SummaryArea.appendText(name+" "+sum+"\n");
        }


    }
// GOING BACK TO ADMIN VIEW. FOR BACK BUTTON
    public void switchAdmin(ActionEvent event) throws IOException {



        Parent root = FXMLLoader.load(getClass().getResource("admin-page-View.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }



}
