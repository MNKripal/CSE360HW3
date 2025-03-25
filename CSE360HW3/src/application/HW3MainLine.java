package application;

import static org.junit.Assert.*;
import org.junit.Test;

public class HW3MainLine {

    // ===== Invalid Username Tests =====

    /**
     * Test the case when the input is empty.
     * Expected: error message indicating the input is empty.
     */
    @Test
    public void testEmptyInput() {
        String input = "";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nThe username input is empty", result);
    }

    /**
     * Test a username that starts with an invalid character (a digit).
     * Expected: error message indicating that the username must start with a letter.
     */
    @Test
    public void testInvalidStartingCharacter() {
        String input = "1John";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA username must start with a letter (A-Z, a-z).\n", result);
    }

    /**
     * Test a username that is too short (less than 4 characters).
     * Expected: error message indicating that the username is too short.
     */
    @Test
    public void testTooShortUsername() {
        String input = "Ab1";  // Only 3 characters
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA username must have at least 4 characters.\n", result);
    }

    /**
     * Test a username that is too long (more than 16 characters).
     * Expected: error message indicating that the username is too long.
     */
    @Test
    public void testTooLongUsername() {
        String input = "John1234567890123";  // 17 characters
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA username must have no more than 16 character.\n", result);
    }

    /**
     * Test a username that ends with a special character (e.g., '.').
     * Expected: error message indicating that a special character must be followed by an alphanumeric.
     */
    @Test
    public void testSpecialCharacterAtEnd() {
        String input = "John.";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nAny special character (., _, -) in the username must be followed by A-Z, a-z, 0-9.\n", result);
    }
}