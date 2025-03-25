package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.*;

import java.util.ArrayList;

import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class WelcomeLoginPage {
	
	private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    public void show( Stage primaryStage, User user) {
    	
    	VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Label welcomeLabel = new Label("Welcome!!");
	    welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    String role = user.getRole();
	    if(role.contains("A")) {
	    	role = "ASITR";
	    }
	    
	    
	    System.out.println(role);
	    ArrayList<String> roles = new ArrayList<String>();
	    if(role.contains("A")) { roles.add("Admin"); }
	    if(role.contains("S")) { roles.add("Student"); }
	    if(role.contains("I")) { roles.add("Instructor"); }
	    if(role.contains("T")) { roles.add("Staff"); }
	    if(role.contains("R")) { roles.add("Reviewer"); }
	    
	    // admin, student, instructor, staff, and reviewer.
	    ChoiceBox roleButton = new ChoiceBox(FXCollections.observableArrayList(roles.toArray()));
	    roleButton.getSelectionModel().selectFirst();
	    
	    
	    // Button to navigate to the user's respective page based on their role
	    Button continueButton = new Button("Continue to your Page");
	    continueButton.setOnAction(a -> {
	    	String selectedRole = (String)roleButton.getValue();
	    	System.out.println("logging in with role: " + selectedRole);
	    
	    	
	    	if(selectedRole.equals("Admin")) {
	    		new AdminHomePage(databaseHelper).show(primaryStage);
	    	}
	    	else {
	    		new UserHomePage(databaseHelper, selectedRole, user).show(primaryStage);
	    	}
	    });
	    
	    // Button to quit the application
	    Button quitButton = new Button("Quit");
	    quitButton.setOnAction(a -> {
	    	databaseHelper.closeConnection();
	    	Platform.exit(); // Exit the JavaFX application
	    });
	    
	    // "Invite" button for admin to generate invitation codes
	    if (role.contains("A")) {
            Button inviteButton = new Button("Invite");
            inviteButton.setOnAction(a -> {
                new InvitationPage().show(databaseHelper, primaryStage);
            });
            layout.getChildren().add(inviteButton);
        }

	    layout.getChildren().addAll(welcomeLabel,roleButton, continueButton,quitButton);
	    Scene welcomeScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(welcomeScene);
	    primaryStage.setTitle("Welcome Page");
	    
	    // go straight to page if the user only has one role
	    if(roles.size() <= 1) {
	    	System.out.println(roles.size());
	    	new UserHomePage(databaseHelper, roles.get(0), user).show(primaryStage);
	    }
    }
}