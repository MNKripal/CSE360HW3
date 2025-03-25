package application;

import static org.junit.Assert.*;
import org.junit.Test;

public class UserNameRecognizerTest {

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

    /**
     * Test a username containing an invalid character in the middle.
     * Expected: error message indicating that only A-Z, a-z, 0-9 (and the allowed specials) are allowed.
     */
    @Test
    public void testInvalidCharacterInMiddle() {
        String input = "John#Doe";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA UserName character may only contain the characters A-Z, a-z, 0-9, '.', '_', '-'.\n", result);
    }

    /**
     * Test a username with a trailing space.
     * Expected: error message because space is not a valid character.
     */
    @Test
    public void testInvalidTrailingSpace() {
        String input = "John ";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA UserName character may only contain the characters A-Z, a-z, 0-9, '.', '_', '-'.\n", result);
    }
    
    /**
     * Test a username with consecutive special characters.
     * Expected: error message because a special character must be followed by an alphanumeric.
     */
    @Test
    public void testUsernameWithConsecutiveSpecials() {
        String input = "John..Doe";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nAny special character (., _, -) in the username must be followed by A-Z, a-z, 0-9.\n", result);
    }
    
    /**
     * Test a username with a leading space.
     * Expected: error message because a username must start with a letter.
     */
    @Test
    public void testUsernameWithLeadingSpace() {
        String input = " JohnDoe";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA username must start with a letter (A-Z, a-z).\n", result);
    }
    
    /**
     * Test a username with a space in the middle.
     * Expected: error message because space is not allowed.
     */
    @Test
    public void testUsernameWithSpaceInMiddle() {
        String input = "John Doe";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA UserName character may only contain the characters A-Z, a-z, 0-9, '.', '_', '-'.\n", result);
    }
    
    
    /**
     * Test a username that starts with a special character.
     * Expected: error message indicating that a username must start with a letter.
     */
    @Test
    public void testUsernameStartsWithSpecialChar() {
        String input = ".JohnDoe";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("\nA username must start with a letter (A-Z, a-z).\n", result);
    }
    
    
    // ===== Valid Username Tests =====

    /**
     * Test a simple valid username.
     * Expected: an empty string indicating no error.
     */
    @Test
    public void testValidUserNameSimple() {
        String input = "John123";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a valid username that uses a special character correctly.
     * Expected: an empty string indicating the username is valid.
     */
    @Test
    public void testValidUserNameWithSpecialCharacters() {
        String input = "John.Doe";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a valid username that uses underscore and hyphen in valid positions.
     * Expected: an empty string indicating the username is valid.
     */
    @Test
    public void testValidUserNameWithUnderscoreAndHyphen() {
        String input = "Jane_Doe-123";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a username that is exactly 4 characters long.
     * Expected: an empty string indicating the username is valid.
     */
    @Test
    public void testExactBoundaryUsername4() {
        String input = "John"; // Exactly 4 characters
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a username that is exactly 16 characters long.
     * Expected: an empty string indicating the username is valid.
     */
    @Test
    public void testExactBoundaryUsername16() {
        String input = "JohnDoe123456789"; // Exactly 16 characters
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a valid username starting with a lowercase letter.
     */
    @Test
    public void testValidUserNameStartingLowerCase() {
        String input = "johnDoe123";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a valid username with mixed upper and lower-case letters.
     */
    @Test
    public void testValidUserNameMixedCase() {
        String input = "AliceBob";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a valid username with multiple special characters in valid positions.
     */
    @Test
    public void testValidUserNameMultipleSpecials() {
        String input = "john.Doe_smith-9";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a valid username where digits appear in the middle.
     */
    @Test
    public void testValidUserNameDigitsInMiddle() {
        String input = "A1b2C3d4";
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

    /**
     * Test a valid username exactly 4 characters long starting with a lowercase letter.
     */
    @Test
    public void testExactBoundaryUsername4LowerCase() {
        String input = "aB1c";  // Exactly 4 characters starting with a lowercase letter
        String result = UserNameRecognizer.checkForValidUserName(input);
        assertEquals("", result);
    }

}
