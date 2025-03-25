package application.adminpages;

import java.sql.SQLException;
import java.util.List;

import application.AdminHomePage;
import application.User;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class ListUsersPage {
	
	private final DatabaseHelper databaseHelper;
	
	public ListUsersPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("List Users");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    TextArea textArea = new TextArea();
        textArea.setPrefSize(300, 200);  // Set TextArea size
        textArea.setWrapText(true);
        textArea.setEditable(false);

        // Create a StackPane to center the TextArea
        StackPane centeringPane = new StackPane(textArea);

        // Create a larger ScrollPane
        ScrollPane scrollPane = new ScrollPane(centeringPane);
        scrollPane.setPrefSize(600, 400);  // Set ScrollPane size
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        
        // get all users
        String result = "";
        try {
			List<User> users = databaseHelper.getAllUsers();
			for(User user : users) {
				result += "\n------------";
				result += "\nUsername: " + user.getUserName();
				result += "\nRole: " + user.getRole();
			}
		} catch (SQLException e) {
			result = "Failed to retrieve user data";
		}
        //set scroll pane text
        textArea.setText(result);
        
        Button exit = new Button("Exit");
        exit.setOnAction(a -> {
        	new AdminHomePage(databaseHelper).show(primaryStage);
        });

	    layout.getChildren().addAll(userLabel, scrollPane, exit);
	    Scene userScene = new Scene(layout, 800, 400);
	 
	   
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("List Users");
    	
    }
}