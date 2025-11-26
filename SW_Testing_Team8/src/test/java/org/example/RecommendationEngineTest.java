package org.example;

import org.Models.Movie;
import org.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationEngineTest {
    private RecommendationEngine engine;
    private ArrayList<Movie> movies;
    private User user;

    @BeforeEach
    void setUp() {
        engine = new RecommendationEngine();
        movies = new ArrayList<>();

        // Create test movies
        ArrayList<String> actionGenres = new ArrayList<>(Arrays.asList("Action", "Thriller"));
        ArrayList<String> dramaGenres = new ArrayList<>(List.of("Drama"));
        ArrayList<String> actionDramaGenres = new ArrayList<>(Arrays.asList("Action", "Drama"));
        ArrayList<String> horrorGenres = new ArrayList<>(List.of("Horror"));

        movies.add(new Movie("The Dark Knight", "TDK123", actionGenres));
        movies.add(new Movie("Inception", "INC456", actionGenres));
        movies.add(new Movie("The Godfather", "TG789", dramaGenres));
        movies.add(new Movie("Mad Max", "MM012", actionDramaGenres));
        movies.add(new Movie("The Conjuring", "TC345", horrorGenres));
    }

    @Test
    @DisplayName("Recommend movies based on single genre")
    void testRecommendationSingleGenre() {
        ArrayList<String> likedMoviesId = new ArrayList<>(List.of("TDK123"));
        user = new User("John Doe", "123456789", likedMoviesId);
        user.setLikedMovies(movies);

        engine.GetRecommendations(user, movies);
        ArrayList<String> recommendations = user.getRecMovies();

        assertTrue(recommendations.contains("Inception"));
        assertTrue(recommendations.contains("Mad Max"));
        assertFalse(recommendations.contains("The Dark Knight"));
        assertFalse(recommendations.contains("The Conjuring"));
    }

    @Test
    @DisplayName("Recommend movies based on multiple liked movies")
    void testRecommendationMultipleLikedMovies() {
        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("TDK123", "TG789"));
        user = new User("John Doe", "123456789", likedMoviesId);
        user.setLikedMovies(movies);

        engine.GetRecommendations(user, movies);
        ArrayList<String> recommendations = user.getRecMovies();

        assertTrue(recommendations.contains("Inception"));
        assertTrue(recommendations.contains("Mad Max"));
        assertFalse(recommendations.contains("The Dark Knight"));
        assertFalse(recommendations.contains("The Godfather"));
    }

    @Test
    @DisplayName("No recommendations when no matching genres")
    void testNoRecommendationsNoMatch() {
        ArrayList<String> likedMoviesId = new ArrayList<>(List.of("TC345"));
        user = new User("John Doe", "123456789", likedMoviesId);
        user.setLikedMovies(movies);

        engine.GetRecommendations(user, movies);
        ArrayList<String> recommendations = user.getRecMovies();

        assertTrue(recommendations.isEmpty());
    }

    @Test
    @DisplayName("No duplicate recommendations")
    void testNoDuplicateRecommendations() {
        ArrayList<String> likedMoviesId = new ArrayList<>(List.of("TDK123"));
        user = new User("John Doe", "123456789", likedMoviesId);
        user.setLikedMovies(movies);

        engine.GetRecommendations(user, movies);
        ArrayList<String> recommendations = user.getRecMovies();

        long uniqueCount = recommendations.stream().distinct().count();
        assertEquals(recommendations.size(), uniqueCount);
    }

    @Test
    @DisplayName("Recommend movies from overlapping genres")
    void testRecommendationOverlappingGenres() {
        ArrayList<String> likedMoviesId = new ArrayList<>(List.of("MM012"));
        user = new User("John Doe", "123456789", likedMoviesId);
        user.setLikedMovies(movies);

        engine.GetRecommendations(user, movies);
        ArrayList<String> recommendations = user.getRecMovies();

        assertTrue(recommendations.contains("The Dark Knight"));
        assertTrue(recommendations.contains("Inception"));
        assertTrue(recommendations.contains("The Godfather"));
    }
}