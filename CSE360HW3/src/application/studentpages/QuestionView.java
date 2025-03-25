package application.studentpages;

import java.sql.SQLException;
import java.util.List;

import application.UserHomePage;
import application.Question;
import application.Answer;
import application.User;
import application.studentpages.RWValidation;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This page allows the user to view details on questions, 
 * the question author to resolve questions and people to reflect positively on answers.
 * Now also allows users to reply to both questions and answers.
 */
public class QuestionView {
    
    private final DatabaseHelper databaseHelper;
    private final RWValidation validator;
    private final int number;
    private final User user;
    private Question selectedQuestion; // Declare selectedQuestion as a class-level variable

    public QuestionView(DatabaseHelper databaseHelper, int number, User user) {
        this.databaseHelper = databaseHelper;
        this.number = number;
        this.validator = new RWValidation();
        this.user = user;
    }

    public void show(Stage primaryStage) {
        System.out.println("DEBUG: Opening QuestionView for question ID: " + this.number);
        // Create Back Button
        Button backButton = new Button("Back to Questions");
        backButton.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        backButton.setOnAction(a -> {
            System.out.println("DEBUG: Returning to Browse Questions.");
            new BrowseQuestions(databaseHelper, user).show(primaryStage);
        });

        VBox layout = new VBox(10); // Restore spacing
        layout.setStyle("-fx-padding: 20; -fx-alignment: top-center;");

        Label listQuestion = new Label("Question Details");
        listQuestion.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox questionDetails = new VBox(10); // Restore spacing inside the box
        questionDetails.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #f9f9f9;");

        StackPane centeringPane = new StackPane(questionDetails);
        ScrollPane scrollPane = new ScrollPane(centeringPane);
        scrollPane.setPrefSize(600, 600);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Label title = null;
        TextArea text = null;

        try {
            // Retrieve the selected question
            selectedQuestion = databaseHelper.getQuestionById(this.number);

            if (selectedQuestion == null) {
                System.out.println("ERROR: No question found for ID: " + this.number);
                new BrowseQuestions(databaseHelper, user).show(primaryStage);
                return;
            }

            System.out.println("DEBUG: Retrieved Question - " + selectedQuestion.getTitle());

            // Question title and text setup
            title = new Label(selectedQuestion.getTitle());
            title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            text = new TextArea(selectedQuestion.getText());
            text.setEditable(false);
            text.setWrapText(true);
            text.setPrefHeight(100);
            
            
            // Buttons Setup
            Button answer = new Button("Answer Question");
            answer.setOnAction(a -> {
                new GiveAnswer(databaseHelper, selectedQuestion, user).show(primaryStage);
            });

            Button resolved = new Button("Mark as Resolved");
            resolved.setOnAction(a -> {
                try {
                    validator.resolve(databaseHelper, selectedQuestion, user);
                    new BrowseQuestions(databaseHelper, user).show(primaryStage);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });

            if (selectedQuestion.getResolved()) {
                title.setTextFill(Color.GREEN);
            }

            // Add buttons based on author and resolved status
            if (user.getId() == selectedQuestion.getAuthor() && !selectedQuestion.getResolved()) {
                questionDetails.getChildren().addAll(title, text, answer, resolved);
            } else {
                questionDetails.getChildren().addAll(title, text, answer);
            }
            
            if (user.getId() == selectedQuestion.getAuthor()) {
            	Button modify = new Button("Modify");
            	modify.setOnAction(a -> {
            		new AskQuestion(databaseHelper, user, selectedQuestion).show(primaryStage);
            	});
            	questionDetails.getChildren().add(modify);
            }

            // Display answers with buttons and deletion logic
            List<Answer> answerList = databaseHelper.getAnswersByQId(this.number);

            for (Answer i : answerList) {
                HBox l = new HBox(5);
                TextArea answerTextArea = new TextArea(i.getText());
                answerTextArea.setPrefWidth(600);
                answerTextArea.setMaxWidth(600);
                answerTextArea.setMinHeight(100);
                answerTextArea.setEditable(false);
                answerTextArea.setWrapText(true);

                Button workedButton = new Button("Answer Worked");
                Label workedLabel = new Label("Good answer");

                Button deleteButton = null;
                if (user.getId() == i.getAuthor()) {
                    deleteButton = new Button("Delete");
                    deleteButton.setOnAction(a -> {
                        try {
                            databaseHelper.deleteAnswer(i.getId());
                            new QuestionView(databaseHelper, number, user).show(primaryStage);
                        } catch (SQLException e) {
                            System.out.println("Error deleting answer: " + e.getMessage());
                        }
                    });
                }

                workedButton.setOnAction(a -> {
                    try {
                        validator.worked(databaseHelper, i, user);
                        new BrowseQuestions(databaseHelper, user).show(primaryStage);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                });

                if (user.getId() != i.getAuthor() && !i.getWorked()) {
                    l.getChildren().addAll(answerTextArea, workedButton);
                } else {
                    l.getChildren().add(answerTextArea);
                    if (i.getWorked()) {
                        l.getChildren().add(workedLabel);
                    }
                }
                
                // if the user is the answerer allow them to modify their answer
                if (user.getId() == i.getAuthor()) {
                	Button modify = new Button("Modify");
                	modify.setOnAction(a -> {
                		new GiveAnswer(databaseHelper, selectedQuestion, user, i).show(primaryStage);
                	});
                	l.getChildren().add(modify);
                }

                if (deleteButton != null) {
                    l.getChildren().add(deleteButton);
                }

                questionDetails.getChildren().add(l);
            }

        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // ** Replies to Question **
        System.out.println("DEBUG: Fetching replies for question ID: " + selectedQuestion.getId());
        VBox questionRepliesBox = new VBox(5);
        questionRepliesBox.setStyle("-fx-padding: 10;");

        TextField questionReplyField = new TextField();
        questionReplyField.setPromptText("Reply to this question...");
        Button replyQuestionButton = new Button("Reply");

        replyQuestionButton.setOnAction(a -> {
            try {
                String replyText = questionReplyField.getText();
                if (!replyText.isEmpty()) {
                    databaseHelper.addQuestionReply(replyText, selectedQuestion.getId(), user.getId());
                    new QuestionView(databaseHelper, selectedQuestion.getId(), user).show(primaryStage);
                }
            } catch (SQLException e) {
                System.out.println("ERROR: Failed to add question reply - " + e.getMessage());
                e.printStackTrace();
            }
        });

        try {
            List<Question> questionReplies = databaseHelper.getQuestionReplies(selectedQuestion.getId());
            System.out.println("DEBUG: Found " + questionReplies.size() + " question replies.");

            for (Question reply : questionReplies) {
                Label replyLabel = new Label("Reply: ");
                TextArea replyText = new TextArea(reply.getText());
                replyText.setEditable(false);
                replyText.setWrapText(true);

                VBox replyContainer = new VBox(5, replyLabel, replyText);
                replyContainer.setStyle("-fx-border-color: grey; -fx-padding: 5;");
                questionRepliesBox.getChildren().add(replyContainer);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to retrieve question replies - " + e.getMessage());
            e.printStackTrace();
        }

        questionDetails.getChildren().addAll(questionReplyField, replyQuestionButton, questionRepliesBox);

        // ** Display Answers **
        System.out.println("DEBUG: Fetching answers for question ID: " + selectedQuestion.getId());
        VBox answerContainer = new VBox(10);
        answerContainer.setStyle("-fx-padding: 10;");

        try {
            List<Answer> answerList = databaseHelper.getAnswersByQId(this.number);
            System.out.println("DEBUG: Found " + answerList.size() + " answers.");

            for (Answer answerObj : answerList) {
                Label answerLabel = new Label("Answer:");
                TextArea answerText = new TextArea(answerObj.getText());
                answerText.setEditable(false);
                answerText.setWrapText(true);

                VBox singleAnswer = new VBox(5, answerLabel, answerText);
                singleAnswer.setStyle("-fx-border-color: black; -fx-padding: 5;");

                answerContainer.getChildren().add(singleAnswer);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to retrieve answers - " + e.getMessage());
            e.printStackTrace();
        }

        questionDetails.getChildren().add(answerContainer);

        // Removed duplicate button container addition to prevent re-adding title and text

        layout.getChildren().addAll(listQuestion, scrollPane, backButton);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Question View");
        primaryStage.show();
    }
}