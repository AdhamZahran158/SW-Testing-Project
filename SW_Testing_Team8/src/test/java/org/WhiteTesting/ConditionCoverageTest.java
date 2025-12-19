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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionCoverageTest {

    @Test
    @DisplayName("MovieValidator Title Condition: Uppercase first letters")
    void testTitleUppercase() {
        MovieValidator movieValidator = new MovieValidator();
        assertTrue(movieValidator.validateMovieTitle("Toy Story 3"));
        assertFalse(movieValidator.validateMovieTitle("toy Story"));
        assertFalse(movieValidator.validateMovieTitle("Toy story"));
    }

    @Test
    @DisplayName("MovieValidator Title Condition: Minimum length")
    void testTitleLength() {
        MovieValidator movieValidator = new MovieValidator();
        assertFalse(movieValidator.validateMovieTitle("ts"));
    }

    @Test
    @DisplayName("MovieValidator Title Condition: Digit or space in title")
    void testTitleDigitOrSpace() {
        MovieValidator movieValidator = new MovieValidator();
        assertTrue(movieValidator.validateMovieTitle("Toy Story 3"));
    }

    @Test
    @DisplayName("MovieValidator MovieID Letters Condition")
    void testMovieIDLettersOnly() {
        MovieValidator movieValidator = new MovieValidator();
        assertTrue(movieValidator.validateMovieIdLetters("TDK123"));
        assertFalse(movieValidator.validateMovieIdLetters("TS3123"));
        assertFalse(movieValidator.validateMovieIdLetters("TDk123"));
        assertTrue(movieValidator.validateMovieIdLetters("TD"));
    }

    @Test
    @DisplayName("MovieValidator MovieID Unique Digits Condition")
    void testMovieIDUniqueDigits() {
        MovieValidator movieValidator = new MovieValidator();
        ArrayList<String> existingIDs = new ArrayList<>();
        existingIDs.add("TDK123");
        existingIDs.add("I456");
        movieValidator.setId_list(existingIDs);

        assertTrue(movieValidator.checkUnique3Digits("TS012"));
        assertFalse(movieValidator.checkUnique3Digits("TS123"));
        assertFalse(movieValidator.checkUnique3Digits("TG12"));
    }

    @Test
    @DisplayName("MovieValidator MovieID Full Condition")
    void testMovieIDFull() {
        MovieValidator movieValidator = new MovieValidator();
        ArrayList<String> existingIDs = new ArrayList<>();
        existingIDs.add("TDK123");
        existingIDs.add("I456");
        movieValidator.setId_list(existingIDs);

        assertTrue(movieValidator.validateMovieIdFull("TS012"));
        assertFalse(movieValidator.validateMovieIdFull("TS123"));
        assertFalse(movieValidator.validateMovieIdFull("Tdk789"));
        assertFalse(movieValidator.validateMovieIdFull("Ts789"));
        assertFalse(movieValidator.validateMovieIdFull("T12"));
    }

    @Test
    @DisplayName("MovieValidator Genre Length Condition")
    void testMovieGenreLength() {
        MovieValidator movieValidator = new MovieValidator();
        assertTrue(movieValidator.validateMovieGenre("Action"));
        assertFalse(movieValidator.validateMovieGenre("A"));
    }

    @Test
    @DisplayName("MovieValidator Genre Alphabetic Condition")
    void testMovieGenreAlphabetic() {
        MovieValidator movieValidator = new MovieValidator();
        assertFalse(movieValidator.validateMovieGenre("Act4ion"));
    }

    @Test
    @DisplayName("Condition coverage: likedMoviesId.isEmpty() == true")
    void testEmptyLikedMoviesId() {
        // isEmpty() → true
        ArrayList<String> likedMovies = new ArrayList<>();

        User user = new User("Alia", "U018", likedMovies);
        ArrayList<Movie> availableMovies = new ArrayList<>();

        assertFalse(user.setLikedMovies(availableMovies));
    }

    @Test
    @DisplayName("Condition coverage: likedMoviesId.isEmpty() == false")
    void testNonEmptyLikedMoviesId() {
        // isEmpty() → false
        ArrayList<String> likedMovies = new ArrayList<>(Arrays.asList("M018"));
        User user = new User("Alia", "U018", likedMovies);

        ArrayList<Movie> availableMovies = new ArrayList<>();
        Movie movie = new Movie("Whiplash", "M018", new ArrayList<>());
        availableMovies.add(movie);

        assertTrue(user.setLikedMovies(availableMovies));
        assertEquals(1, user.getLikedMovies().size());
    }

    @Test
    @DisplayName("Condition coverage: Objects.equals() true and false")
    void testMovieIdMatchAndNoMatch() {
        ArrayList<String> likedMovies = new ArrayList<>(Arrays.asList("M018"));
        User user = new User("Alia", "U018", likedMovies);

        ArrayList<Movie> availableMovies = new ArrayList<>();

        // Objects.equals → false
        Movie m1 = new Movie("Movie1", "M001", new ArrayList<>());

        // Objects.equals → true
        Movie m2 = new Movie("Whiplash", "M018", new ArrayList<>());

        availableMovies.add(m1);
        availableMovies.add(m2);

        assertTrue(user.setLikedMovies(availableMovies));
        assertEquals(1, user.getLikedMovies().size());
    }
}

