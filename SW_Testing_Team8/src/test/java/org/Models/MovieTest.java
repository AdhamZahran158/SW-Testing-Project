package org.Models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Movie constructor sets title correctly")
    void testMovieConstructorTitle() {
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Thriller"));
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);

        assertEquals("The Dark Knight", movie.getTitle());
    }

    @Test
    @DisplayName("Movie constructor sets movie ID correctly")
    void testMovieConstructorID() {
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Thriller"));
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);

        assertEquals("TDK123", movie.getMovieID());
    }

    @Test
    @DisplayName("Movie constructor sets genres list correctly")
    void testMovieConstructorGenres() {
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Thriller"));
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);

        assertEquals(2, movie.getGenres().size());
    }


    // ==================== Getter Tests ====================

    @Test
    @DisplayName("Movie getTitle returns non-null value")
    void testMovieGetTitleNotNull() {
        ArrayList<String> genres = new ArrayList<>(List.of("Drama"));
        Movie movie = new Movie("Inception", "INC456", genres);

        assertNotNull(movie.getTitle());
    }

    @Test
    @DisplayName("Movie getMovieID returns non-null value")
    void testMovieGetMovieIDNotNull() {
        ArrayList<String> genres = new ArrayList<>(List.of("Drama"));
        Movie movie = new Movie("Inception", "INC456", genres);

        assertNotNull(movie.getMovieID());
    }

    @Test
    @DisplayName("Movie getGenres returns non-null value")
    void testMovieGetGenresNotNull() {
        ArrayList<String> genres = new ArrayList<>(List.of("Drama"));
        Movie movie = new Movie("Inception", "INC456", genres);

        assertNotNull(movie.getGenres());
    }


    @Test
    @DisplayName("Movie maintains all field values after creation")
    void testMovieFieldIntegrity() {
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Thriller"));
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);

        // Verify all fields maintain their values
        assertEquals("The Dark Knight", movie.getTitle());
        assertEquals("TDK123", movie.getMovieID());
        assertEquals(2, movie.getGenres().size());
        assertTrue(movie.getGenres().contains("Action"));
        assertTrue(movie.getGenres().contains("Thriller"));
    }
}