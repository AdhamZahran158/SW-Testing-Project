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
public class StatementCoverageTest {

    @Test
    @DisplayName("Statement coverage for Movie class")
    void testMovieStatements() {
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("Music", "Drama"));
        Movie movie = new Movie("Whiplash", "M018", genres);

        assertEquals("Whiplash", movie.getTitle());
        assertEquals("M018", movie.getMovieID());
        assertEquals(genres, movie.getGenres());
    }

    @Test
    @DisplayName("Statement coverage for User class")
    void testUserStatements() {
        ArrayList<String> likedMovies = new ArrayList<>(Arrays.asList("M018"));
        ArrayList<String> recommendedMovies = new ArrayList<>();
        ArrayList<Movie> availableMovies = new ArrayList<>();

        User user = new User("Alia", "U018", likedMovies);
        Movie m1 = new Movie("Whiplash", "M018", new ArrayList<>());


        assertEquals("Alia", user.getName());
        assertEquals("U018", user.getId());

        availableMovies.add(m1);

        assertTrue(user.setLikedMovies(availableMovies));
        assertEquals(1, user.getLikedMovies().size());

        recommendedMovies.add("M018");
        user.setRecMovies(recommendedMovies);
        assertEquals(1, user.getRecMovies().size());

    }


}