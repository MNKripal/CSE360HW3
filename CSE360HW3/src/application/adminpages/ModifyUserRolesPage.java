package application.adminpages;

import java.sql.SQLException;

import application.AdminHomePage;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class ModifyUserRolesPage {
	
	private final DatabaseHelper databaseHelper;
	
	public ModifyUserRolesPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	String[] possibleRoleCode = { "S", "I", "T", "R" };
    	String[] possibleRoles = { "Student", "Instructor", "Staff", "Reviewer"};
    	CheckBox[] checkBoxes = new CheckBox[4];
    	
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Modify User Roles");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Label errorLabel = new Label("");
	    errorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
	    
	    TextField username = new TextField();
	    username.setMaxWidth(250);
	    username.setPromptText("Username");
	    
	    layout.getChildren().addAll(userLabel, username);
	    
	    for(int i = 0; i < possibleRoles.length; i++) {
	    	checkBoxes[i] = new CheckBox(possibleRoles[i]);
	    	layout.getChildren().add(checkBoxes[i]);
	    }
	    
	    Button updateRole = new Button("Update Role");
	    updateRole.setOnAction(a -> {
	    	try {
        		if(databaseHelper.getUserRole(username.getText()).contains("A")) {
            		errorLabel.setText("Cannot modify a user with the admin role");
            		return;
            	}
        	} catch(Exception e){
        		errorLabel.setText("There was a problem mdoifying the specified user please, double check your spelling.");
        		return;
        	}
        	
        	try {
        		String roleCode = "";
            	for(int i = 0; i < possibleRoles.length; i++) {
        	    	if(checkBoxes[i].isSelected()) {
        	    		roleCode += possibleRoleCode[i];
        	    	}
        	    }
        		
				if(!databaseHelper.updateUserRole(username.getText(), roleCode)) {
					errorLabel.setText("There was a problem modifying the specified user please, double check your spelling.");
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

	    layout.getChildren().addAll(updateRole, exit);
	    Scene userScene = new Scene(layout, 800, 400);
	 
	   
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Modify User Roles");
    	
    }
}