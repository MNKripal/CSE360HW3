package application.studentpages;

import java.sql.SQLException;
import java.util.List;

import application.UserHomePage;
import application.Question;
import application.Answer;
import application.User;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This page allows the user to browse questions, search for questions,
 * order their display, and click on the questions for more details.
 * Based on work from TP1 GUI features.
 */
public class BrowseQuestions {
    
    private final DatabaseHelper databaseHelper;
    private final User user;
    
    public BrowseQuestions(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
    }
    
    // Searches for the items and produces the list
    public void populateSearch(VBox questionSet, Stage primaryStage, String criteria, int mode, String ordering) throws SQLException {
        if (mode == 0) {
            questionSet.getChildren().clear();
            
            List<Question> queryList;
            
            if (criteria == null || criteria.isEmpty()) {
                // Fetch all questions when no criteria provided
                queryList = databaseHelper.getAllQuestions();
                System.out.println("DEBUG: Retrieved " + queryList.size() + " questions from DB."); // Debug log
            } else {
                // Fetch questions by title criteria
                queryList = databaseHelper.getQuestionByTitle("%" + criteria + "%", ordering);
            }
    
            for (Question q : queryList) {
                System.out.println("DEBUG: Adding question to list - ID: " + q.getId() + ", Title: " + q.getTitle());
                QuestionResultItem newItem = new QuestionResultItem(q, user, databaseHelper, primaryStage);
                questionSet.getChildren().add(newItem);
            }
        }
    }
            
    
    public void show(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Label to display "List Questions"
        Label listQuestion = new Label("List Questions");
        listQuestion.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Create a larger ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(600, 400); 
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // always show vertical scroll bar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // hide horizontal bars 
        
        VBox questionSet = new VBox(4); // spacing between each item
        scrollPane.setContent(questionSet);
        
        // Search box holder
        HBox searchHolder = new HBox();
        Label searchLabel = new Label("Search");
        TextField searchQuery = new TextField();
        ChoiceBox<String> sortBy = new ChoiceBox<>();
        sortBy.getItems().add("Recent");
        sortBy.getItems().add("Resolved");
        sortBy.getItems().add("Unresolved");
        sortBy.setValue("Recent");
        
        // Search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(a -> {
            try {
                populateSearch(questionSet, primaryStage, searchQuery.getText(), 0, sortBy.getValue());
            } catch (SQLException e) {
                System.out.println("oops");
                System.out.println(e);
            } catch (ClassCastException e) {
                System.out.println("cast failed");
                System.out.println(e);
            }
        });
        
        searchHolder.getChildren().addAll(searchLabel, searchQuery, sortBy, searchButton);
        
        // Get all questions (or a subset) and add them to the question set
        try {
            populateSearch(questionSet, primaryStage, "", 0, "Recent");
        } catch (SQLException e) {
            System.out.println("oops");
            System.out.println(e);
        }
        
        Button exit = new Button("Exit");
        exit.setOnAction(a -> {
            new UserHomePage(databaseHelper, "Student", user).show(primaryStage);
        });
    
        layout.getChildren().addAll(listQuestion, searchHolder, scrollPane, exit);
        Scene userScene = new Scene(layout, 800, 400);
    
        // Set the scene to primary stage
        primaryStage.setScene(userScene);
        primaryStage.setTitle("Browse Questions");
    }
}