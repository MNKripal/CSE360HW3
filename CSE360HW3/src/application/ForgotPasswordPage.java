package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class ForgotPasswordPage {
	
	private final DatabaseHelper databaseHelper;
	private final User user;
	
	public ForgotPasswordPage(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Reset Password");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter New Password");
        passwordField.setMaxWidth(250);
        
        Button confirm = new Button("Confirm");
        confirm.setOnAction(a -> {
        	System.out.println("New Password: " + passwordField.getText());
        	try {
				databaseHelper.updatePassword(user.getUserName(), passwordField.getText(), false);
				new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
			} catch (SQLException e) {
				System.out.println("error occured");
			}
        });

	    layout.getChildren().addAll(userLabel, passwordField, confirm);
	    Scene userScene = new Scene(layout, 800, 400);
	 
	   
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Forgot Password Page");
    	
    }
}