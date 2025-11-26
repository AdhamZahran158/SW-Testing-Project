package org.Models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    @Test
    @DisplayName("Movie constructor sets fields correctly")
    void testMovieConstructor() {
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Thriller"));
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);

        assertEquals("The Dark Knight", movie.getTitle());
        assertEquals("TDK123", movie.getMovieID());
        assertEquals(2, movie.getGenres().size());
        assertTrue(movie.getGenres().contains("Action"));
        assertTrue(movie.getGenres().contains("Thriller"));
    }

    @Test
    @DisplayName("Movie getters return correct values")
    void testMovieGetters() {
        ArrayList<String> genres = new ArrayList<>(List.of("Drama"));
        Movie movie = new Movie("Inception", "INC456", genres);

        assertNotNull(movie.getTitle());
        assertNotNull(movie.getMovieID());
        assertNotNull(movie.getGenres());
    }
}