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
    @DisplayName("SetLikedMovies populates liked movies correctly")
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
}