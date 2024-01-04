package com.example.cafeteria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class HelloController {


    @FXML
    private Label SummaryLabel;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane anchorInScroll;




    private Stage stage;
    private Scene scene;

// SETTING UI SPACE FOR BUTTONS
    double topAnchorLabel = 15;
    double topAnchorButton = 15;

    int buttonID = 1;
    int labelID = 1;
    int clientID = 0;
    int i =0;

    // ARRAYS TO STORE CREATED BUTTON AND LABELS, SO WE CAN CALL THEM TO ASSIGN FUNCTIONS
    ArrayList<Label> labels = new ArrayList<Label>();
    ArrayList<Button> buttons = new ArrayList<Button>();



// SQL DATABASE CONNECTION VARIABLES
    String url = "jdbc:mysql://db4free.net:3306/cafeteria";
    String username = "martinez600";
    Connection connection = DriverManager.getConnection(url, username, "shelbyGT500#");




    public HelloController() throws SQLException {

    }


    // FUNCTION TO GO BACK TO LOGIN VIEW FOR "LOG OUT BUTTON"
    public void switchToLoginView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }



// FUNCTION THAT AUTOMATICALLY COUNTS AND PRINTS ALL COSTS FOR THE CUSTOMER IN THE WINDOW WITH LABEL. DATABASE HAS TRIGGER THAT COUNTS "cost", AND THIS FUNCTION ONLY SUMS IT
    public void updateSumCost() throws SQLException {
        PreparedStatement stmtSum = connection.prepareStatement("SELECT sum(cost) FROM balance WHERE Worker_ID = ?");
        stmtSum.setString(1, String.valueOf(clientID));
        ResultSet getCostsSum = stmtSum.executeQuery();

        while (getCostsSum.next()) {
            String costsSum = getCostsSum.getString("sum(cost)");
            SummaryLabel.setText(costsSum);
        }
    }


    //ADDING LABEL WITH INFORMATION FROM DATABASE ABOUT AMOUNT OF PRODUCTS CUSTOMER ORDERED. LABELS STORED IN ARRAY,
    // ID OF PRODUCT FROM DB CORRESPONDS WITH ARRAY ELEMENT
    private void addNewLabel() throws SQLException {

        //GETTING NAME OF PRODUCT FROM ID OF BUTTON CORRESPONDING TO IT
        PreparedStatement checkLabelAmount = connection.prepareStatement("SELECT Name FROM items where ID = ?");
        checkLabelAmount.setString(1, String.valueOf(buttonID));
        ResultSet itemsRS = checkLabelAmount.executeQuery();


        while (itemsRS.next()){
            //CREATING LABEL AND POSITIONING, GIVING IT "0" DEFAULT VALUE, ADDING LABEL TO ANCHOR
            Label labelLabel = new Label("0");
            AnchorPane.setTopAnchor(labelLabel, topAnchorLabel);
            AnchorPane.setLeftAnchor(labelLabel, 150.0);
            anchorInScroll.getChildren().add(labelLabel);
            //SETTING LABEL ID, SO WE CAN CALL IT LATER
            labelLabel.setId(String.valueOf(labelID));

            // GETTING VALUE OF PRODUCT CORRESPONDING TO LABEL, BY CLIENT ID AND ITEM ID, buttonID CORRESPONDS TO ITEM ID FROM DB
            PreparedStatement checkLabelName = connection.prepareStatement("SELECT Amount from balance where Worker_ID = ? and Item_ID = ?");
            checkLabelName.setString(1, String.valueOf(clientID));
            checkLabelName.setString(2, String.valueOf(buttonID));
            ResultSet labelRS = checkLabelName.executeQuery();

            //SETTING CORRECT VALUE TO GIVEN LABEL
            while (labelRS.next()){

                labelLabel.setText(labelRS.getString("Amount"));

            }
            //MAKING ROOM FOR ANOTHER LABEL, ADJUSTING POSITIONING AND ADDING LABEL TO ARRAY
            topAnchorLabel += 60;
            labelID +=1;
            labels.add(labelLabel);


        }

    }




//CREATING NEW BUTTON, GIVING IT ID AND ADDING TO ARRAY OF BUTTONS (NAME FOR BUTTON TAKEN FROM ITEMS IN DATABASE, ID ORDER IN DB IS BUTTON ORDER IN ARRAY
    private void addNewButton(String buttonLabel) throws SQLException {
        Button button = new Button(buttonLabel);
        AnchorPane.setTopAnchor(button, topAnchorButton);
        AnchorPane.setLeftAnchor(button, 50.0);
        anchorInScroll.getChildren().add(button);

        button.setId(String.valueOf(buttonID));

        topAnchorButton += 60;
        buttons.add(button);
        button.getId();

    }




//ADDING ACTIONS TO CREATED BUTTONS, CALLING BY ID OR ARRAY ORDER
    private void addingActionsToButtons()throws SQLException{

        final int iFinal = i;
        final int buttonFinal = buttonID;
            //SETTING ACTION TO BUTTON IN ORDER FROM ARRAYS OF BUTTONS
            buttons.get(iFinal).setOnAction(event -> {


                //GETTING TEXT (NUMBERS OF ORDERS FOR THIS PRODUCT) FOR BUTTON FROM LABEL, ACCoRDING TO BUTTON NUMBER AND LABEL NUMBER IN ARRAY
                int tempText = Integer.parseInt(labels.get(iFinal).getText());

                //IF THERE IS 0 ORDERS OF PRODUCT, PUSHING BUTTON CAUSES ADDING Item_ID and Worker_ID WITH VALUE 1 IN DATABASE "balance" TABLE
                if (tempText == 0){
                    try {
                        PreparedStatement buttonActionPS = connection.prepareStatement("INSERT INTO balance (Item_ID, Worker_ID, Amount) VALUES (?, ?, 1)");
                        buttonActionPS.setString(1, String.valueOf(buttonFinal));
                        buttonActionPS.setString(2, String.valueOf(clientID));
                        buttonActionPS.execute();

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
                //IF THERE WAS ALREADY ORDER FOR SPECIFIC PRODUCT, JUST ADD ONE VALUE TO THE DATABASE WITH PRODUCT ID AND WORKER ID
                else{
                    try {
                        PreparedStatement buttonActionPS = connection.prepareStatement("UPDATE balance SET Amount = Amount +1 WHERE Item_ID = ? and Worker_ID = ?");
                        buttonActionPS.setString(1, String.valueOf(buttonFinal));
                        buttonActionPS.setString(2, String.valueOf(clientID));
                        buttonActionPS.executeUpdate();



                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                }
                //AFTER ORDER, SUM APP AND SHOW NEW SUM BALANCE ON THE SCREEN FOR USER
                try {
                    updateSumCost();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //SET VALUE +1 IN LABEL CORRESPONDING TO THE PUSHED BUTTON
                labels.get(iFinal).setText(String.valueOf(tempText+ 1));


            });
            //GO TO NEXT BUTTON
             i++;
        }



    // ORDER OF EVENTS ON STARTUP
    @FXML
    public void initialize() throws SQLException {
        //GETTING USER LOGIN, FROM PREV PAGE BY CREATING INSTANCE OF LOGINCONTROLLER
        LoginController loginController = new LoginController();
        String login1 = loginController.getLogin();

        //CREATING CONNECTION TO DATABASE USING LOGIN RECEIVED FROM LOGINCONTROLLER
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM workers WHERE Full_name LIKE ?");
        stmt.setString(1, login1);
        ResultSet getClientIdResultSet = stmt.executeQuery();

// GETTING USER ID FROM DB KNOWING HIS LOGIN
        while (getClientIdResultSet.next()) {
            String clientIDSTRING = getClientIdResultSet.getString("ID");
            clientID = Integer.parseInt(clientIDSTRING);
        }

        // INITIALIZING USER COSTS AND SHOWING
        updateSumCost();
// CALLING DATABESE FOR EXISTING ITEMS
        PreparedStatement stmtForButtons = connection.prepareStatement("SELECT * FROM items");
        ResultSet buttonsRS = stmtForButtons.executeQuery();

// FOR EVERY EXISTING ITEM IN DB, CALLING FUNCTIONS IN ORDER TO:
        while (buttonsRS.next()){
            //GET NAME OF CURRENT ITEM, SO GENERATED BUTTONS ARE PROPERLY LABELED
            String itemName = buttonsRS.getString("Name");
            //ADD NEW LABEL
            addNewLabel();
            //ADD NEW BUTTON WITH PROPER NAME FROM VARIABLE
            addNewButton(itemName);
            //ADD ACTIONS TO BUTTONS SINCE THERE ARE ALERADY BUTTONS AND LABELS
            addingActionsToButtons();
            //GOING FOR NEXT CYCLE
            buttonID += 1;
        }


}}

