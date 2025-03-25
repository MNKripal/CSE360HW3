package application.adminpages;

import java.sql.SQLException;
import java.util.Optional;

import application.AdminHomePage;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class DeleteUserAccountPage {
	
	private final DatabaseHelper databaseHelper;
	
	public DeleteUserAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Delete User Account");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    TextField username = new TextField();
	    username.setMaxWidth(250);
	    username.setPromptText("Username");
        
        Button exit = new Button("Exit");
        exit.setOnAction(a -> {
        	new AdminHomePage(databaseHelper).show(primaryStage);
        });
        
        Label errorLabel = new Label("");
	    errorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
        
        Button delete = new Button("Delete");
        delete.setOnAction(a -> {
        	Alert areYouSure = new Alert(Alert.AlertType.CONFIRMATION);
        	areYouSure.setTitle("Confirmation");
        	areYouSure.setHeaderText("Are you sure?");
        	areYouSure.setContentText("Are you sure you want to delete the following user: " + username.getText());
        	Optional<ButtonType> result = areYouSure.showAndWait();
        	if (result.isPresent() && result.get() == ButtonType.OK) {
        		try {
            		if(databaseHelper.getUserRole(username.getText()).contains("A")) {
                		errorLabel.setText("Cannot delete a user with the admin role");
                		return;
                	}
            	} catch(Exception e){
            		errorLabel.setText("There was a problem deleting the specified user please, double check your spelling.");
            		return;
            	}
            	
            	try {
    				if(!databaseHelper.removeUser(username.getText())) {
    					errorLabel.setText("There was a problem deleting the specified user please, double check your spelling.");
    				} else {
    					new AdminHomePage(databaseHelper).show(primaryStage);
    				}
    			} catch (SQLException e) {
    			}
        	} 
        });
        
        
        

	    layout.getChildren().addAll(userLabel, username, delete, exit, errorLabel);
	    Scene userScene = new Scene(layout, 800, 400);
	 
	   
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Delete User Account");
    	
    }
}