package application.studentpages;

import java.sql.SQLException;

import java.util.List;
//import application.*;
import application.UserHomePage;
import application.Answer;
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
 * This page allows a user to enter an answer for their question
 */

public class GiveAnswer {
	
	private final DatabaseHelper databaseHelper;
	private final User user;
	Question question;
	private final Answer answer;
	
	//constructior
	public GiveAnswer(DatabaseHelper databaseHelper, Question question, User user) {
        this.databaseHelper = databaseHelper;
        this.question = question;
        this.user = user;
        answer = null;
    }
	
	public GiveAnswer(DatabaseHelper databaseHelper, Question question, User user, Answer answer) {
        this.databaseHelper = databaseHelper;
        this.question = question;
        this.user = user;
        this.answer = answer;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label questionLabel = new Label(question.getTitle());
	    questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    //text box for the title
	    
	    //Label title = new Label();
	    
	    TextArea textArea = new TextArea();
        textArea.setPrefSize(300, 200);  // Set TextArea size
        textArea.setWrapText(true);
        textArea.setEditable(true);
        
        // if modifying set the text area to have the current answer text
        if(answer != null) {
        	textArea.setText(answer.getText());
        }

        // Create a StackPane to center the TextArea
        StackPane centeringPane = new StackPane(textArea);

        // Create a larger ScrollPane
        ScrollPane scrollPane = new ScrollPane(centeringPane);
        scrollPane.setPrefSize(600, 400);  // Set ScrollPane size
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        
        //create the askQuestion button
        Button submit = new Button("Submit");
        submit.setOnAction(a -> {
        	try {
        		//checks to ensure that the text field for answer is not empty - validation code
        		if(textArea.getText().equals("")) {
        			throw new SQLException ("Provide answer text");
        		}
        		if(answer == null) {
        			databaseHelper.addAnswer(textArea.getText(), question.getId(), user.getId());
        		} else {
        			databaseHelper.updateAnswerById(answer.getId(), textArea.getText());
        		}
        		new BrowseQuestions(databaseHelper, user).show(primaryStage);
        	} catch (SQLException e){
        		System.out.println(e);
        	}
        });
        
        //create the exit button
        Button exit = new Button("Exit");
        exit.setOnAction(a -> {
        	new UserHomePage(databaseHelper, "Student", user).show(primaryStage);
        });

	    layout.getChildren().addAll(questionLabel, scrollPane, exit, submit);
	    Scene makeQuestion = new Scene(layout, 800, 400);
	 
	   
	    // Set the scene to primary stage
	    primaryStage.setScene(makeQuestion);
	    primaryStage.setTitle("Post Question");
    	
    }
}