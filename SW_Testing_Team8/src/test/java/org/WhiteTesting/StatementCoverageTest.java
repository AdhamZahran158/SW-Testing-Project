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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    @Test
    @DisplayName("Statment coverage for FileHandler Class: All statments beside the Catch Block")
    void testFileHandlerStatements() throws IOException {
        FileHandler fileHandler = new FileHandler();
        File tempFile = File.createTempFile("tempFile",".txt");
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write("The Dark Knight,TDK123\n");
        fileWriter.write("action,thriller,drama\n");
        fileWriter.write("Inception,I456\n");
        fileWriter.write("action,scifi,thriller");
        fileWriter.close();
        ArrayList<Movie> movies = fileHandler.readMovies(tempFile.getAbsolutePath());

        assertEquals("The Dark Knight", movies.get(0).getTitle());
        assertEquals("TDK123", movies.get(0).getMovieID());
        assertEquals("action", movies.get(0).getGenres().get(0));
        assertEquals("thriller", movies.get(0).getGenres().get(1));
        assertEquals("drama", movies.get(0).getGenres().get(2));
        assertEquals("Inception", movies.get(1).getTitle());
        assertEquals("I456", movies.get(1).getMovieID());
        assertEquals("action", movies.get(1).getGenres().get(0));
        assertEquals("scifi", movies.get(1).getGenres().get(1));
        assertEquals("thriller", movies.get(1).getGenres().get(2));

        File tempFile2 = File.createTempFile("tempFile2",".txt");
        FileWriter fileWriter2 = new FileWriter(tempFile2);
        fileWriter2.write("Ahmed Hassan,123456789\n");
        fileWriter2.write("TDK123,I456");
        fileWriter2.close();
        ArrayList<User> users = fileHandler.readUser(tempFile2.getAbsolutePath());
        users.get(0).setLikedMovies(movies);

        assertEquals("Ahmed Hassan", users.get(0).getName());
        assertEquals("123456789",  users.get(0).getId());
        assertEquals("The Dark Knight", users.get(0).getLikedMovies().get(0).getTitle());
        assertEquals("Inception", users.get(0).getLikedMovies().get(1).getTitle());
    }

    @Test
    @DisplayName("Statment coverage for FileHandler Class: Catch Block")
    void testFileHandlerStatementCatchBlock() throws IOException {
        FileHandler fileHandler = new FileHandler();
        String invalidPath = "invalidPath.txt";
        ArrayList<Movie> movies2 = fileHandler.readMovies(invalidPath);
        assertTrue(movies2.isEmpty());
        ArrayList<User> users2 = fileHandler.readUser(invalidPath);
        assertTrue(users2.isEmpty());
    }
}

