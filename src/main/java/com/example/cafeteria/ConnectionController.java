package com.example.cafeteria;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public  class ConnectionController {

    static String login;
    static String password;
    private static Stage stage;
    private static Scene scene;


    public static void changeScene(ActionEvent event, String fxml){

        try {
            Parent root = FXMLLoader.load(ConnectionController.class.getResource(fxml));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



public static void connecting(ActionEvent event, String loginText, String passwordText) throws SQLException, IOException {


    PreparedStatement checkUserExistsStatement = null;
    ResultSet checkUserExistsResultSet = null;
    PreparedStatement gettingPassword = null;
    ResultSet passRS = null;
    // GETTING LOGIN AND PASSWORD FROM TEXTFIELDS
    login = loginText;



    password = passwordText;

    // TEMP PASSWORD VARIABLE
    String dbPassword = null;

    try {
        String url = "jdbc:mysql://db4free.net:3306/cafeteria";
        String username = "martinez600";
        Connection connection = DriverManager.getConnection(url, username, "9a18aede");
        // CHECKING IF USER EXISTS IN DATABASE
        checkUserExistsStatement = connection.prepareStatement("SELECT * FROM workers WHERE Full_name = ?");
        checkUserExistsStatement.setString(1, login);
        checkUserExistsResultSet = checkUserExistsStatement.executeQuery();

        // GETTING USER PASSWORD FROM DATABASE
        gettingPassword = connection.prepareStatement("SELECT password from workers WHERE Full_name = ?");
        gettingPassword.setString(1, login);
        passRS = gettingPassword.executeQuery();
    }catch (SQLException e){
        e.printStackTrace();
    }

    // SETTING PASSWORD VARIABLE TO COMPARE FROM DATABASE PASSWORD
    while (passRS.next()){
        dbPassword = passRS.getString("password");
    }


    Parent root = null;
    // CHECKING IF ADMIN CREDENTIALS ARE INPUTED SO ADMIN PAGE CAN LOAD
    if (login.equals("Admin") && password.equals("Admin")){
        root = FXMLLoader.load(ConnectionController.class.getResource("admin-page-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    // IF NOT ADMIN CREDENTIALS, COMAPRING INPUTED PASSWORD WITH DATABASE PASSWORD FOR USER AND LOGGING IN
    else if (checkUserExistsResultSet.next() && password.equals(dbPassword)) {
        root = FXMLLoader.load(ConnectionController.class.getResource("hello-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //IF WRONG CREDENTIALS INPUTED (NOT IN DB) THEN ALERT SHOWN
    else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Wrong password or login");
        alert.show();


    }


}

}



