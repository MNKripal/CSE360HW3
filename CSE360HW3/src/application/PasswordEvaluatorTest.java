package application;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordEvaluatorTest {

    /**
     * Test that an empty password returns the expected error message.
     */
    @Test
    public void testEmptyPassword() {
        String input = "";
        String result = PasswordEvaluator.evaluatePassword(input);
        assertEquals("*** Error *** The password is empty!", result);
    }

    /**
     * Test that a password containing an invalid character (e.g., a space)
     * returns the expected error message.
     */
    @Test
    public void testInvalidCharacter() {
        // Space is not allowed in the password.
        String input = "Pass word1!";
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "An invalid character has been found in your password , only the following characters are allowed: A-Z, a-z, 0-9, ~`!@#$%^&*()_+{}[]:,.?/";
        assertEquals(expected, result);
    }

    /**
     * Test that a password missing an uppercase letter returns the expected error message.
     */
    @Test
    public void testMissingUpperCase() {
        // All lower case, with digit and special character present.
        String input = "password1!";
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "The following requirements weren't met for your password: Upper case; ";
        assertEquals(expected, result);
    }

    /**
     * Test that a password missing a lowercase letter returns the expected error message.
     */
    @Test
    public void testMissingLowerCase() {
        // All upper case, with digit and special character present.
        String input = "PASSWORD1!";
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "The following requirements weren't met for your password: Lower case; ";
        assertEquals(expected, result);
    }

    /**
     * Test that a password missing a numeric digit returns the expected error message.
     */
    @Test
    public void testMissingNumericDigit() {
        // Contains upper case, lower case, and special character, but no digit.
        String input = "Password!";
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "The following requirements weren't met for your password: Numeric digits; ";
        assertEquals(expected, result);
    }

    /**
     * Test that a password missing a special character returns the expected error message.
     */
    @Test
    public void testMissingSpecialCharacter() {
        // Contains upper case, lower case, and digit, but no allowed special character.
        String input = "Password1";
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "The following requirements weren't met for your password: Special character; ";
        assertEquals(expected, result);
    }

    /**
     * Test that a password which is not long enough (i.e., less than 8 characters)
     * returns the expected error message.
     */
    @Test
    public void testNotLongEnough() {
        // Contains all character types but only 4 characters in length.
        String input = "P1!a";
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "The following requirements weren't met for your password: Long Enough (<8 letters); ";
        assertEquals(expected, result);
    }
    
    /**
     * Test that a password that is exactly 7 characters long is rejected as too short.
     */
    @Test
    public void testNotLongEnoughExactlySeven() {
        // 7 characters: even if it contains upper, lower, digit, and special character,
        // it should fail the length requirement.
        String input = "Pa1!aa1"; // 7 characters
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "The following requirements weren't met for your password: Long Enough (<8 letters); ";
        assertEquals(expected, result);
    }

    /**
     * Test that a password missing multiple requirements returns the expected combined error message.
     */
    @Test
    public void testMultipleMissingRequirements() {
        // "password" is 8 letters long and all lower case.
        // It is missing an uppercase letter, a numeric digit, and a special character.
        String input = "password";
        String result = PasswordEvaluator.evaluatePassword(input);
        String expected = "The following requirements weren't met for your password: Upper case; Numeric digits; Special character; ";
        assertEquals(expected, result);
    }

    /**
     * Test that a valid password meeting all requirements returns an empty string.
     */
    @Test
    public void testValidPassword() {
        // Valid password: contains at least one uppercase letter, one lowercase letter,
        // one digit, one special character, and is at least 8 characters long.
        String input = "Password1!";
        String result = PasswordEvaluator.evaluatePassword(input);
        assertEquals("", result);
    }
    
    
    /**
     * Test that a valid password that includes several allowed special characters returns an empty string.
     */
    @Test
    public void testValidPasswordAllAllowedSpecials() {
        // This password uses multiple allowed special characters: ~, `, !, @, and #.
        String input = "Aa1~`!@#";  // 8 characters
        String result = PasswordEvaluator.evaluatePassword(input);
        assertEquals("", result);
    }
    
    /**
     * Test that a longer valid password returns an empty string.
     */
    @Test
    public void testValidPasswordLonger() {
        // A longer password that meets all requirements.
        String input = "ComplexPass123!@#";
        String result = PasswordEvaluator.evaluatePassword(input);
        assertEquals("", result);
    }
}
