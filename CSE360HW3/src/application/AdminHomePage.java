package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import application.adminpages.*;



/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	private final DatabaseHelper databaseHelper;
	
	public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

	public void show(Stage primaryStage) {
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    Button logout = new Button("Logout");
	    
	    logout.setOnAction(a -> {
	    	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	    });
	    
	    Button listUsers = new Button("List Users");
	    listUsers.setOnAction(a -> {
	    	new ListUsersPage(databaseHelper).show(primaryStage);
	    });
	    
	    Button giveTempPassword = new Button("Grant Temp Password");
	    giveTempPassword.setOnAction(a -> {
	    	new GiveTempPasswordPage(databaseHelper).show(primaryStage);
	    });
	    
	    Button deleteUserAccount = new Button("Delete User");
	    deleteUserAccount.setOnAction(a -> {
	    	new DeleteUserAccountPage(databaseHelper).show(primaryStage);
	    });
	    
	    Button modifyUserRoles = new Button("Modify User Role");
	    modifyUserRoles.setOnAction(a -> {
	    	new ModifyUserRolesPage(databaseHelper).show(primaryStage);
	    });
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    layout.getChildren().addAll(adminLabel, listUsers, giveTempPassword, modifyUserRoles, deleteUserAccount, logout);

	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
}