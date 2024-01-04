package com.example.cafeteria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    static String login;
    static String password;

    @FXML
    public void initialize() throws SQLException{

    }

    // GETTER FOR LOGIN, TO MAKE AN INSTANCE IN HelloController
    public String getLogin(){
        return login;
    }


    // FUNCTION THAT HANDLES LOGIN OPERATION
    public String switchToHelloView(ActionEvent event) throws IOException, SQLException {

        // GETTING LOGIN AND PASSWORD FROM TEXTFIELDS
        login = loginText.getText();
        password = passwordText.getText();

        // TEMP PASSWORD VARIABLE
        String dbPassword = null;


        // MAKING CONNECTION TO DATABASE
        String url = "jdbc:mysql://db4free.net:3306/cafeteria";
        String username = "martinez600";
        Connection connection = DriverManager.getConnection(url, username, "shelbyGT500#");

        // CHECKING IF USER EXISTS IN DATABASE
        PreparedStatement checkUserExistsStatement = connection.prepareStatement("SELECT * FROM workers WHERE Full_name = ?");
        checkUserExistsStatement.setString(1, login);
        ResultSet checkUserExistsResultSet = checkUserExistsStatement.executeQuery();

        // GETTING USER PASSWORD FROM DATABASE
        PreparedStatement gettingPassword = connection.prepareStatement("SELECT password from workers WHERE Full_name = ?");
        gettingPassword.setString(1, login);
        ResultSet passRS = gettingPassword.executeQuery();

        // SETTING PASSWORD VARIABLE TO COMPARE FROM DATABASE PASSWORD
        while (passRS.next()){
            dbPassword = passRS.getString("password");
        }

        

        // CHECKING IF ADMIN CREDENTIALS ARE INPUTED SO ADMIN PAGE CAN LOAD
        if (login.equals("Admin") && password.equals("Admin")){
            Parent root = FXMLLoader.load(getClass().getResource("admin-page-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        // IF NOT ADMIN CREDENTIALS, COMAPRING INPUTED PASSWORD WITH DATABASE PASSWORD FOR USER AND LOGGING IN
        else if (checkUserExistsResultSet.next() && password.equals(dbPassword)) {
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
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
        //RETURNING LOGIN SO hello-view CAN TAKE DATA FROM THE CORRECT USER DATABASE
        return login;
    }


}
