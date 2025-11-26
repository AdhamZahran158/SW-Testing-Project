package org.example;
import org.example.*;
import org.Models.Movie;
import org.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
// ==================== MovieValidator Tests ====================
class MovieValidatorTest {

    private MovieValidator validator;
//    private ExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        validator = new MovieValidator();
//        exceptionHandler = new ExceptionHandler();
    }

    // Movie Title Tests
    @Test
    @DisplayName("Valid movie title - all words start with capital")
    void testValidMovieTitleAllCaps() {
        assertTrue(validator.validateMovieTitle("The Dark Knight"));
    }

    @Test
    @DisplayName("Valid movie title - single word")
    void testValidMovieTitleSingleWord() {
        assertTrue(validator.validateMovieTitle("Inception"));
    }

    @Test
    @DisplayName("Valid movie title - with numbers")
    void testValidMovieTitleWithNumbers() {
        assertTrue(validator.validateMovieTitle("The Dark Knight 2"));
    }

    @Test
    @DisplayName("Invalid movie title - lowercase first letter")
    void testInvalidMovieTitleLowercase() {
        assertFalse(validator.validateMovieTitle("the Dark Knight"));
    }

    @Test
    @DisplayName("Invalid movie title - word after space not capitalized")
    void testInvalidMovieTitleWordNotCapitalized() {
        assertFalse(validator.validateMovieTitle("The dark Knight"));
    }

    @Test
    @DisplayName("Invalid movie title - too short")
    void testInvalidMovieTitleTooShort() {
        assertFalse(validator.validateMovieTitle("A"));
    }

    @Test
    @DisplayName("Invalid movie title - special characters")
    void testInvalidMovieTitleSpecialChars() {
        assertFalse(validator.validateMovieTitle("The Dark@Knight"));
    }

    // Movie ID Letter Tests
    @Test
    @DisplayName("Valid movie ID letters - all uppercase")
    void testValidMovieIdLetters() {
        assertTrue(validator.validateMovieIdLetters("TDK123"));
    }

    @Test
    @DisplayName("Invalid movie ID letters - contains lowercase")
    void testInvalidMovieIdLettersLowercase() {
        assertFalse(validator.validateMovieIdLetters("Tdk123"));
    }

    @Test
    @DisplayName("Invalid movie ID letters - contains digit in letter part")
    void testInvalidMovieIdLettersWithDigit() {
        assertFalse(validator.validateMovieIdLetters("TD2123"));
    }

    @Test
    @DisplayName("Invalid movie ID - non-digit in last 3 positions")
    void testInvalidNonDigitInLastThree() {
        setupIdList();
        assertFalse(validator.checkUnique3Digits("TDK12A"));
    }

    // make a setup for the id_list before each test
    void setupIdList() {
        List<String> idList = new ArrayList<>();
        idList.add("TDK123");
        idList.add("INC456");
        validator.setId_list(idList);
    }
    // Movie ID Unique Digits Tests
    @Test
    @DisplayName("Valid movie ID - unique 3 digits")
    void testValidUniqueDigits() {
        setupIdList();
        assertTrue(validator.checkUnique3Digits("MAT789"));
    }

    @Test
    @DisplayName("Invalid movie ID - duplicate digits")
    void testInvalidDuplicateDigitsID() {
        setupIdList();
        assertFalse(validator.checkUnique3Digits("JKL123"));
    }
    // Test failed in the previous version.
    // Test fixed


    // Full Movie ID Tests
    @Test
    @DisplayName("Valid full movie ID")
    void testValidFullMovieId() {
        assertTrue(validator.validateMovieIdFull("TL123"));
    }

    @Test
    @DisplayName("Invalid full movie ID - too short")
    void testInvalidFullMovieIdTooShort() {
        assertFalse(validator.validateMovieIdFull("TDK"));
    }

    @Test
    @DisplayName("Invalid full movie ID - lowercase letters")
    void testInvalidFullMovieIdLowercase() {
        assertFalse(validator.validateMovieIdFull("tdk123"));
    }

    @Test
    @DisplayName("Invalid full movie ID - special characters")
    void testInvalidFullMovieIdSpecialChars() {
        assertFalse(validator.validateMovieIdFull("TDK@123"));
        assertFalse(validator.validateMovieIdFull("TDK@123"));
        assertFalse(validator.validateMovieIdFull("@TDK123"));
    }

    // Movie Genre Tests
    @Test
    @DisplayName("Valid movie genre")
    void testValidMovieGenre() {
        assertTrue(validator.validateMovieGenre("Action"));
    }

    @Test
    @DisplayName("Valid movie genre - lowercase")
    void testValidMovieGenreLowercase() {
        assertTrue(validator.validateMovieGenre("horror"));
    }

    @Test
    @DisplayName("Invalid movie genre - contains digits")
    void testInvalidMovieGenreWithDigits() {
        assertFalse(validator.validateMovieGenre("Action123"));
    }

    @Test
    @DisplayName("Invalid movie genre - too short")
    void testInvalidMovieGenreTooShort() {
        assertFalse(validator.validateMovieGenre("A"));
    }

    @Test
    @DisplayName("Invalid movie genre - contains spaces")
    void testInvalidMovieGenreWithSpaces() {
        assertFalse(validator.validateMovieGenre("Action Movie"));
    }
}