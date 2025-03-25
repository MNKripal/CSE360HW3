package application.studentpages;

import application.Question;
import application.User;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.scene.control.Button;

public class QuestionResultItem extends HBox {
    public QuestionResultItem(Question question, User user, DatabaseHelper databaseHelper, Stage primaryStage) {
        setMaxWidth(Double.MAX_VALUE);
        setPadding(new Insets(10));
        setSpacing(15);
        setAlignment(Pos.CENTER_LEFT);
        
        Hyperlink titleButton = new Hyperlink(question.getTitle());
        titleButton.setStyle("-fx-font-size: 18px; -fx-text-fill: #1a0dab;");
        titleButton.setOnAction(e -> {
            new QuestionView(databaseHelper, question.getId(), user).show(primaryStage);
        });
        
        String description = question.getText().trim();
        if(description.length() > 50) {
            description = description.substring(0, 50) + "...";
        } else if(description.length() == 0) {
            description = "No description was provided.";
        }
        
        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #545454;");
        
        VBox textContainer = new VBox(titleButton, descriptionLabel);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        
        // Add Delete button only if the user is the author
        if (user.getId() == question.getAuthor()) {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(a -> {
                try {
                    databaseHelper.deleteQuestion(question.getId());
                    new BrowseQuestions(databaseHelper, user).show(primaryStage); // Refresh list
                } catch (SQLException e) {
                    System.out.println("Error deleting question: " + e.getMessage());
                }
            });
            textContainer.getChildren().add(deleteButton);
        }
        
        Rectangle box = new Rectangle(20,20);
        box.setFill(Color.WHITE);
        box.setStroke(Color.LIGHTGRAY);
        
        Polygon checkmark = new Polygon();
        checkmark.getPoints().addAll(
            4.0, 10.0,  
            8.0, 14.0,  
            16.0, 2.0,  
            12.0, 2.0,  
            8.0, 10.0   
        );
        
        if(question.getResolved()) {
            checkmark.setFill(Color.GREEN);
        } else {
            checkmark.setFill(Color.LIGHTGRAY);
        }
        
        StackPane checkmarkPane = new StackPane(box, checkmark);
        checkmarkPane.setAlignment(Pos.CENTER);
        
        getChildren().addAll(checkmarkPane, textContainer);
        setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");
    }
}