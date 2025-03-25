package application;


import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InvitePage class represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking a button.
 */

public class InvitationPage {

	/**
     * Displays the Invite Page in the provided primary stage.
     * 
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     */
    public void show(DatabaseHelper databaseHelper,Stage primaryStage) {
    	String[] possibleRoleCode = { "A", "S", "I", "T", "R" };
    	String[] possibleRoles = { "Admin", "Student", "Instructor", "Staff", "Reviewer"};
    	CheckBox[] checkBoxes = new CheckBox[5];
    	
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display the title of the page
	    Label userLabel = new Label("Invite ");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Button to generate the invitation code
	    Button showCodeButton = new Button("Generate Invitation Code");
	    
	    
	    // Label to display the generated invitation code
	    Label inviteCodeLabel = new Label(""); ;
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        
        Label errorLabel = new Label(""); ;
        errorLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        
        layout.getChildren().add(userLabel);
        
        for(int i = 0; i < possibleRoles.length; i++) {
	    	checkBoxes[i] = new CheckBox(possibleRoles[i]);
	    	layout.getChildren().add(checkBoxes[i]);
	    }
        
        showCodeButton.setOnAction(a -> {
        	String roleCode = "";
        	for(int i = 0; i < possibleRoles.length; i++) {
    	    	if(checkBoxes[i].isSelected()) {
    	    		roleCode += possibleRoleCode[i];
    	    	}
    	    }
        	if(roleCode.isBlank()) {
        		errorLabel.setText("You must select at least one role.");
        		return;
        	}
        	
        	// Generate the invitation code using the databaseHelper and set it to the label
            String invitationCode = databaseHelper.generateInvitationCode(roleCode);
            inviteCodeLabel.setText(invitationCode);
        });
        
        
	    

        layout.getChildren().addAll(showCodeButton, inviteCodeLabel);
	    Scene inviteScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(inviteScene);
	    primaryStage.setTitle("Invite Page");
    	
    }
}