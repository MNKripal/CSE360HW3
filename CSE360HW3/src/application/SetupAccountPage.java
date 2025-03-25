package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button setupButton = new Button("Setup");
        Button backButton = new Button("Back");
        
        //navigates back to the setuploginselection page when clicked
        backButton.setOnAction(A -> {
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            
            // Send the username to the username recognizer class to validate the input 
            String errorMsg = UserNameRecognizer.checkForValidUserName(userName); 
            
            if(errorMsg != "") { // if there is an error handle it below
            	errorLabel.setText(errorMsg); // display the error to user   
            	return; // since there was a problem with the username we do not want to even check anything else so end the method here
            }
            
            // send the password to the password evaluator class to validate the input
            errorMsg = PasswordEvaluator.evaluatePassword(password);
            if(errorMsg != "") { // if there is an error handle it below
            	errorLabel.setText(errorMsg); // display the error to user   
            	return; // since there was a problem with the username we do not want to even check anything else so end the method here
            }
            
            try {
            	// Check if the user already exists
            	if(!databaseHelper.doesUserExist(userName)) {
            		
            		String role = databaseHelper.validateInvitationCode(code);
            		// Validate the invitation code
            		if(!role.isBlank()) {
            			
            			// Create a new user and register them in the database
		            	User user=new User(userName, password, role);
		                databaseHelper.register(user);
		                
		             // Navigate to the Welcome Login Page
		                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            		}
            		else {
            			errorLabel.setText("Please enter a valid invitation code");
            		}
            	}
            	else {
            		errorLabel.setText("This username is taken! Please use another to setup an account");
            	}
            	
            } catch (SQLException e) {
                errorLabel.setText("Ran into the following: " + e.getMessage());
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField,inviteCodeField, setupButton, backButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
