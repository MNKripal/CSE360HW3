package application.adminpages;

import java.sql.SQLException;

import application.AdminHomePage;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class GiveTempPasswordPage {
	
	private final DatabaseHelper databaseHelper;
	
	public GiveTempPasswordPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Assign Temp Password");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Label errorLabel = new Label("");
	    errorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
	    
	    TextField username = new TextField();
	    username.setMaxWidth(250);
	    username.setPromptText("Username");
	    
	    TextField tempPassword = new TextField("");
	    tempPassword.setMaxWidth(250);
	    tempPassword.setPromptText("Temporary Password");
        
	    Button setTemp = new Button("Set Password");
	    setTemp.setOnAction(a -> {
	    	try {
        		if(databaseHelper.getUserRole(username.getText()).contains("A")) {
            		errorLabel.setText("Cannot grant temp password to admin role");
            		return;
            	}
        	} catch(Exception e){
        		errorLabel.setText("There was a problem accessing the specified user please, double check your spelling.");
        		return;
        	}
	    	
	    	try {
				if(!databaseHelper.updatePassword(username.getText(), tempPassword.getText(), true)) {
					errorLabel.setText("There was a problem accessing the specified user please, double check your spelling.");
				} else {
					new AdminHomePage(databaseHelper).show(primaryStage);
				}
			} catch (SQLException e) {
			}
        });

	    
        Button exit = new Button("Exit");
        exit.setOnAction(a -> {
        	new AdminHomePage(databaseHelper).show(primaryStage);
        });
        
        

	    layout.getChildren().addAll(userLabel, username, tempPassword, setTemp, exit, errorLabel);
	    Scene userScene = new Scene(layout, 800, 400);
	 
	   
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Assign Temp Password");
    	
    }
}