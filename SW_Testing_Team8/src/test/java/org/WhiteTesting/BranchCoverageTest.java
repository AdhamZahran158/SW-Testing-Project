package org.WhiteTesting;

import org.Models.Movie;
import org.Models.User;

import org.example.UserValidator;
import org.example.RecommendationEngine;
import org.example.ExceptionHandler;
import org.example.FileHandler;
import org.example.FileWriteHandler;
import org.example.MovieRecommendationApp;
import org.example.MovieValidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
public class BranchCoverageTest {
    @Test
    @DisplayName("Branch coverage: likedMoviesId empty returns false")

    void testLikedMoviesIdEmpty() {
        User user = new User("Alia", "U018", new ArrayList<>());
        ArrayList<Movie> availableMovies = new ArrayList<>();
        assertFalse(user.setLikedMovies(availableMovies));
    }

    @Test
    @DisplayName("Branch coverage: likedMoviesId not empty, movie matches")
    void testMovieMatch() {
        ArrayList<String> likedMovies = new ArrayList<>(Arrays.asList("M001"));
        User user = new User("Alia", "U018", likedMovies);

        ArrayList<Movie> availableMovies = new ArrayList<>();
        Movie m1 = new Movie("Inception", "M001", new ArrayList<>());
        availableMovies.add(m1);

        assertTrue(user.setLikedMovies(availableMovies));
        assertEquals(1, user.getLikedMovies().size());
    }

    @Test
    @DisplayName("Branch coverage: likedMoviesId not empty, movie does not match")
    void testMovieNoMatch() {
        ArrayList<String> likedMovies = new ArrayList<>(Arrays.asList("M002"));
        User user = new User("Alia", "U018", likedMovies);

        ArrayList<Movie> availableMovies = new ArrayList<>();
        Movie m1 = new Movie("Inception", "M001", new ArrayList<>());
        availableMovies.add(m1);

        assertFalse(user.setLikedMovies(availableMovies));
        assertEquals(0, user.getLikedMovies().size());
    }

    @Test
    @DisplayName("Branch coverage: setRecMovies")
    void testSetRecMovies() {
        User user = new User("Alia", "U018", new ArrayList<>(Arrays.asList("M001")));
        ArrayList<String> recommended = new ArrayList<>();
        recommended.add("M001");

        user.setRecMovies(recommended);
        assertEquals(1, user.getRecMovies().size());
    }

    // ---------- validateLikedMovieList ----------

    @Test
    @DisplayName("Branch: liked movies result = false")
    void testValidateLikedMoviesFalse() {
        UserValidator validator = new UserValidator();
        validator.validateLikedMovieList(false);
    }

    @Test
    @DisplayName("Branch: liked movies result = true")
    void testValidateLikedMoviesTrue() {
        UserValidator validator = new UserValidator();
        validator.validateLikedMovieList(true);
    }

    // ---------- validateUserName ----------

    @Test
    @DisplayName("Branch: userName is null")
    void testUserNameNull() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserName(null));
    }

    @Test
    @DisplayName("Branch: userName empty")
    void testUserNameEmpty() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserName(""));
    }

    @Test
    @DisplayName("Branch: userName starts with space")
    void testUserNameStartsWithSpace() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserName(" Ali"));
    }

    @Test
    @DisplayName("Branch: userName contains invalid character")
    void testUserNameInvalidCharacter() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserName("Joe1"));
    }

    @Test
    @DisplayName("Branch: valid userName")
    void testValidUserName() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.validateUserName("Joe Hassan"));
    }

    // ---------- validateUserId ----------

    @Test
    @DisplayName("Branch: userId is null")
    void testUserIdNull() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserId(null));
    }

    @Test
    @DisplayName("Branch: userId wrong length")
    void testUserIdWrongLength() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserId("123"));
    }

    @Test
    @DisplayName("Branch: userId first char not digit")
    void testUserIdFirstCharNotDigit() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserId("A23456789"));
    }

    @Test
    @DisplayName("Branch: userId contains invalid character")
    void testUserIdInvalidCharacter() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserId("1@3456789"));
    }

    @Test
    @DisplayName("Branch: last two chars both letters")
    void testUserIdEndsWithTwoLetters() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.validateUserId("1234567AB"));
    }

    @Test
    @DisplayName("Branch: duplicate userId")
    void testDuplicateUserId() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.validateUserId("12345678A"));
        assertFalse(validator.validateUserId("12345678A"));
    }

    @Test
    @DisplayName("Branch: valid userId")
    void testValidUserId() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.validateUserId("12345678B"));
    }
}