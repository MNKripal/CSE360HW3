package application.studentpages;

import java.sql.SQLException;
import application.User;
import application.Answer;
import databasePart1.DatabaseHelper;
import application.Question;

/*
 * *This validates the resolve and worked buttons
 */

public class RWValidation {
	
	//resolve validator
	public void resolve(DatabaseHelper databaseHelper, Question question, User user) throws SQLException {
		try {
			//check the user data for the quesiton passed to ensure that the user has created the question - a second layer of validation
			if (question.getAuthor() == user.getId()) {
				databaseHelper.resolveQuestion(question.getId());
			} else {
				throw new SQLException("Invalid party to resolve");
			}
		}catch(NullPointerException e) {//catching nullpointer exeptions to shut up eclipse and this originally caught the SQL Exception errors but that did not play nice with the automated testing 
			System.out.println("oops");
			System.out.println(e);
		}
	}
	
	public void worked(DatabaseHelper databaseHelper, Answer answer, User user) throws SQLException {
		try {
			//checks the user data for the answer passed to ensure that the person trying to state worked is not the answer provider
			if (answer.getAuthor() != user.getId()) {
				databaseHelper.answerWorked(answer.getId());
			}else {
				throw new SQLException("Invalid party to resolve");
			}
		}catch(NullPointerException e) {//catching nullpointer exeptions to shut up eclipse and this originally caught the SQL Exception errors but that did not play nice with the automated testing 
			System.out.println("oops");
			System.out.println(e);
		}
	}
}