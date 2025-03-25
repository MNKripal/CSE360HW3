package application.studentpages;

import java.sql.SQLException;
import java.util.List;
//import application.*;
import application.UserHomePage;
import application.Question;
import application.User;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page allows the user to ask questions - based off some UI features from TP 1
 */

public class AskQuestion {
	
	private final DatabaseHelper databaseHelper;
	private final User user;
	private final Question question;
	
	//constructior
	public AskQuestion(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
        question = null;
    }
	
	public AskQuestion(DatabaseHelper databaseHelper, User user, Question question) {
        this.databaseHelper = databaseHelper;
        this.user = user;
        this.question = question;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label questionLabel = new Label("Ask a Question");
	    questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    //text box for the title
	    
	    TextField title = new TextField();
	    
	    TextArea textArea = new TextArea();
        textArea.setPrefSize(300, 200);  // Set TextArea size
        textArea.setWrapText(true);
        textArea.setEditable(true);

        // Create a StackPane to center the TextArea
        StackPane centeringPane = new StackPane(textArea);

        // Create a larger ScrollPane
        ScrollPane scrollPane = new ScrollPane(centeringPane);
        scrollPane.setPrefSize(600, 400);  // Set ScrollPane size
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        if(question != null) {
        	title.setText(question.getTitle());
        	textArea.setText(question.getText());
        }
        
        
        //create the askQuestion button
        Button submit = new Button("Submit");
        submit.setOnAction(a -> {
        	try {
        		//if statement is validation to ensure that the title cannot be blank
        		if (title.getText().equals("")) {
        			throw new SQLException ("Please Provide Title");
        		}
        		if(question == null ) {
        			databaseHelper.addQuestion(title.getText(), textArea.getText(), user.getId());
        		} else {
        			databaseHelper.updateQuestionById(question.getId(), title.getText(), textArea.getText());
        		}
        		new UserHomePage(databaseHelper, "Student", user).show(primaryStage);
        	} catch (SQLException e){
        		System.out.println("Crap");
        		System.out.println(e);
        	}
        });
        
        //create the exit button
        Button exit = new Button("Exit");
        exit.setOnAction(a -> {
        	new UserHomePage(databaseHelper, "Student", user).show(primaryStage);
        });

	    layout.getChildren().addAll(questionLabel, title, scrollPane, exit, submit);
	    Scene makeQuestion = new Scene(layout, 800, 400);
	 
	   
	    // Set the scene to primary stage
	    primaryStage.setScene(makeQuestion);
	    primaryStage.setTitle("Post Question");
    	
    }
}