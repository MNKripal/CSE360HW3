package application;

import java.util.List;
import java.util.ArrayList;

//Question object that holds the information for questions

public class Question {
	
	//data associated with the question
	private int id;
	private int parent;
	private int author;
	private String title;
	private String text;
	private boolean resolved;
	private List<Question> replies;  // Stores threaded replies
	
	//private final DatabaseHelper databaseHelper;
	//methods
	
	//constructor
	public Question(int author, String title, String text, boolean resolved, int parent, int id) {
        //this.databaseHelper = databaseHelper;
        this.id = id;
        this.title = title;
        this.text = text;
        this.resolved = resolved;
        this.parent = parent;
        this.author = author;
        this.replies = new ArrayList<>();
    }
	
	// Overloaded constructor without specified id
	public Question(int author, String title, String text, int parent) {
	    this.id = 0;
	    this.title = title;
	    this.text = text;
	    this.resolved = false;
	    this.parent = parent;  // <- Parent should remain unchanged
	    this.author = author;
	    this.replies = new ArrayList<>();  // <- Initialize replies list
	}

	// Overloaded constructor without parent
	public Question(int author, String title, String text) {
	    this.id = 0;
	    this.title = title;
	    this.text = text;
	    this.resolved = false;
	    this.parent = 0;
	    this.author = author;
	    this.replies = new ArrayList<>();  // <- Initialize replies list
	}

	
	//getters
	public int getId() {return this.id;}
	public int  getParent() {return this.parent;}
	public int getAuthor() {return this.author;}
	public String getTitle() {return this.title;}
	public String getText() {return this.text;}
	public boolean getResolved() {return this.resolved;}
	public List<Question> getReplies() { return this.replies; }
	
	// Add a reply to the question
		public void addReply(Question reply) {
			this.replies.add(reply);
		}
			
			
	//setters - for now, only factor that should need to change is resolving but since interaction happens with the database, I do not think even that is needed
	public void setResolved() {
		this.resolved = true;
	}
	
	public void setUnResolved() {
		this.resolved = false;
	}
}