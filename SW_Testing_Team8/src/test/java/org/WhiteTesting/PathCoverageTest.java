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

public class PathCoverageTest {
    @Test
    void testGetters() {
        ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("M001"));
        User user = new User("Alice", "U12345678", likedIds);

        assertEquals("Alice", user.getName());
        assertEquals("U12345678", user.getId());
        assertEquals(0, user.getLikedMovies().size());
        assertEquals(0, user.getRecMovies().size());
    }

    @Test
    void testSetLikedMoviesNullIds() {
        User user = new User("Alice", "U12345678", null);
        assertFalse(user.setLikedMovies(new ArrayList<>()));
        assertEquals(0, user.getLikedMovies().size());
    }

    @Test
    void testSetLikedMoviesEmptyIds() {
        User user = new User("Alice", "U12345678", new ArrayList<>());
        assertFalse(user.setLikedMovies(new ArrayList<>()));
        assertEquals(0, user.getLikedMovies().size());
    }

    @Test
    void testSetLikedMoviesNoMatches() {
        ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("M001"));
        User user = new User("Alice", "U12345678", likedIds);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Movie1", "M002", new ArrayList<>()));

        assertFalse(user.setLikedMovies(movies));
        assertEquals(0, user.getLikedMovies().size());
    }

    @Test
    void testSetLikedMoviesPartialMatches() {
        ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("M001", "M002"));
        User user = new User("Alice", "U12345678", likedIds);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Movie1", "M001", new ArrayList<>()));

        assertFalse(user.setLikedMovies(movies)); // only 1 matched, required 2
        assertEquals(1, user.getLikedMovies().size());
    }

    @Test
    void testSetLikedMoviesAllMatches() {
        ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("M001", "M002"));
        User user = new User("Alice", "U12345678", likedIds);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Movie1", "M001", new ArrayList<>()));
        movies.add(new Movie("Movie2", "M002", new ArrayList<>()));

        assertTrue(user.setLikedMovies(movies));
        assertEquals(2, user.getLikedMovies().size());
    }

    @Test
    void testSetAndGetRecMovies() {
        User user = new User("Alice", "U12345678", new ArrayList<>());
        ArrayList<String> rec = new ArrayList<>(Arrays.asList("Movie1", "Movie2"));

        user.setRecMovies(rec);
        assertEquals(2, user.getRecMovies().size());
        assertTrue(user.getRecMovies().contains("Movie1"));
        assertTrue(user.getRecMovies().contains("Movie2"));
    }
}
