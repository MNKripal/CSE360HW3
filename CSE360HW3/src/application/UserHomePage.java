package application;

import databasePart1.DatabaseHelper;
import application.studentpages.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 * File is from TP 1 just added addtional options for when student mode is selected
 */

public class UserHomePage {
	
	private final DatabaseHelper databaseHelper;
	private final User user;
	private final String userRole;
	
	public UserHomePage(DatabaseHelper databaseHelper, String appliedRole, User user) {
        this.databaseHelper = databaseHelper;
        this.userRole = appliedRole;
        this.user = user;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, " + userRole + "!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Button askQuestion = new Button("Ask a Question");
	    askQuestion.setOnAction(a -> {
	    	new AskQuestion(databaseHelper, user).show(primaryStage);
	    });
	    
	    Button browseQuestion = new Button("Browse Questions");
	    browseQuestion.setOnAction(a -> {
	    	new BrowseQuestions(databaseHelper, user).show(primaryStage);
	    });
	    
	    Button logout = new Button("Logout");
	    logout.setOnAction(a -> {
	    	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	    });

	    layout.getChildren().add(userLabel);
	    layout.getChildren().add(logout);
	    if(userRole == "Student") {
	    	layout.getChildren().add(askQuestion);
	    	layout.getChildren().add(browseQuestion);
	    	
	    }
	    Scene userScene = new Scene(layout, 800, 400);
	    
	    

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}