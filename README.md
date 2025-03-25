# CSE360HW3 : Automated Testing & Javadoc


Overview
This project is the third individual assignment for CSE360, designed to enhance skills in automated testing and internal documentation using Javadoc. It builds upon components from Team Project 2 (TP2), specifically the UserNameRecognizer class, by implementing a standalone test suite to validate username requirements.

The primary focus of this assignment is to ensure that the UserNameRecognizer class correctly processes various username inputs by enforcing predefined constraints and validation rules. This is achieved through a set of JUnit test cases, which systematically check for edge cases such as empty inputs, invalid starting characters, length restrictions, and improper use of special characters.

The project focuses on the following key areas:

- Manual testing through a main() method, providing an alternative approach to JUnit-based testing

- Professional internal documentation following Javadoc standards to ensure clear and consistent code explanations

- Screencasting the test execution and reviewing the documentation to demonstrate and validate the testing process

Features Implemented
1. Standalone Automated Testing
A standalone test runner has been implemented in HW3Mainline.java, which executes five selected test cases using the main() method and provides clear PASS/FAIL results for each test.

2. Username Testing
Each test case is designed to validate a specific username requirement, including: minimum length, inclusion of uppercase letters, digits, and special characters, correct handling of valid and invalid combinations

3. Javadoc Documentation
All methods in the test runner are thoroughly documented using Javadoc comments, including detailed descriptions and @param tags. The generated Javadoc HTML output is included for review and reference.

How to Run the Project

Prerequisites:
-To run this project, ensure the following requirements are met:

-A Java-compatible IDE (e.g., Eclipse, IntelliJ IDEA)

-Java 17 or higher (recommended) installed on your machine

Steps to Run:

-Clone or download this repository.

-Open the project in your IDE.

-Navigate to: src/application/HW3Mainline.java

-Right-click and run the file as a Java application.

-Observe the PASS/FAIL results in the console output.

Files Implemented for HW3:
The following files were implemented or used specifically for this assignment:

- HW3Mainline.java – Standalone test runner with 5 manually implemented tests
  
- UserNameRecognizer.java – The class under test, validating password rules
  
- kmogala_Assignment_3.pdf – The assignment submission document with:
  - Test descriptions
  - Screencast link
  - Javadoc inspiration
  - Documentation output
    
- docs/ – Folder containing generated Javadoc HTML files

Testing and Validation:

1. List of the Five Automated Tests:
   The following JUnit test methods are implemented in the UserNameRecognizerTest.java file:
   testEmptyInput(): Tests the case when the input is empty.

   testInvalidStartingCharacter(): Tests a username that starts with an invalid character (a digit).

   testSpecialCharacterAtEnd(): Tests a username that ends with a special character (e.g., .).

   testTooLongUsername(): Tests a username that is too long (more than 16 characters).

   testTooShortUsername(): Tests a username that is too short (less than 4 characters).

2. Description of the Five Automated Tests:
   a) testEmptyInput()
      Description: Validates the behavior of the UserNameRecognizer class when provided with an empty input.

      Input: An empty string.

      Expected Outcome: An error message indicating that the input is empty.

      Purpose: Ensures the system correctly handles and identifies empty inputs as invalid, preventing unexpected errors or unintended behavior.
   
   b) testInvalidStartingCharacter()
      Description: Checks if the UserNameRecognizer class rejects usernames that begin with an invalid character, such as a digit.

      Input: A username starting with a digit.

      Expected Outcome: An error message indicating that the username must start with a letter.

      Purpose: Ensures that usernames follow the rule that they must start with a letter, maintaining consistency in username formatting.
   
   c) testTooShortUsername()
      Description: Ensures that the UserNameRecognizer class rejects usernames that are too short.

      Input: A username with fewer than four characters.

      Expected Outcome: An error message indicating that the username is too short.

      Purpose: Validates that the system enforces a minimum length requirement for usernames.
   
   d) testTooLongUsername()
      Description: Verifies that the UserNameRecognizer class rejects usernames exceeding the maximum length of 16 characters.

      Input: A username with more than 16 characters.

      Expected Outcome: An error message indicating that the username is too long.

      Purpose: Ensures that usernames remain within a manageable and readable length.
   
   e) testSpecialCharacterAtEnd()
      Description: Tests if the UserNameRecognizer class properly handles usernames ending with a special character.

      Input: A username where the last character is a special symbol (e.g., .).

      Expected Outcome: An error message indicating that a special character must be followed by an alphanumeric character.

      Purpose: Enforces the rule that special characters cannot appear at the end of a username.

Author: Kripal Naveen Kumar Mogala

Course: CSE360

Date: March 2025



