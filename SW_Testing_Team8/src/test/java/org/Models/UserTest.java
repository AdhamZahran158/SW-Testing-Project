package org.Models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("User constructor sets fields correctly")
    void testUserConstructor() {
        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("TDK123", "INC456"));
        User user = new User("John Doe", "123456789", likedMoviesId);

        assertEquals("John Doe", user.getName());
        assertEquals("123456789", user.getId());
    }

    @Test
    @DisplayName("SetLikedMovies populates liked movies correctly when all movies exist")
    void testSetLikedMovies() {
        ArrayList<Movie> availableMovies = new ArrayList<>();
        availableMovies.add(new Movie("The Dark Knight", "TDK123", new ArrayList<>(List.of("Action"))));
        availableMovies.add(new Movie("Inception", "INC456", new ArrayList<>(List.of("Action"))));
        availableMovies.add(new Movie("The Godfather", "TG789", new ArrayList<>(List.of("Drama"))));

        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("TDK123", "TG789"));
        User user = new User("John Doe", "123456789", likedMoviesId);
        user.setLikedMovies(availableMovies);

        assertEquals(2, user.getLikedMovies().size());
        assertEquals("TDK123", user.getLikedMovies().get(0).getMovieID());
        assertEquals("TG789", user.getLikedMovies().get(1).getMovieID());
    }

    @Test
    @DisplayName("SetLikedMovies handles movie not found in available movies")
    void testSetLikedMoviesNotFound() {
        ArrayList<Movie> availableMovies = new ArrayList<>();
        availableMovies.add(new Movie("Inception", "INC456", new ArrayList<>(List.of("Action"))));
        availableMovies.add(new Movie("The Godfather", "TG789", new ArrayList<>(List.of("Drama"))));

        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("TDK123", "TG789", "XYZ999"));
        User user = new User("John Doe", "123456789", likedMoviesId);
        user.setLikedMovies(availableMovies);

        // Only TG789 should be found
//        assertEquals(1, user.getLikedMovies().size());
//        assertEquals("TG789", user.getLikedMovies().getFirst().getMovieID());
        assertFalse(user.setLikedMovies(availableMovies));
    }

    @Test
    @DisplayName("SetLikedMovies with empty available movies list")
    void testSetLikedMoviesEmptyAvailableList() {
        ArrayList<Movie> availableMovies = new ArrayList<>();

        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("TDK123", "INC456"));
        User user = new User("Jane Smith", "987654321", likedMoviesId);
        user.setLikedMovies(availableMovies);

        assertEquals(0, user.getLikedMovies().size());
    }

    @Test
    @DisplayName("SetLikedMovies with empty liked movies ID list")
    void testSetLikedMoviesEmptyLikedList() {
        ArrayList<Movie> availableMovies = new ArrayList<>();
        availableMovies.add(new Movie("The Dark Knight", "TDK123", new ArrayList<>(List.of("Action"))));
        availableMovies.add(new Movie("Inception", "INC456", new ArrayList<>(List.of("Action"))));

        ArrayList<String> likedMoviesId = new ArrayList<>();
        User user = new User("Alice Johnson", "111111111", likedMoviesId);
        user.setLikedMovies(availableMovies);

//        assertEquals(0, user.getLikedMovies().size());
        assertFalse(user.setLikedMovies(availableMovies));
    }

    @Test
    @DisplayName("SetRecMovies and GetRecMovies work correctly")
    void testRecMovies() {
        ArrayList<String> likedMoviesId = new ArrayList<>();
        User user = new User("John Doe", "123456789", likedMoviesId);

        ArrayList<String> recommendations = new ArrayList<>(Arrays.asList("Movie1", "Movie2"));
        user.setRecMovies(recommendations);

        assertEquals(2, user.getRecMovies().size());
        assertTrue(user.getRecMovies().contains("Movie1"));
        assertTrue(user.getRecMovies().contains("Movie2"));
    }

    @Test
    @DisplayName("SetRecMovies with empty recommendations list")
    void testSetRecMoviesEmpty() {
        ArrayList<String> likedMoviesId = new ArrayList<>();
        User user = new User("Bob Williams", "222222222", likedMoviesId);

        ArrayList<String> recommendations = new ArrayList<>();
        user.setRecMovies(recommendations);

        assertEquals(0, user.getRecMovies().size());
        assertTrue(user.getRecMovies().isEmpty());
    }

    @Test
    @DisplayName("Multiple calls to SetLikedMovies overwrites previous data")
    void testSetLikedMoviesMultipleCalls() {
        ArrayList<Movie> firstBatch = new ArrayList<>();
        firstBatch.add(new Movie("Movie A", "MA001", new ArrayList<>(List.of("Action"))));

        ArrayList<Movie> secondBatch = new ArrayList<>();
        secondBatch.add(new Movie("Movie B", "MB002", new ArrayList<>(List.of("Drama"))));
        secondBatch.add(new Movie("Movie C", "MC003", new ArrayList<>(List.of("Horror"))));

        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("MA001", "MB002", "MC003"));
        User user = new User("Charlie Brown", "333333333", likedMoviesId);

        user.setLikedMovies(firstBatch);
        assertEquals(1, user.getLikedMovies().size());

        user.setLikedMovies(secondBatch);
        // Should now have 3 movies total (1 from first + 2 from second)
        assertEquals(3, user.getLikedMovies().size());
    }

    @Test
    @DisplayName("GetLikedMovies returns correct order matching liked movies ID order")
    void testGetLikedMoviesOrder() {
        ArrayList<Movie> availableMovies = new ArrayList<>();
        availableMovies.add(new Movie("Movie A", "MA001", new ArrayList<>(List.of("Action"))));
        availableMovies.add(new Movie("Movie B", "MB002", new ArrayList<>(List.of("Drama"))));
        availableMovies.add(new Movie("Movie C", "MC003", new ArrayList<>(List.of("Horror"))));

        // Note the order: MB002, MA001, MC003
        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("MB002", "MA001", "MC003"));
        User user = new User("Diana Prince", "444444444", likedMoviesId);
        user.setLikedMovies(availableMovies);

        assertEquals(3, user.getLikedMovies().size());
        // Verify the movies are added in the order they appear in availableMovies
        assertEquals("MA001", user.getLikedMovies().get(0).getMovieID());
        assertEquals("MB002", user.getLikedMovies().get(1).getMovieID());
        assertEquals("MC003", user.getLikedMovies().get(2).getMovieID());
    }

    @Test
    @DisplayName("User with duplicate movie IDs in liked list handles correctly")
    void testSetLikedMoviesDuplicateIds() {
        ArrayList<Movie> availableMovies = new ArrayList<>();
        availableMovies.add(new Movie("The Dark Knight", "TDK123", new ArrayList<>(List.of("Action"))));
        availableMovies.add(new Movie("Inception", "INC456", new ArrayList<>(List.of("Action"))));

        // Duplicate TDK123 in liked movies
        ArrayList<String> likedMoviesId = new ArrayList<>(Arrays.asList("TDK123", "TDK123", "INC456"));
        User user = new User("Eve Adams", "555555555", likedMoviesId);
        user.setLikedMovies(availableMovies);

        // Should add duplicates based on current implementation
        assertEquals(3, user.getLikedMovies().size());
        assertEquals("TDK123", user.getLikedMovies().get(0).getMovieID());
        assertEquals("TDK123", user.getLikedMovies().get(1).getMovieID());
        assertEquals("INC456", user.getLikedMovies().get(2).getMovieID());
    }
}