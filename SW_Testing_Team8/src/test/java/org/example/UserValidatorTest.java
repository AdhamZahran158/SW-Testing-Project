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
    }

    @Test
    @DisplayName("Valid user name - multiple words")
    void testValidUserNameMultipleWords() {
        assertTrue(validator.validateUserName("John Doe"));
    }

    @Test
    @DisplayName("Valid user name - with middle name")
    void testValidUserNameWithMiddle() {
        assertTrue(validator.validateUserName("John Michael Doe"));
    }

    @Test
    @DisplayName("Invalid user name - starts with space")
    void testInvalidUserNameStartsWithSpace() {
        assertFalse(validator.validateUserName(" John"));
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
    }

    @Test
    @DisplayName("Valid user ID - starts with digit")
    void testValidUserIdStartsWithDigit() {
        assertTrue(validator.validateUserId("912345678"));
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
        assertFalse(validator.validateUserId("12345678@"));
    }
}