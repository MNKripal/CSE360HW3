package application.studentpages;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.After;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import application.Question;
import application.Answer;
import application.User;

public class crudtests {
    private DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Test Case: Add a question with valid title & text
     * Description: Verifies that a question with a valid title and text can be added to the database and retrieved successfully.
     */
    @Test
    public void testAddQuestion() throws SQLException {
        databaseHelper.connectToDatabase();
        String title = "Valid Question Title";
        String text = "Valid Question Text";
        int authorId = 42;

        databaseHelper.addQuestion(title, text, authorId);
        List<Question> questions = databaseHelper.getQuestionByTitle(title, "Recent");

        assertFalse(questions.isEmpty());
        assertEquals(title, questions.get(0).getTitle());
    }

    /**
     * Test Case: Add a question with a title but null text
     * Description: Ensures that a question with a valid title but null text can still be added to the database.
     */
    @Test
    public void testAddQuestionWithNullText() throws SQLException {
        databaseHelper.connectToDatabase();
        String title = "Question With Null Text";
        
        databaseHelper.addQuestion(title, null, 42);
        List<Question> questions = databaseHelper.getQuestionByTitle(title, "Recent");

        assertFalse(questions.isEmpty());
    }

    /**
     * Test Case: Add a question with no title (should fail)
     * Description: Confirms that attempting to add a question with a null title results in an SQLException.
     */
    @Test(expected = SQLException.class)
    public void testAddQuestionNoTitle() throws SQLException {
        databaseHelper.connectToDatabase();
        databaseHelper.addQuestion(null, "Some text", 42);
    }

    /**
     * Test Case: Retrieve all questions
     * Description: Verifies that the method to retrieve all questions from the database returns a non-null list.
     */
    @Test
    public void testGetAllQuestions() throws SQLException {
        databaseHelper.connectToDatabase();
        List<Question> questions = databaseHelper.getAllQuestions();
        assertNotNull(questions);
    }

    /**
     * Test Case: Retrieve a specific question by ID
     * Description: Tests that a question can be added and then retrieved accurately using its unique ID.
     */
    @Test
    public void testGetQuestionById() throws SQLException {
        databaseHelper.connectToDatabase();
        String title = "Find by ID";
        String text = "This question should be retrievable";
        int authorId = 42;

        databaseHelper.addQuestion(title, text, authorId);
        List<Question> questions = databaseHelper.getQuestionByTitle(title, "Recent");

        assertFalse(questions.isEmpty());

        int questionId = questions.get(0).getId();
        Question foundQuestion = databaseHelper.getQuestionById(questionId);

        assertEquals(title, foundQuestion.getTitle());
    }

    /**
     * Test Case: Update a question
     * Description: Validates that an existing question can be updated with new title and text, and the changes are reflected in the database.
     */
    @Test
    public void testUpdateQuestion() throws SQLException {
        databaseHelper.connectToDatabase();
        String title = "Question to Update";
        String text = "Original Text";
        int authorId = 42;

        databaseHelper.addQuestion(title, text, authorId);
        List<Question> questions = databaseHelper.getQuestionByTitle(title, "Recent");

        assertFalse("Question was not added successfully.", questions.isEmpty());

        int questionId = questions.get(0).getId();
        //System.out.println("Updating Question ID: " + questionId);

        boolean updated = databaseHelper.updateQuestionById(questionId, "Updated Title", "Updated Text");
        assertTrue("Update failed, check if ID exists.", updated);

        // Fetch question again after updating
        Question updatedQuestion = databaseHelper.getQuestionById(questionId);
        assertEquals("Updated Title", updatedQuestion.getTitle());
        assertEquals("Updated Text", updatedQuestion.getText());
    }
    
    /**
     * Test Case: Search for a question by title
     * Description: Ensures that a question added to the database can be retrieved by searching for its title.
     */
    @Test
    public void testSearchQuestionByTitle() throws SQLException {
        databaseHelper.connectToDatabase();
        String title = "Unique Search Test Title";
        String text = "This is a search test.";
        int authorId = 42;

        // Add a question
        databaseHelper.addQuestion(title, text, authorId);
        
        // Perform a search by title
        List<Question> questions = databaseHelper.getQuestionByTitle(title, "Recent");

        // Validate that at least one result is returned and the title matches
        assertFalse("Search returned no results.", questions.isEmpty());
        assertEquals("Title mismatch in search results.", title, questions.get(0).getTitle());
    }

    /**
     * Test Case: Delete a question
     * Description: Confirms that a question can be added and then successfully deleted from the database.
     */
    @Test
    public void testDeleteQuestion() throws SQLException {
        databaseHelper.connectToDatabase();
        String title = "Question To Be Deleted";
        databaseHelper.addQuestion(title, "Some text", 42);

        List<Question> questions = databaseHelper.getQuestionByTitle(title, "Recent");
        int questionId = questions.get(0).getId();

        boolean deleted = databaseHelper.deleteQuestion(questionId);
        assertTrue(deleted);
    }

    /**
     * Test Case: Attempt to delete a non-existent question
     * Description: Verifies that attempting to delete a question with a non-existent ID returns false.
     */
    @Test
    public void testDeleteNonExistentQuestion() throws SQLException {
        databaseHelper.connectToDatabase();
        boolean result = databaseHelper.deleteQuestion(999999); // Fake ID
        assertFalse(result);
    }

    /**
     * Test Case: Mark a question as resolved
     * Description: Tests that a question can be marked as resolved and the operation completes successfully.
     */
    @Test
    public void testResolveQuestion() throws SQLException {
        databaseHelper.connectToDatabase();
        String title = "Question to Resolve";
        databaseHelper.addQuestion(title, "Some text", 42);

        List<Question> questions = databaseHelper.getQuestionByTitle(title, "Recent");
        int questionId = questions.get(0).getId();

        boolean resolved = databaseHelper.resolveQuestion(questionId);
        assertTrue(resolved);
    }

    // ----------------------------------------
    // ANSWER TEST CASES
    // ----------------------------------------

    /**
     * Test Case: Add a valid answer
     * Description: Verifies that an answer with valid text can be added to a question and retrieved from the database.
     */
    @Test
    public void testAddAnswer() throws SQLException {
        databaseHelper.connectToDatabase();
        String text = "Valid Answer";
        int questionId = 21;
        int author = 42;

        databaseHelper.addAnswer(text, questionId, author);
        List<Answer> answers = databaseHelper.getAnswersByQId(questionId);

        assertFalse(answers.isEmpty());
        assertEquals(text, answers.get(0).getText());
    }

    /**
     * Test Case: Add an answer with null text (should fail)
     * Description: Ensures that attempting to add an answer with null text results in an SQLException.
     */
    @Test(expected = SQLException.class)
    public void testAddAnswerNoText() throws SQLException {
        databaseHelper.connectToDatabase();
        databaseHelper.addAnswer(null, 21, 42);
    }

    /**
     * Test Case: Retrieve all answers
     * Description: Confirms that the method to retrieve all answers from the database returns a non-null list.
     */
    @Test
    public void testGetAllAnswers() throws SQLException {
        databaseHelper.connectToDatabase();
        List<Answer> answers = databaseHelper.getAllAnswers();
        assertNotNull(answers);
    }

    /**
     * Test Case: Update an answer
     * Description: Validates that an existing answer can be updated with new text and the changes are persisted in the database.
     */
    @Test
    public void testUpdateAnswer() throws SQLException {
        databaseHelper.connectToDatabase();
        String text = "Original Answer";
        int questionId = 21;
        int author = 42;

        databaseHelper.addAnswer(text, questionId, author);
        List<Answer> answers = databaseHelper.getAnswersByQId(questionId);

        assertFalse("Answer was not added successfully.", answers.isEmpty());

        int answerId = answers.get(0).getId();
        // System.out.println("Updating Answer ID: " + answerId);

        boolean updated = databaseHelper.updateAnswerById(answerId, "Updated Answer Text");
        assertTrue("Update failed, check if ID exists.", updated);

        // Fetch answer again after updating
        List<Answer> updatedAnswers = databaseHelper.getAnswersByQId(questionId);
        assertEquals("Updated Answer Text", updatedAnswers.get(0).getText());
    }

    /**
     * Test Case: Delete an answer
     * Description: Tests that an answer can be added and then successfully deleted from the database.
     */
    @Test
    public void testDeleteAnswer() throws SQLException {
        databaseHelper.connectToDatabase();
        String text = "Answer to be deleted";
        int questionId = 21;
        int author = 42;

        databaseHelper.addAnswer(text, questionId, author);
        List<Answer> answers = databaseHelper.getAnswersByQId(questionId);
        int answerId = answers.get(0).getId();

        boolean deleted = databaseHelper.deleteAnswer(answerId);
        assertTrue(deleted);
    }

    /**
     * Test Case: Attempt to delete a non-existent answer
     * Description: Verifies that attempting to delete an answer with a non-existent ID returns false.
     */
    @Test
    public void testDeleteNonExistentAnswer() throws SQLException {
        databaseHelper.connectToDatabase();
        boolean result = databaseHelper.deleteAnswer(999999); // Fake ID
        assertFalse(result);
    }

    /**
     * Test Case: Mark an answer as "worked"
     * Description: Confirms that an answer can be marked as "worked" and the operation completes successfully.
     */
    @Test
    public void testAnswerWorked() throws SQLException {
        databaseHelper.connectToDatabase();
        String text = "Answer to be marked as worked";
        int questionId = 21;
        int author = 42;

        databaseHelper.addAnswer(text, questionId, author);
        List<Answer> answers = databaseHelper.getAnswersByQId(questionId);
        int answerId = answers.get(0).getId();

        boolean worked = databaseHelper.answerWorked(answerId);
        assertTrue(worked);
    }
    
    /**
     * Test Case: Search for answers by question ID
     * Description: Ensures that answers linked to a specific question can be retrieved using its question ID.
     */
    @Test
    public void testSearchAnswersByQuestionId() throws SQLException {
        databaseHelper.connectToDatabase();
        String text = "Answer for search test";
        int questionId = 21; // Assume this exists
        int authorId = 42;

        databaseHelper.addAnswer(text, questionId, authorId);
        
        List<Answer> answers = databaseHelper.getAnswersByQId(questionId);

        assertFalse("Search returned no answers.", answers.isEmpty());
        assertEquals("Answer text mismatch in search results.", text, answers.get(0).getText());
    }

    /**
     * Cleanup After Each Test
     * Description: Ensures that test data created by author ID 42 is removed from the database after each test to maintain a clean state.
     */
    @After
    public void cleanup() {
        try {
            databaseHelper.connectToDatabase();
            databaseHelper.delete42();
            databaseHelper.closeConnection();
        } catch (Exception e) {
            System.out.println("Cleanup failed: " + e.getMessage());
        }
    }
}