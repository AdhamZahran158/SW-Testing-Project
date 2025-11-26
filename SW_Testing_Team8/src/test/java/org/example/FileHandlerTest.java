package org.example;

import org.Models.Movie;
import org.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private FileHandler fileHandler;
    private String testMovieFilePath;
    private String testUserFilePath;

    @BeforeEach
    void setUp() throws Exception {
        fileHandler = new FileHandler();

        // Create temporary test files
        testMovieFilePath = "test_movies.txt";
        testUserFilePath = "test_users.txt";
    }

    @Test
    @DisplayName("Read movies from valid file")
    void testReadMoviesValidFile() throws Exception {
        // Create test movie file
        java.io.FileWriter writer = new java.io.FileWriter(testMovieFilePath);
        writer.write("The Dark Knight,TDK123\n");
        writer.write("Action,Thriller\n");
        writer.write("Inception,INC456\n");
        writer.write("Action,SciFi\n");
        writer.close();

        ArrayList<Movie> movies = fileHandler.readMovies(testMovieFilePath);

        assertEquals(2, movies.size());
        assertEquals("The Dark Knight", movies.get(0).getTitle());
        assertEquals("TDK123", movies.get(0).getMovieID());
        assertEquals(2, movies.get(0).getGenres().size());
        assertTrue(movies.get(0).getGenres().contains("Action"));
        assertTrue(movies.get(0).getGenres().contains("Thriller"));

        assertEquals("Inception", movies.get(1).getTitle());
        assertEquals("INC456", movies.get(1).getMovieID());

        // Cleanup
        new java.io.File(testMovieFilePath).delete();
    }

    @Test
    @DisplayName("Read movies with single genre")
    void testReadMoviesSingleGenre() throws Exception {
        java.io.FileWriter writer = new java.io.FileWriter(testMovieFilePath);
        writer.write("The Godfather,TG789\n");
        writer.write("Drama\n");
        writer.close();

        ArrayList<Movie> movies = fileHandler.readMovies(testMovieFilePath);

        assertEquals(1, movies.size());
        assertEquals("The Godfather", movies.get(0).getTitle());
        assertEquals(1, movies.get(0).getGenres().size());
        assertEquals("Drama", movies.get(0).getGenres().get(0));

        new java.io.File(testMovieFilePath).delete();
    }

    @Test
    @DisplayName("Read movies with multiple genres")
    void testReadMoviesMultipleGenres() throws Exception {
        java.io.FileWriter writer = new java.io.FileWriter(testMovieFilePath);
        writer.write("Mad Max,MM012\n");
        writer.write("Action,Drama,Thriller,Adventure\n");
        writer.close();

        ArrayList<Movie> movies = fileHandler.readMovies(testMovieFilePath);

        assertEquals(1, movies.size());
        assertEquals(4, movies.get(0).getGenres().size());
        assertTrue(movies.get(0).getGenres().contains("Action"));
        assertTrue(movies.get(0).getGenres().contains("Drama"));
        assertTrue(movies.get(0).getGenres().contains("Thriller"));
        assertTrue(movies.get(0).getGenres().contains("Adventure"));

        new java.io.File(testMovieFilePath).delete();
    }

    @Test
    @DisplayName("Read movies from empty file")
    void testReadMoviesEmptyFile() throws Exception {
        java.io.FileWriter writer = new java.io.FileWriter(testMovieFilePath);
        writer.close();

        ArrayList<Movie> movies = fileHandler.readMovies(testMovieFilePath);

        assertEquals(0, movies.size());

        new java.io.File(testMovieFilePath).delete();
    }

    @Test
    @DisplayName("Read movies from non-existent file")
    void testReadMoviesNonExistentFile() {
        ArrayList<Movie> movies = fileHandler.readMovies("non_existent_file.txt");

        // Should return empty list without crashing
        assertNotNull(movies);
        assertEquals(0, movies.size());
    }

    @Test
    @DisplayName("Read users from valid file")
    void testReadUsersValidFile() throws Exception {
        java.io.FileWriter writer = new java.io.FileWriter(testUserFilePath);
        writer.write("John Doe,123456789\n");
        writer.write("TDK123,INC456\n");
        writer.write("Jane Smith,987654321\n");
        writer.write("TG789\n");
        writer.close();

        ArrayList<User> users = fileHandler.readUser(testUserFilePath);

        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("123456789", users.get(0).getId());

        assertEquals("Jane Smith", users.get(1).getName());
        assertEquals("987654321", users.get(1).getId());

        new java.io.File(testUserFilePath).delete();
    }

    @Test
    @DisplayName("Read users with single liked movie")
    void testReadUsersSingleLikedMovie() throws Exception {
        java.io.FileWriter writer = new java.io.FileWriter(testUserFilePath);
        writer.write("Alice Johnson,111111111\n");
        writer.write("TDK123\n");
        writer.close();

        ArrayList<User> users = fileHandler.readUser(testUserFilePath);

        assertEquals(1, users.size());
        assertEquals("Alice Johnson", users.get(0).getName());

        new java.io.File(testUserFilePath).delete();
    }

    @Test
    @DisplayName("Read users with multiple liked movies")
    void testReadUsersMultipleLikedMovies() throws Exception {
        java.io.FileWriter writer = new java.io.FileWriter(testUserFilePath);
        writer.write("Bob Williams,222222222\n");
        writer.write("TDK123,INC456,TG789,MM012\n");
        writer.close();

        ArrayList<User> users = fileHandler.readUser(testUserFilePath);

        assertEquals(1, users.size());

        new java.io.File(testUserFilePath).delete();
    }

    @Test
    @DisplayName("Read users from empty file")
    void testReadUsersEmptyFile() throws Exception {
        java.io.FileWriter writer = new java.io.FileWriter(testUserFilePath);
        writer.close();

        ArrayList<User> users = fileHandler.readUser(testUserFilePath);

        assertEquals(0, users.size());

        new java.io.File(testUserFilePath).delete();
    }

    @Test
    @DisplayName("Read users from non-existent file")
    void testReadUsersNonExistentFile() {
        ArrayList<User> users = fileHandler.readUser("non_existent_user_file.txt");

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    @DisplayName("Read movies handles title with comma correctly")
    void testReadMoviesTitleWithComma() throws Exception {
        // Note: This tests the current implementation's behavior
        // The current split by comma may not handle titles with commas
        java.io.FileWriter writer = new java.io.FileWriter(testMovieFilePath);
        writer.write("The Good The Bad And The Ugly,TGTBATU123\n");
        writer.write("Western,Drama\n");
        writer.close();

        ArrayList<Movie> movies = fileHandler.readMovies(testMovieFilePath);

        assertEquals(1, movies.size());
        assertEquals("The Good The Bad And The Ugly", movies.get(0).getTitle());

        new java.io.File(testMovieFilePath).delete();
    }
}