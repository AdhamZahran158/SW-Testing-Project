package org.example.integration;

import org.Models.Movie;
import org.Models.User;
import org.example.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BOTTOM-UP INTEGRATION TESTING
 *
 * Bottom-Up approach starts testing from the lowest level modules (leaf modules)
 * and progressively integrates and tests higher-level modules.
 *
 * Testing Order (from bottom to top):
 * Level 1: Models (Movie, User) + ExceptionHandler - Lowest level, no dependencies
 * Level 2: MovieValidator, UserValidator - Depend on ExceptionHandler
 * Level 3: FileHandler - Depends on Movie, User models
 * Level 4: RecommendationEngine - Depends on Movie, User models
 * Level 5: FileWriteHandler - Depends on User model
 * Level 6: Full System Integration - All components together
 */

/*
Level 1,2: Andro
Level 3,4: George
Level 5,6: Kirllos
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BottomUpIntegrationTest {

    private final String referencePath = "src/test/java/org/example/integration/BottomUp_Text_Test";
    private final Path testDir = Path.of(System.getProperty("user.dir")).resolve(referencePath);


    // ==================== LEVEL 1: Basic Models + ExceptionHandler ====================

    @Nested
    @DisplayName("Level 1: Movie Model Tests")
    @Order(1)
    class Level1_MovieModelTests {


    }

    @Nested
    @DisplayName("Level 1: User Model Tests")
    @Order(2)
    class Level1_UserModelTests {


    }

    @Nested
    @DisplayName("Level 1: ExceptionHandler Tests")
    @Order(3)
    class Level1_ExceptionHandlerTests {

    }

    // ==================== LEVEL 2: Validators with ExceptionHandler ====================

    @Nested
    @DisplayName("Level 2: MovieValidator + ExceptionHandler Integration")
    @Order(4)
    class Level2_MovieValidatorIntegration {


    }

    @Nested
    @DisplayName("Level 2: UserValidator + ExceptionHandler Integration")
    @Order(5)
    class Level2_UserValidatorIntegration {


    }

    // ==================== LEVEL 3: FileHandler with Models ====================

    @Nested
    @DisplayName("Level 3: FileHandler + Movie/User Models Integration")
    @Order(6)
    class Level3_FileHandlerIntegration {

    }

    // ==================== LEVEL 4: RecommendationEngine with Models ====================

    @Nested
    @DisplayName("Level 4: RecommendationEngine + Movie/User Integration")
    @Order(7)
    class Level4_RecommendationEngineIntegration {


    }

    // ==================== LEVEL 5: FileHandler + Validators Integration ====================

    @Nested
    @DisplayName("Level 5: FileHandler + Validators Integration")
    @Order(8)
    class Level5_FileHandlerValidatorsIntegration {
        private FileHandler fileHandler;
        private MovieValidator movieValidator;
        private UserValidator userValidator;

        @BeforeEach
        void setUp() {
            fileHandler = new FileHandler();
            movieValidator = new MovieValidator();
            userValidator = new UserValidator();
        }

        @Test
        @DisplayName("Read and validate movies from file")
        void testReadAndValidateMovies() throws IOException {
            Path moviesFile = testDir.resolve("valid_movies.txt");
            Files.writeString(moviesFile, "The Matrix,MATR001\nAction,Sci-Fi");

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());

            for (Movie movie : movies) {
                assertTrue(movieValidator.validateMovieTitle(movie.getTitle()));
                assertTrue(movieValidator.validateMovieIdLetters(movie.getMovieID()));
            }
        }

        @Test
        @DisplayName("Read and validate users from file")
        void testReadAndValidateUsers() throws IOException {
            Path usersFile = testDir.resolve("valid_users.txt");
            Files.writeString(usersFile, "John Doe,1a2b3c4d5\nM001");

            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            for (User user : users) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
            }
        }

        @Test
        @DisplayName("Complete read-validate-link workflow")
        void testCompleteReadValidateLinkWorkflow() throws IOException {
            // Create test files
            Path moviesFile = testDir.resolve("movies.txt");
            Path usersFile = testDir.resolve("users.txt");

            Files.writeString(moviesFile, "The Matrix,MATR001\nAction,Sci-Fi\nInception,INCE002\nThriller,Drama");
            Files.writeString(usersFile, "John Doe,1a2b3c4d5\nMATR001,INCE002");

            // Read data
            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            // Validate movies
            boolean moviesValid = true;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    moviesValid = false;
                    break;
                }
            }

            // Validate users
            boolean usersValid = true;
            for (User user : users) {
                if (!userValidator.validateUserName(user.getName()) ||
                        !userValidator.validateUserId(user.getId())) {
                    usersValid = false;
                    break;
                }
            }

            assertTrue(moviesValid);
            assertTrue(usersValid);

            // Link movies to users
            for (User user : users) {
                assertTrue(user.setLikedMovies(movies));
            }
        }

    }

    // ==================== LEVEL 6: Full System Integration ====================

    @Nested
    @DisplayName("Level 6: Full System Integration")
    @Order(9)
    class Level6_FullSystemIntegration {
        private FileHandler fileHandler;
        private MovieValidator movieValidator;
        private UserValidator userValidator;
        private RecommendationEngine recommendationEngine;

        @BeforeEach
        void setUp() {
            fileHandler = new FileHandler();
            movieValidator = new MovieValidator();
            userValidator = new UserValidator();
            recommendationEngine = new RecommendationEngine();
        }

        @Test
        @DisplayName("Complete workflow: Read -> Validate -> Link -> Recommend")
        void testCompleteWorkflow() throws IOException {
            // Setup test files
            Path moviesFile = testDir.resolve("movies.txt");
            Path usersFile = testDir.resolve("users.txt");

            String moviesContent =
                    "The Matrix,MATR001\n" +
                            "Action,Sci-Fi\n" +
                            "Inception,INCE002\n" +
                            "Thriller,Sci-Fi\n" +
                            "The Hangover,HANG003\n" +
                            "Comedy";

            String usersContent =
                    "John Doe,1a2b3c4d5\n" +
                            "MATR001\n" +
                            "Jane Smith,9z8y7x6w5\n" +
                            "HANG003";

            Files.writeString(moviesFile, moviesContent);
            Files.writeString(usersFile, usersContent);

            // Step 1: Read movies
            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            assertEquals(3, movies.size());

            // Step 2: Validate movies
            boolean moviesValid = true;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    moviesValid = false;
                    break;
                }
                movieValidator.getId_list().add(movie.getMovieID());
            }
            assertTrue(moviesValid);

            // Step 3: Read users
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());
            assertEquals(2, users.size());

            // Step 4: Validate users
            for (User user : users) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
            }

            // Step 5: Link movies to users
            for (User user : users) {
                assertTrue(user.setLikedMovies(movies));
            }

            // Step 6: Generate recommendations
            for (User user : users) {
                recommendationEngine.GetRecommendations(user, movies);
            }

            // Verify recommendations
            // John likes Matrix (Action, Sci-Fi) -> should get Matrix, Inception (both have Sci-Fi)
            ArrayList<String> johnRecs = users.get(0).getRecMovies();
            assertTrue(johnRecs.contains("The Matrix"));
            assertTrue(johnRecs.contains("Inception")); // Shares Sci-Fi genre
            assertFalse(johnRecs.contains("The Hangover")); // No shared genre

            // Jane likes Hangover (Comedy) -> should only get Hangover
            ArrayList<String> janeRecs = users.get(1).getRecMovies();
            assertTrue(janeRecs.contains("The Hangover"));
            assertEquals(1, janeRecs.size());
        }

        @Test
        @DisplayName("Error handling: Invalid movie title stops processing")
        void testErrorHandlingInvalidMovieTitle() throws IOException {
            Path moviesFile = testDir.resolve("invalid_movies.txt");
            Files.writeString(moviesFile, "invalid title,M001\nAction"); // lowercase title

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());

            boolean hasError = false;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    hasError = true;
                    break;
                }
            }

            assertTrue(hasError);
            assertFalse(movieValidator.getExceptionHandler().getErrorLog().isEmpty());
        }

        @Test
        @DisplayName("Error handling: Invalid user name stops processing")
        void testErrorHandlingInvalidUserName() throws IOException {
            Path usersFile = testDir.resolve("invalid_users.txt");
            Files.writeString(usersFile, " Invalid Name,123456789\nM001"); // starts with space

            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            boolean hasError = false;
            for (User user : users) {
                if (!userValidator.validateUserName(user.getName())) {
                    hasError = true;
                    break;
                }
            }

            assertTrue(hasError);
            assertFalse(userValidator.getExceptionHandler().getErrorLog().isEmpty());
        }
    }
}

