package com.example.cafeteria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class NewItemController {

    private Stage stage;
    private Scene scene;

    @FXML
    TextField nameText;
    @FXML
    TextField priceText;
    @FXML
    TextField stockText;


// ADDING ITEM TO DATABASE WITH CHOSEN NAME, PRICE, STOCK
    public void addItem() throws SQLException {

        String name = nameText.getText();
        String price = priceText.getText();
        String stock = stockText.getText();
//CHECKING IF ALL TEXT FIELDS ARE FILLED
        if (name != null && price != null && stock != null) {

            String url = "jdbc:mysql://db4free.net:3306/cafeteria";
            String username = "martinez600";
            Connection connection = DriverManager.getConnection(url, username, "shelbyGT500#");
// GETTING ITEMS FROM DATABASE
            PreparedStatement checkUserStatement = connection.prepareStatement("SELECT * FROM items WHERE Name = ?");
            checkUserStatement.setString(1, name);
            ResultSet checkUserResultSet = checkUserStatement.executeQuery();
// CKECKING IF ITEM IS ALREADY IN DATABASE
            if (checkUserResultSet.next()){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("item already exists");
                alert.show();

// IF ITEM IS NOT IN DATABASE, ADDING IT TO DB, THEN RESETING TEXT FIELDS
            }
            else {
                PreparedStatement resetStatement = connection.prepareStatement("INSERT INTO items (Name, Price, Stock) VALUES (?, ?, ?)");
                resetStatement.setString(1, name);
                resetStatement.setString(2, price);
                resetStatement.setString(3,stock);
                resetStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Item created!");
                alert.show();
                nameText.setText("");
                priceText.setText("");
                stockText.setText("");
            }
        }
// ALERT WHEN TEXT FIELDS ARE EMPTY
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("insert data first");
            alert.show();
        }

    }

//GOING BACK TO ADMIN VIEW
    public void switchAdmin(ActionEvent event) throws IOException {



        Parent root = FXMLLoader.load(getClass().getResource("admin-page-View.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
