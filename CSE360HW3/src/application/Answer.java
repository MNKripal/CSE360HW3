package application;

import java.util.List;
import java.util.ArrayList;

public class Answer {
	
	// Data associated with the question
	private int id;
	private int questionId;
	private int author;
	private String text;
	private boolean worked;
	private List<Answer> replies; // Store threaded replies
	
	//private final DatabaseHelper databaseHelper;
	//methods
	
	// Constructor with all fields
	public Answer(int author, int id, String text, boolean worked, int questionId) {
        //this.databaseHelper = databaseHelper;
        this.id = id;
        this.author = author;
        this.text = text;
        this.worked = worked;
        this.questionId = questionId;
        this.replies = new ArrayList<>(); // Initialize replies list
    }
	
	// Answer without specified worked
	public Answer(int author, int id, String text, int questionId) {
        //this.databaseHelper = databaseHelper;
        this.id = id;
        this.author = author;
        this.text = text;
        this.worked = false;
        this.questionId = questionId;
        this.replies = new ArrayList<>(); // Initialize replies list
    }
	
	// Answer without id but worked
	public Answer(int author, String text, boolean worked, int questionId) {
        //this.databaseHelper = databaseHelper;
        this.id = 0;
        this.author = author;
        this.text = text;
        this.worked = worked;
        this.questionId = questionId;
        this.replies = new ArrayList<>(); // Initialize replies list
    }
	
	// Answer without id or worked
	public Answer (int author, String text, int questionId) {
        //this.databaseHelper = databaseHelper;
        this.id = 0;
        this.author = author;
        this.text = text;
        this.worked = false;
        this.questionId = questionId;
        this.replies = new ArrayList<>(); // Initialize replies list
    }
	
	// Getters
	public int getAuthor() { return this.author; }
	public int getId() { return this.id; }
	public int getQuestionId() { return this.questionId; }
	public String getText() { return this.text; }
	public boolean getWorked() { return this.worked; }
	public List<Answer> getReplies() { return this.replies; }
	
	// Add a reply to the answer
	public void addReply(Answer reply) {
		this.replies.add(reply);
	}
	
	// Setters
	public void setWorked() {
		this.worked = true;
	}
	
	public void clearWorked() {
		this.worked = false;
	}
}
