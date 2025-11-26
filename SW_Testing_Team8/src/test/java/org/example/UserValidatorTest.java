package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    // User Name Tests
    @Test
    @DisplayName("Valid user name - single word")
    void testValidUserNameSingleWord() {
        assertTrue(validator.validateUserName("John"));
        assertTrue(validator.validateUserName("john"));
        assertTrue(validator.validateUserName("joHn"));
    }

    @Test
    @DisplayName("Valid user name - multiple words")
    void testValidUserNameMultipleWords() {
        assertTrue(validator.validateUserName("John Doe"));
        assertTrue(validator.validateUserName("john doe"));
        assertTrue(validator.validateUserName("john Doe"));
    }

    @Test
    @DisplayName("Valid user name - with middle name")
    void testValidUserNameWithMiddle() {
        assertTrue(validator.validateUserName("John Michael Doe"));
        assertTrue(validator.validateUserName("John michael Doe"));
    }

    @Test
    @DisplayName("Invalid user name - starts with space")
    void testInvalidUserNameStartsWithSpace() {
        assertFalse(validator.validateUserName(" John"));
        assertFalse(validator.validateUserName(" john"));
    }

    @Test
    @DisplayName("Invalid user name - contains digits")
    void testInvalidUserNameWithDigits() {
        assertFalse(validator.validateUserName("John123"));
    }

    @Test
    @DisplayName("Invalid user name - null")
    void testInvalidUserNameNull() {
        assertFalse(validator.validateUserName(null));
    }

    @Test
    @DisplayName("Invalid user name - empty")
    void testInvalidUserNameEmpty() {
        assertFalse(validator.validateUserName(""));
    }

    @Test
    @DisplayName("Invalid user name - special characters")
    void testInvalidUserNameSpecialChars() {
        assertFalse(validator.validateUserName("John@Doe"));
    }

    // User ID Tests
    @Test
    @DisplayName("Valid user ID - all digits")
    void testValidUserIdAllDigits() {
        assertTrue(validator.validateUserId("123456789"));
    }

    @Test
    @DisplayName("Valid user ID - ends with letter")
    void testValidUserIdEndsWithLetter() {
        assertTrue(validator.validateUserId("12345678A"));
        assertTrue(validator.validateUserId("12345678a"));
    }

    @Test
    @DisplayName("Valid user ID - ends with 2 letter")
    void testValidUserIdEndsWithLetters() {
        assertFalse(validator.validateUserId("1A2B3C4DD"));
        assertFalse(validator.validateUserId("1A2B3CGDD"));
        assertFalse(validator.validateUserId("1A2B3CGdD"));
    }

    @Test
    @DisplayName("Valid user ID - mixed digits and letters")
    void testValidUserIdMixed() {
        assertTrue(validator.validateUserId("1A2B3C4D5"));
        assertTrue(validator.validateUserId("1a2b3c4d5"));
        assertTrue(validator.validateUserId("1a2b3c4D5"));
    }
    // I dont understand the requirement. does it allow letters mixed in between?


    @Test
    @DisplayName("Valid user ID - starts with digit")
    void testValidUserIdStartsWithDigit() {
        assertTrue(validator.validateUserId("9123A5678"));
    }

    @Test
    @DisplayName("Invalid user ID - wrong length (too short)")
    void testInvalidUserIdTooShort() {
        assertFalse(validator.validateUserId("12345678"));
    }

    @Test
    @DisplayName("Invalid user ID - wrong length (too long)")
    void testInvalidUserIdTooLong() {
        assertFalse(validator.validateUserId("1234567890"));
    }

    @Test
    @DisplayName("Invalid user ID - starts with letter")
    void testInvalidUserIdStartsWithLetter() {
        assertFalse(validator.validateUserId("A12345678"));
        assertFalse(validator.validateUserId("a12345678"));
    }

    @Test
    @DisplayName("Invalid user ID - null")
    void testInvalidUserIdNull() {
        assertFalse(validator.validateUserId(null));
    }

    @Test
    @DisplayName("Invalid user ID - duplicate")
    void testInvalidUserIdDuplicate() {
        validator.validateUserId("123456789");
        assertFalse(validator.validateUserId("123456789"));
    }

    @Test
    @DisplayName("Invalid user ID - special characters")
    void testInvalidUserIdSpecialChars() {
        assertFalse(validator.validateUserId("@12345678@"));
        assertFalse(validator.validateUserId("1234@5678"));
        assertFalse(validator.validateUserId("12345678@"));
    }
}