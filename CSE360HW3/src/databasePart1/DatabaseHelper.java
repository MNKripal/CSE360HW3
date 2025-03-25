package databasePart1;
import java.sql.*;
import java.util.UUID;

import application.User;
import application.Answer;
import application.Question;

import java.util.ArrayList;
import java.util.List;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 * Added questions table, answers table to TP1 work and getAllQuestions() plus all following functions
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase;mode=MySQL";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "isTempPassword BOOLEAN DEFAULT FALSE,"
				+ "role VARCHAR(20))";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "role VARCHAR(20), "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);
	    
	    //Create the Questions table (added for HW2)
	    String questionsTable  = "CREATE TABLE IF NOT EXISTS cse360questions ("
	    		+ "cse360questions_id INT PRIMARY KEY AUTO_INCREMENT, "
	    		+ "cse360questions_title TEXT NOT NULL, "
	    		+ "cse360questions_text TEXT, "
	    		+ "cse360questions_resolved INT DEFAULT 0, "
	    		+ "cse360questions_parent INT, "
	    		+ "cse360questions_author INT NOT NULL)";
	    statement.execute(questionsTable);
	    
	    //Create teh Answers table (added for HW2)
	    String answersTable  = "CREATE TABLE IF NOT EXISTS answers ("
	    		+ "answers_id INT PRIMARY KEY AUTO_INCREMENT, "
	    		+ "answers_text TEXT NOT NULL, "
	    		+ "answers_worked INT DEFAULT 0, "
	    		+ "answers_questionId INT NOT NULL, "
	    		+"answers_authorId INT NOT NULL)";
	    statement.execute(answersTable);
	    
	    //add a studentTest user (for automated debugging HW2) - having a designated test id makes it easier to delete designated test questions/answers - ignore statement stops the query if the data is already entered
	    String addStudent = "INSERT INTO cse360users (id, username,  password, isTempPassword, role) VALUES (42, 'studentTest', 'test', 'FALSE', 'S') ON DUPLICATE KEY UPDATE role = 'S'";
	    statement.execute(addStudent);
	}
	
	// Remove a user, given a username
	public boolean removeUser(String userName) throws SQLException {
	    String removeUser = "DELETE FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(removeUser)) {
	        pstmt.setString(1, userName);
	        
	        int affectedRows = pstmt.executeUpdate(); 
	        return affectedRows > 0;
	    }
	}
	
	// Update password, given a username
	public boolean updatePassword(String userName, String newPassword) throws SQLException {
	    String updatePasswordQuery = "UPDATE cse360users SET password = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(updatePasswordQuery)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setString(2, userName);
	        
	        int affectedRows = pstmt.executeUpdate(); 
	        return affectedRows > 0;
	    }
	}
	
	public boolean updateUserRole(String username, String newRole) throws SQLException {
		String updateRoleQuery = "UPDATE cse360users SET role = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(updateRoleQuery)) {
	        pstmt.setString(1, newRole);
	        pstmt.setString(2, username);
	        
	        int affectedRows = pstmt.executeUpdate(); 
	        return affectedRows > 0;
	    }
	}
	
	public boolean userHasTempPassword(String username) throws SQLException {
		String hasTempPassQuery = "SELECT isTempPassword FROM cse360users WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(hasTempPassQuery)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getBoolean("isTempPassword"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
		
	}
	
	public boolean updatePassword(String userName, String newPassword, boolean isTemp) throws SQLException {
	    String updatePasswordQuery = "UPDATE cse360users SET password = ?, isTempPassword = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(updatePasswordQuery)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setBoolean(2, isTemp);
	        pstmt.setString(3, userName);
	        
	        int affectedRows = pstmt.executeUpdate();
	        return affectedRows > 0;
	    }
	}
	
	// Return List of all Users in the database, as objects
	public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String getUsersQuery = "SELECT userName, password, role FROM cse360users";

        try (PreparedStatement pstmt = connection.prepareStatement(getUsersQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("userName");
                String password = rs.getString("password");
                String role = rs.getString("role");

                users.add(new User(userName, password, role));
            }
        }

        return users; 
    }
	


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users WHERE id != 42";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			pstmt.executeUpdate();
		}
	}

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) throws SQLException {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	//gets the userId to be used for having a consistent number passed through the system
	public int getUserId(String userName) {
	    String query = "SELECT id FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt("id"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0; // If no user exists or an error occurs
	}
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String role) {
	    String code = UUID.randomUUID().toString().substring(0, 6); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code,role) VALUES (?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, role);
	        pstmt.executeUpdate();
	        System.out.println("created inv with role: " + role);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public String validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            System.out.println("validated inv with role: " + rs.getString("role"));
	            return rs.getString("role");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
	// Return List of all Quesitons in the Database as elements of a list
	public List<Question> getAllQuestions() throws SQLException {
	    List<Question> questions = new ArrayList<>();
	    String query = "SELECT * FROM cse360questions"; // Fetch everything for debugging

	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            int id = rs.getInt("cse360questions_id");
	            int parent = rs.getInt("cse360questions_parent");
	            int author = rs.getInt("cse360questions_author");
	            String title = rs.getString("cse360questions_title");
	            String text = rs.getString("cse360questions_text");
	            boolean resolved = rs.getBoolean("cse360questions_resolved");

	            // Print everything for debugging
	            System.out.println("DEBUG: Question ID: " + id + ", Parent: " + parent + ", Title: " + title);

	            if (parent == 0) { // Only top-level questions
	                questions.add(new Question(author, title, text, resolved, parent, id));
	            }
	        }
	    }

	    System.out.println("DEBUG: Total Questions Retrieved (after filtering) = " + questions.size());
	    return questions;
	}




	//Gets a single question by Id input
	public Question getQuestionById(int number) throws SQLException {
	    List<Question> questionList = new ArrayList<>();
	    String getUsersQuery = "SELECT * FROM cse360questions WHERE cse360questions_id = ?";

	    try {
	    	PreparedStatement pstmt = connection.prepareStatement(getUsersQuery); 
	    	pstmt.setString(1, String.valueOf(number));
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            
	            int id = rs.getInt("cse360questions_id");
	        	int parent = rs.getInt("cse360questions_parent");
	        	int author = rs.getInt("cse360questions_author");
	        	String title = rs.getString("cse360questions_title");
	        	String text = rs.getString("cse360questions_text");
	        	boolean resolved =  rs.getBoolean("cse360questions_resolved");

	            questionList.add(new Question (author, title, text, resolved, parent, id));
	        }
	        Question question = questionList.get(0);//send the first match back to the user

		    return question;
	    } finally {}//finally block is for pretend so Eclipse does not yell at me
	}
	
	// update the title or text of a question 
	public boolean updateQuestionById(int id, String title, String text) throws SQLException {
		String updateQuestionQuery = "UPDATE cse360questions SET cse360questions_title=?, cse360questions_text=? WHERE cse360questions_id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(updateQuestionQuery)) {
	        pstmt.setString(1, title);
	        pstmt.setString(2, text);
	        pstmt.setInt(3, id);
	        
	        int affectedRows = pstmt.executeUpdate(); 
	        return affectedRows > 0;
	    }
	}
	
	// delete a question
	public boolean deleteQuestion(int questionId) throws SQLException {
	    String deleteQuestionQuery = "DELETE FROM cse360questions WHERE cse360questions_id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(deleteQuestionQuery)) {
	        pstmt.setInt(1, questionId);
	        int affectedRows = pstmt.executeUpdate();
	        return affectedRows > 0;
	    }
	}
	
	//gets quesitons by matching title
	public List <Question> getQuestionByTitle(String name, String sort) throws SQLException {
	    List<Question> questionList = new ArrayList<>();
	    String getUsersQuery;
	    if (sort.equals("Recent")) {
	    	getUsersQuery = "SELECT * FROM cse360questions WHERE cse360questions_title LIKE ? ORDER BY cse360questions_id DESC";
	    }else if (sort.equals("Resolved")) {
	    	getUsersQuery = "SELECT * FROM cse360questions WHERE cse360questions_title LIKE ? ORDER BY cse360questions_resolved DESC";
	    }else if (sort.equals("Unresolved")) {
	    	getUsersQuery = "SELECT * FROM cse360questions WHERE cse360questions_title LIKE ? ORDER BY cse360questions_resolved ASC";
	    }else {
	    	getUsersQuery = "SELECT * FROM cse360questions WHERE cse360questions_title LIKE ?";
	    }
	    try {
	    	PreparedStatement pstmt = connection.prepareStatement(getUsersQuery); 
	    	pstmt.setString(1, name);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            
	            int id = rs.getInt("cse360questions_id");
	        	int parent = rs.getInt("cse360questions_parent");
	        	int author = rs.getInt("cse360questions_author");
	        	String title = rs.getString("cse360questions_title");
	        	String text = rs.getString("cse360questions_text");
	        	boolean resolved =  rs.getBoolean("cse360questions_resolved");

	            questionList.add(new Question (author, title, text, resolved, parent, id));
	        }

		    return questionList;
	    } finally {}//finally block is for pretend so Eclipse does not yell at me
	}

	//Return List of all answers in the database as element of a list
	public List<Answer> getAllAnswers() throws SQLException {
	    List<Answer> answerList = new ArrayList<>();
	    String getUsersQuery = "SELECT * FROM answers";

	    try (PreparedStatement pstmt = connection.prepareStatement(getUsersQuery);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            
	            int id = rs.getInt("answers_id");
	            int author = rs.getInt("answers_authorId");
	        	int questionId = rs.getInt("answers_questionId");
	        	String text = rs.getString("answers_text");
	        	boolean worked =  rs.getBoolean("answers_worked");

	            answerList.add(new Answer (author, id, text, worked, questionId));
	        }
	    }

	    return answerList; 
	}
	
	// delete an answer
	public boolean deleteAnswer(int answerId) throws SQLException {
	    String deleteAnswerQuery = "DELETE FROM answers WHERE answers_id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(deleteAnswerQuery)) {
	        pstmt.setInt(1, answerId);
	        int affectedRows = pstmt.executeUpdate();
	        return affectedRows > 0; // Return true if deletion was successful
	    }
	}

	
	//provides a list of the answers associated to a particular quesiton id
	public List<Answer> getAnswersByQId(int number) throws SQLException {
	    List<Answer> answerList = new ArrayList<>();
	    String getUsersQuery = "SELECT * FROM answers WHERE answers_questionId = ? ORDER BY answers_worked DESC, answers_id DESC";

	    try {
	    	PreparedStatement pstmt = connection.prepareStatement(getUsersQuery); 
	    	pstmt.setString(1, String.valueOf(number));
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            
	            int id = rs.getInt("answers_id");
	        	int questionId = rs.getInt("answers_questionId");
	        	int author = rs.getInt("answers_authorId");
	        	String text = rs.getString("answers_text");
	        	boolean worked =  rs.getBoolean("answers_worked");

	            answerList.add(new Answer (author, id, text, worked, questionId));
	        }

		    return answerList;
	    } finally {}//finally block is for pretend so Eclipse does not yell at me
	}
	
	// Retrieve replies for a specific answer (threaded replies)
	public List<Answer> getAnswerReplies(int answerId) throws SQLException {
	    List<Answer> replies = new ArrayList<>();
	    String query = "SELECT * FROM answers WHERE answers_questionId = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, answerId);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("answers_id");
	            int questionId = rs.getInt("answers_questionId");
	            int author = rs.getInt("answers_authorId");
	            String text = rs.getString("answers_text");
	            boolean worked = rs.getBoolean("answers_worked");

	            Answer reply = new Answer(author, id, text, worked, questionId);
	            replies.add(reply);
	        }
	    }
	    return replies;
	}
	
	// update the answer text
	public boolean updateAnswerById(int id, String text) throws SQLException {
		String updateAnswerQuery = "UPDATE answers SET answers_text=? WHERE answers_id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(updateAnswerQuery)) {
	        pstmt.setString(1, text);
	        pstmt.setInt(2, id);
	        
	        int affectedRows = pstmt.executeUpdate(); 
	        return affectedRows > 0;
	    }
	}
	
	// Retrieve replies for a specific question (threaded replies)
	public List<Question> getQuestionReplies(int questionId) throws SQLException {
	    List<Question> replies = new ArrayList<>();
	    String query = "SELECT * FROM cse360questions WHERE cse360questions_parent = ?"; // 🔹 Only retrieve replies

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, questionId);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("cse360questions_id");
	            int parent = rs.getInt("cse360questions_parent");
	            int author = rs.getInt("cse360questions_author");
	            String text = rs.getString("cse360questions_text");
	            boolean resolved = rs.getBoolean("cse360questions_resolved");

	            replies.add(new Question(author, "", text, resolved, parent, id)); // 🔹 No title for replies
	        }
	    }

	    System.out.println("DEBUG: Retrieved " + replies.size() + " replies for question ID " + questionId);
	    return replies;
	}


	
	//adds a question via a question object
	public void addQuestion(Question newQuestion) throws SQLException {
		//String insertUser = "INSERT INTO cse360questions (title, question, resolved, author) VALUES (?, ?, ?)";
		String insertUser = "INSERT INTO cse360questions (cse360questions_title, cse360questions_text, cse360questions_author) VALUES (?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, newQuestion.getTitle());
			pstmt.setString(2, newQuestion.getText());
			pstmt.setString(3, Integer.toString(newQuestion.getAuthor()));
			//pstmt.setString(3, newQuestion.getResolved());
			pstmt.executeUpdate();
		}
	}
	
	//adds a question via title, text and author
	public void addQuestion(String title, String text, int author) throws SQLException {
		//String insertUser = "INSERT INTO cse360questions (title, question, resolved) VALUES (?, ?, ?)";
		String insertUser = "INSERT INTO cse360questions (cse360questions_title, cse360questions_text, cse360questions_author) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, title);
			pstmt.setString(2, text);
			pstmt.setString(3, Integer.toString(author));
			//pstmt.setString(3, newQuestion.getResolved());
			pstmt.executeUpdate();
		}
	}
	
	//add answer by answer object
	public void addAnswer(Answer newAnswer) throws SQLException {
		String insertUser = "INSERT INTO answers (answefrs_text, answers_questionId, answers_authorId) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, newAnswer.getText());
			pstmt.setString(2, Integer.toString(newAnswer.getQuestionId()));
			pstmt.setString(3, Integer.toString(newAnswer.getAuthor()));
			pstmt.executeUpdate();
		}
	}
	
	//add answer by text, question id and author id
	public void addAnswer(String text, int questionId, int author) throws SQLException {
		//String insertUser = "INSERT INTO cse360questions (title, question, resolved) VALUES (?, ?, ?)";
		String insertUser = "INSERT INTO answers (answers_text, answers_questionId, answers_authorId) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, text);
			pstmt.setString(2, Integer.toString(questionId));
			pstmt.setString(3, Integer.toString(author));
			pstmt.executeUpdate();
		}
	}
	// Adds a reply to a question (treated as a new question with a parent ID)
	public void addQuestionReply(String text, int parentId, int authorId) throws SQLException {
	    String sql = "INSERT INTO cse360questions (cse360questions_parent, cse360questions_author, cse360questions_text, cse360questions_title, cse360questions_resolved) VALUES (?, ?, ?, ?, false)";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, parentId);  // Parent ID (should link reply to main question)
	        stmt.setInt(2, authorId);  // User ID of the reply author
	        stmt.setString(3, text);   // Reply text
	        stmt.setString(4, "Reply"); // Instead of NULL, use a placeholder title
	        
	        stmt.executeUpdate();
	    }

	    System.out.println("DEBUG: Added reply to question ID " + parentId);
	}




	// Adds a reply to an answer (treated as a new answer with a parent ID)
	public void addAnswerReply(String text, int answerId, int author) throws SQLException {
	    String query = "INSERT INTO answers (answers_text, answers_questionId, answers_authorId) VALUES (?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, text);
	        pstmt.setInt(2, answerId);
	        pstmt.setInt(3, author);
	        pstmt.executeUpdate();
	    }
	}
	
	
	//Question resolver
	public boolean resolveQuestion(int id) throws SQLException{
		String resolve = "UPDATE cse360questions SET cse360questions_resolved = 1 WHERE cse360questions_id = ?";
		 try (PreparedStatement pstmt = connection.prepareStatement(resolve)) {
		        pstmt.setString(1, Integer.toString(id));
		        
		        int affectedRows = pstmt.executeUpdate(); 
		        return affectedRows > 0;
		    }
		
	}
	
	//updates table to show answer worked
	public boolean answerWorked(int id) throws SQLException{
		String resolve = "UPDATE answers SET answers_worked = 1 WHERE answers_id = ?";
		 try (PreparedStatement pstmt = connection.prepareStatement(resolve)) {
		        pstmt.setString(1, Integer.toString(id));
		        
		        int affectedRows = pstmt.executeUpdate(); 
		        return affectedRows > 0;
		    }
		
	}
	
	//this is a deleteFunction which is used for automated test procedures to delete questions and answers created by the test user
	public boolean delete42() throws SQLException {
	    String removeQuestions = "DELETE FROM cse360questions WHERE cse360questions_author = 42; DELETE FROM answers WHERE answers_authorId = 42";
	    try {
	    	int affectedRows = statement.executeUpdate(removeQuestions);
	        //pstmt.setString(1, number);
	        
//	        int affectedRows = pstmt.executeUpdate(); 
	        return affectedRows > 0;
	    }finally {}
	}
	
	//this is a special getQuestionByAuthor method that specifically gets a question written by test student.  It is used in checking qustion resolve capabilities
	public Question getTestQuestion() throws SQLException{
		 List<Question> questionList = new ArrayList<>();
		    String getUsersQuery = "SELECT * FROM cse360questions WHERE cse360questions_author = 42";

		    try {
		    	PreparedStatement pstmt = connection.prepareStatement(getUsersQuery); 
		        ResultSet rs = pstmt.executeQuery();

		        while (rs.next()) {
		            
		            int id = rs.getInt("cse360questions_id");
		        	int parent = rs.getInt("cse360questions_parent");
		        	int author = rs.getInt("cse360questions_author");
		        	String title = rs.getString("cse360questions_title");
		        	String text = rs.getString("cse360questions_text");
		        	boolean resolved =  rs.getBoolean("cse360questions_resolved");

		            questionList.add(new Question (author, title, text, resolved, parent, id));
		        }
		        Question question = questionList.get(0);//send the first match back to the user

			    return question;
		    } finally {}//finally block is for pretend so Eclipse does not yell at me, it seems like a strange bug in my Eclipse
	}
	//special getAnswerByAuthor method that gets an answer written by the student.  It is used in automated tesing procedures
	public Answer getTestAnswer() throws SQLException{
		 List<Answer> answerList = new ArrayList<>();
		    String getUsersQuery = "SELECT * FROM answers WHERE answers_authorId = 42";

		    try {
		    	PreparedStatement pstmt = connection.prepareStatement(getUsersQuery); 
		        ResultSet rs = pstmt.executeQuery();

		        while (rs.next()) {
		            
		        	int id = rs.getInt("answers_id");
			        int questionId = rs.getInt("answers_questionId");
			        int author = rs.getInt("answers_authorId");
			        String text = rs.getString("answers_text");
			        boolean worked =  rs.getBoolean("answers_worked");

			        answerList.add(new Answer (author, id, text, worked, questionId));
		        }
		        Answer answer = answerList.get(0);//send the first match back to the user

			    return answer;
		    } finally {}//finally block is for pretend so Eclipse does not yell at me, it seems like a strange bug in my Eclipse
	}
}



