package application;

import java.util.List;
import application.Answer;

//this is the answerList class, it is here to meet client specification but is not explicitly used since specifications never said it needed to be used (merely that it had to exist)
//implementing this code requires more overhead than just creating a list variable - if the client has a problem with this, inform me and it can be changed in a future version
public class AnswerList {
	public List<Answer> answerList;
	
	AnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}
	
}