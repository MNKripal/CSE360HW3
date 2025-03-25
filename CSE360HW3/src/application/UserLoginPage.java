package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input field for the user's userName, password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");


        Button loginButton = new Button("Login");
        
        //button to go back to the other menu in case somebody misclicks something
        Button backButton = new Button("Back");
        
        //navigates back to the setuploginselection page when clicked
        backButton.setOnAction(A -> {
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
            String password = passwordField.getText();
            try {
            	User user=new User(userName, password, "");
            	
            	//added section that sets up an appropriate user Id
            	int userId = databaseHelper.getUserId(userName);
            	if(userId != 0) {
            	user.setId(userId);
            	} else {
            		throw new SQLException("UserId invalid");
            	}
            	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
            	
            	// Retrieve the user's role from the database using userName - also has a validator to check that the userId is valid
            	String role = databaseHelper.getUserRole(userName);
            	
            	
            	if(role!=null) {
            		user.setRole(role);
            		if(databaseHelper.login(user)) {
            			if(databaseHelper.userHasTempPassword(userName)) {
            				new ForgotPasswordPage(databaseHelper, user).show(primaryStage);
            				return;
            			}
            			welcomeLoginPage.show(primaryStage,user);
            		}
            		else {
            			// Display an error if the login fails
                        errorLabel.setText("Error logging in");
            		}
            	}
            	else {
            		// Display an error if the account does not exist
                    errorLabel.setText("user account doesn't exists");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, backButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
